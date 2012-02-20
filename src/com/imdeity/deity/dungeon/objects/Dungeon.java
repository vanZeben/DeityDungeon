package com.imdeity.deity.dungeon.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deity.dungeon.helpers.DungeonMobs;
import com.imdeity.deityapi.Deity;

public class Dungeon {

	private String regionName = "";
	private ArrayList<String> spawners = new ArrayList<String>();
	public boolean use = false;
	private int bossSpawnRate = 5;

	public Dungeon(String regionName, int bossSpawnRate) {
		this.regionName = regionName;
		this.bossSpawnRate = bossSpawnRate;
	}

	public void init() {
		this.setSpawners();
		this.use = true;
		this.spawnMobs();
	}

	public void spawnMobs() {
		for (String s : spawners) {
			Deity.server.getServer().getScheduler().scheduleAsyncDelayedTask(Deity.plugin, DungeonMobs.schedualMobSpawn(regionName, s));
		}
	}

	public void spawnBoss() {
		Deity.server.getServer().getScheduler().scheduleAsyncDelayedTask(Deity.plugin, DungeonMobs.schedualMobBossSpawn(regionName),  60 * 20);
		Deity.server.getServer().getScheduler().scheduleAsyncDelayedTask(Deity.plugin, new Runnable() {
			public void run() {
				DungeonStorage.getDungeon(regionName).sendEventMessage("&7A shiver runs down your spine...");
			}
		}, /*((this.bossSpawnRate / 4) * 3) * 60*/30 * 20);
	}

	public void sendEventMessage(String msg) {
		for (Player p : this.getPlayersInside()) {
			DeityDungeon.chat.sendPlayerMessage(p, msg);
		}
	}

	public void setSpawners() {
		int i = 1;
		while (DeityDungeon.settings.getSpawnMobType(this.regionName, "" + i) != null) {
			this.spawners.add(i + "");
			i++;
		}
	}

	public Location getMaxLocation() {
		return Deity.sec.toLocation(Deity.server.getServer().getWorld(DeityDungeon.settings.world), Deity.sec.getRegionFromName(Deity.server.getServer().getWorld(DeityDungeon.settings.world), this.regionName).getMaximumPoint());
	}

	public Location getMinLocation() {
		return Deity.sec.toLocation(Deity.server.getServer().getWorld(DeityDungeon.settings.world), Deity.sec.getRegionFromName(Deity.server.getServer().getWorld(DeityDungeon.settings.world), this.regionName).getMinimumPoint());
	}

	public boolean compareLocation(int x, int y, int z) {
		int minX = (int) this.getMinLocation().getX();
		int maxX = (int) this.getMaxLocation().getX();
		if (minX > maxX) {
			int tmpMin = minX;
			minX = maxX;
			maxX = tmpMin;
		}
		if (minX <= x && x <= maxX) {
			int minZ = (int) this.getMinLocation().getZ();
			int maxZ = (int) this.getMaxLocation().getZ();
			if (minZ > maxZ) {
				int tmpMin = minZ;
				minZ = maxZ;
				maxZ = tmpMin;
			}
			if (minZ <= z && z <= maxZ) {
				int minY = (int) this.getMinLocation().getY();
				int maxY = (int) this.getMaxLocation().getY();
				if (minY > maxY) {
					int tmpMin = minY;
					minY = maxY;
					maxY = tmpMin;
				}
				if (minZ <= z && z <= maxZ) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Entity> getMobsInside() {
		List<Entity> entities = Deity.server.getServer().getWorld(DeityDungeon.settings.world).getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e instanceof Player) {
				entities.remove(i);
				continue;
			}
			if (!compareLocation((int) e.getLocation().getX(), (int) e.getLocation().getY(), (int) e.getLocation().getZ())) {
				entities.remove(i);
				continue;
			}
		}
		return entities;
	}

	public List<Player> getPlayersInside() {
		List<Player> players = Deity.server.getServer().getWorld(DeityDungeon.settings.world).getPlayers();
		for (int i = 0; i < players.size(); i++) {
			Entity e = players.get(i);
			if (!compareLocation((int) e.getLocation().getX(), (int) e.getLocation().getY(), (int) e.getLocation().getZ())) {
				players.remove(i);
			}
		}
		return players;
	}

	public boolean isInDungeon(String player) {
		for (Player p : this.getPlayersInside()) {
			if (p.getName().equalsIgnoreCase(player)) {
				return true;
			}
		}
		return false;
	}

	public class ButcherTask implements Runnable {

		public void run() {
			try {
				for (Entity e : Deity.server.getServer().getWorld(DeityDungeon.settings.world).getEntities()) {
					if (compareLocation((int) e.getLocation().getX(), (int) e.getLocation().getY(), (int) e.getLocation().getZ())) {
						if (e instanceof Player) {
							((Player) e).setHealth(0);
							// TODO send time up message
							System.out.println("Player Lost");
						} else {
							e.remove();
							System.out.println("Removing Entity");
						}
					}
				}
			} catch (Exception ex) {
			}
		}

	}

	public class CountdownTask implements Runnable {

		private Location minPoint;
		private Location maxPoint;
		private double timeLeft = 0;

		public CountdownTask(Location minPoint, Location maxPoint, double timeLeft) {
			this.minPoint = minPoint;
			this.maxPoint = maxPoint;
			this.timeLeft = timeLeft;
		}

		public void run() {
			try {
				for (Entity e : minPoint.getWorld().getEntities()) {
					if (compareLocation((int) e.getLocation().getX(), (int) e.getLocation().getY(), (int) e.getLocation().getZ())) {
						if (e instanceof Player) {
							DeityDungeon.chat.sendPlayerMessage((Player) e, "&c" + (this.timeLeft == 1 ? " 1 minute" : this.timeLeft + " minutes") + " left!");
							timeLeft -= 1;
						}
					}
				}
			} catch (Exception ex) {
			}
		}

		public boolean compareLocation(int x, int y, int z) {
			int minX = (int) minPoint.getX();
			int maxX = (int) maxPoint.getX();
			if (minX > maxX) {
				int tmpMin = minX;
				minX = maxX;
				maxX = tmpMin;
			}
			if (minX <= x && x <= maxX) {
				int minZ = (int) minPoint.getZ();
				int maxZ = (int) maxPoint.getZ();
				if (minZ > maxZ) {
					int tmpMin = minZ;
					minZ = maxZ;
					maxZ = tmpMin;
				}
				if (minZ <= z && z <= maxZ) {
					int minY = (int) minPoint.getY();
					int maxY = (int) maxPoint.getY();
					if (minY > maxY) {
						int tmpMin = minY;
						minY = maxY;
						maxY = tmpMin;
					}
					if (minZ <= z && z <= maxZ) {
						return true;
					}
				}
			}
			return false;
		}
	}
	//
	// protected void playPotionEffect(final Player player, final LivingEntity
	// entity, int color, int duration) {
	// final DataWatcher dw = new DataWatcher();
	// dw.a(8, Integer.valueOf(0));
	// dw.watch(8, Integer.valueOf(color));
	//
	// Packet40EntityMetadata packet = new
	// Packet40EntityMetadata(entity.getEntityId(), dw);
	// ((CraftPlayer)player).getHandle().netServerHandler.sendPacket(packet);
	//
	// Bukkit.getScheduler().scheduleSyncDelayedTask(Deity.plugin, new
	// Runnable() {
	// public void run() {
	// DataWatcher dwReal =
	// ((CraftLivingEntity)entity).getHandle().getDataWatcher();
	// dw.watch(8, dwReal.getInt(8));
	// Packet40EntityMetadata packet = new
	// Packet40EntityMetadata(entity.getEntityId(), dw);
	// ((CraftPlayer)player).getHandle().netServerHandler.sendPacket(packet);
	// }
	// }, duration);
	// }
}
