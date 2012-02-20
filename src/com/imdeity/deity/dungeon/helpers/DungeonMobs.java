package com.imdeity.deity.dungeon.helpers;

import org.bukkit.Location;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deityapi.Deity;

public class DungeonMobs implements Runnable {

	private String name = "";
	private CreatureType type = null;
	private Location spawnLocation = null;
	private int amount = 0;

	public DungeonMobs(String region, String name) {
		this.name = name;
		this.type = DungeonMobs.getCreatureType(DeityDungeon.settings.getSpawnMobType(region, this.name));
		this.spawnLocation = DeityDungeon.settings.getSpawnMobLocation(region, this.name);
		this.amount = DeityDungeon.settings.getSpawnMobAmount(region, this.name);
	}

	public DungeonMobs(CreatureType type, Location location, int amount) {
		this.name = "";
		this.type = type;
		this.spawnLocation = location;
		this.amount = amount;
	}

	public static String getMobName(Entity entity) {
		if (entity instanceof Blaze) {
			return "blaze";
		} else if (entity instanceof CaveSpider) {
			return "cavespider";
		} else if (entity instanceof Creeper) {
			return "creeper";
		} else if (entity instanceof Enderman) {
			return "enderman";
		} else if (entity instanceof Ghast) {
			return "ghast";
		} else if (entity instanceof MagmaCube) {
			return "magmacube";
		} else if (entity instanceof PigZombie) {
			return "pigzombie";
		} else if (entity instanceof Silverfish) {
			return "silverfish";
		} else if (entity instanceof Skeleton) {
			return "skeleton";
		} else if (entity instanceof Spider) {
			return "spider";
		} else if (entity instanceof Wolf) {
			return "wolf";
		} else if (entity instanceof Zombie) {
			return "zombie";
		}
		return null;
	}

	public static CreatureType getCreatureType(String name) {
		if (name == null || name.equalsIgnoreCase("")) {
			return null;
		} else if (name.equalsIgnoreCase("blaze")) {
			return CreatureType.BLAZE;
		} else if (name.equalsIgnoreCase("cavespider")) {
			return CreatureType.CAVE_SPIDER;
		} else if (name.equalsIgnoreCase("creeper")) {
			return CreatureType.CREEPER;
		} else if (name.equalsIgnoreCase("enderman")) {
			return CreatureType.ENDERMAN;
		} else if (name.equalsIgnoreCase("ghast")) {
			return CreatureType.GHAST;
		} else if (name.equalsIgnoreCase("magmacube")) {
			return CreatureType.MAGMA_CUBE;
		} else if (name.equalsIgnoreCase("pigzombie")) {
			return CreatureType.PIG_ZOMBIE;
		} else if (name.equalsIgnoreCase("silverfish")) {
			return CreatureType.SILVERFISH;
		} else if (name.equalsIgnoreCase("skeleton")) {
			return CreatureType.SKELETON;
		} else if (name.equalsIgnoreCase("spider")) {
			return CreatureType.SPIDER;
		} else if (name.equalsIgnoreCase("wolf")) {
			return CreatureType.WOLF;
		} else if (name.equalsIgnoreCase("zombie")) {
			return CreatureType.ZOMBIE;
		}
		return null;
	}

	public static DungeonMobs schedualMobSpawn(String region, String name) {
		return new DungeonMobs(region, name);
	}

	public static DungeonMobs schedualMobSpawn(CreatureType type, Location location, int amount) {
		return new DungeonMobs(type, location, amount);
	}

	public static DungeonMobs schedualMobBossSpawn(String region) {
		return new DungeonMobs(region, "boss");
	}

	@Override
	public void run() {
		try {
			int entityId = 0;
			for (int i = 0; i < amount; i++) {
				if (this.type != null) {
					if (this.name.equalsIgnoreCase("boss")) {
						entityId = spawnLocation.getWorld().spawnCreature(this.spawnLocation, type).getEntityId();
					} else {
						spawnLocation.getWorld().spawnCreature(this.spawnLocation, type);
					} 
				}
			}
			if (name != null && name.equalsIgnoreCase("boss")) {
				try {
					Thread.sleep(5000L);
					for (Entity e : Deity.server.getServer().getWorld(DeityDungeon.settings.world).getEntities()) {
						if (e.getEntityId() == entityId) {
							e.remove();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
		}

	}

}
