package com.imdeity.deity.dungeon.objects;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import com.imdeity.deityapi.Deity;

public class DungeonSettings {

	private YamlConfiguration config = new YamlConfiguration();
	private File file = new File("plugins/DeityDungeon/config.yml");
	public String world = "skyblock";

	public void loadDefaults() {
		this.config = YamlConfiguration.loadConfiguration(this.file);
		if (!this.config.contains("dungeon.region.dungeon_1.damage_dealt.zombie")) {
			this.config.set("dungeon.region.dungeon_1.damage_dealt.zombie", -1);
		}
		if (!this.config.contains("dungeon.region.dungeon_1.damage_received.zombie")) {
			this.config.set("dungeon.region.dungeon_1.damage_received.zombie", -1);
		}
		if (!this.config.contains("dungeon.region.dungeon_1.drops.zombie.item")) {
			this.config.set("dungeon.region.dungeon_1.drops.zombie.item", 371);
		}
		if (!this.config.contains("dungeon.region.dungeon_1.drops.zombie.amount")) {
			this.config.set("dungeon.region.dungeon_1.drops.zombie.amount", 2);
		}
		if (!this.config.contains("dungeon.region.dungeon_1.skeleton_flame_arrow")) {
			this.config.set("dungeon.region.dungeon_1.skeleton_flame_arrow", 5);
		}
		if (!this.config.contains("dungeon.region.dungeon_1.boss_spawn_interval")) {
			this.config.set("dungeon.region.dungeon_1.boss_spawn_interval", 5);
		}
		if (!this.config.contains("dungeon.region.dungeon_1.spawn.1.mob_type")) {
			this.config.set("dungeon.region.dungeon_1.spawn.1.mob_type", "pigzombie");
		}
		if (!this.config.contains("dungeon.region.dungeon_1.spawn.1.mob_amount")) {
			this.config.set("dungeon.region.dungeon_1.spawn.1.mob_amount", 7);
		}
		if (!this.config.contains("dungeon.region.dungeon_1.spawn.1.world")) {
			this.config.set("dungeon.region.dungeon_1.spawn.1.world", "skyblock");
		}
		if (!this.config.contains("dungeon.region.dungeon_1.spawn.1.x_coord")) {
			this.config.set("dungeon.region.dungeon_1.spawn.1.x_coord", -7);
		}
		if (!this.config.contains("dungeon.region.dungeon_1.spawn.1.y_coord")) {
			this.config.set("dungeon.region.dungeon_1.spawn.1.y_coord", 65);
		}
		if (!this.config.contains("dungeon.region.dungeon_1.spawn.1.Z_coord")) {
			this.config.set("dungeon.region.dungeon_1.spawn.1.z_coord", -37);
		}

		this.save();
	}

	public void save() {
		try {
			this.config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getRegionMobDamageDealt(String region, String mob) {
		return this.config.getInt("dungeon.region." + region + ".damage_dealt." + mob);
	}

	public int getRegionMobDamageReceived(String region, String mob) {
		return this.config.getInt("dungeon.region." + region + ".damage_received." + mob);
	}

	public int getRegionMobDropsItem(String region, String mob) {
		return this.config.getInt("dungeon.region." + region + ".drops." + mob + ".item");
	}

	public int getRegionMobDropsAmount(String region, String mob) {
		return this.config.getInt("dungeon.region." + region + ".drops." + mob + ".amount");
	}

	public int getRegionSkeletonFire(String region) {
		return this.config.getInt("dungeon.region." + region + ".skeleton_flame_arrow");
	}

	public String getSpawnMobType(String region, String name) {
		return this.config.getString("dungeon.region." + region + ".spawn." + name + ".mob_type");
	}

	public int getSpawnMobAmount(String region, String name) {
		return this.config.getInt("dungeon.region." + region + ".spawn." + name + ".mob_amount");
	}

	public Location getSpawnMobLocation(String region, String name) {
		World world = Deity.server.getServer().getWorld(this.config.getString("dungeon.region." + region + ".spawn." + name + ".world"));
		int x = this.config.getInt("dungeon.region." + region + ".spawn." + name + ".x_coord");
		int y = this.config.getInt("dungeon.region." + region + ".spawn." + name + ".y_coord");
		int z = this.config.getInt("dungeon.region." + region + ".spawn." + name + ".z_coord");
		return new Location(world, x, y, z);
	}

	public void setSpawn(String region, String name, String mobtype, int amount, Location location) {
		this.config.set("dungeon.region." + region + ".spawn." + name + ".mob_type", mobtype);
		this.config.set("dungeon.region." + region + "." + name + ".mob_amount", amount);
		this.config.set("dungeon.region." + region + "." + name + ".world", location.getWorld().getName());
		this.config.set("dungeon.region." + region + "." + name + ".x_coord", location.getBlockX());
		this.config.set("dungeon.region." + region + "." + name + ".y_coord", location.getBlockY());
		this.config.set("dungeon.region." + region + "." + name + ".z_coord", location.getBlockZ());
		this.save();
	}

	public String[] getRegionSpawnerNames(String region) {
		System.out.println("config: " + this.config.getStringList("dungeon.region." + region + ".spawn"));
		return null;
	}

	public int getMobBossSpawnInterval(String region) {
		return this.config.getInt("dungeon.region." + region + ".boss_spawn_interval");
	}
}
