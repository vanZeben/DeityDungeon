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

	public void loadDefaults() {
		this.config = YamlConfiguration.loadConfiguration(this.file);
		if (!this.config.contains("dungeon.region.dungeon_one.damage_dealt.zombie")) {
			this.config.set("dungeon.region.dungeon_one.damage_dealt.zombie", -1);
		}
		if (!this.config.contains("dungeon.region.dungeon_one.damage_received.zombie")) {
			this.config.set("dungeon.region.dungeon_one.damage_received.zombie", -1);
		}
		if (!this.config.contains("dungeon.region.dungeon_one.skeleton_flame_arrow")) {
			this.config.set("dungeon.region.dungeon_one.skeleton_flame_arrow", 5);
		}
		if (!this.config.contains("dungeon.spawn.loc_one.mob_type")) {
			this.config.set("dungeon.spawn.loc_one.mob_type", "pigzombie");
		}
		if (!this.config.contains("dungeon.spawn.loc_one.mob_amount")) {
			this.config.set("dungeon.spawn.loc_one.mob_amount", 7);
		}
		if (!this.config.contains("dungeon.spawn.loc_one.world")) {
			this.config.set("dungeon.spawn.loc_one.world", "skyblock");
		}
		if (!this.config.contains("dungeon.spawn.loc_one.x_coord")) {
			this.config.set("dungeon.spawn.loc_one.x_coord", -7);
		}
		if (!this.config.contains("dungeon.spawn.loc_one.y_coord")) {
			this.config.set("dungeon.spawn.loc_one.y_coord", 65);
		}
		if (!this.config.contains("dungeon.spawn.loc_one.Z_coord")) {
			this.config.set("dungeon.spawn.loc_one.z_coord", -37);
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

	public int getRegionSkeletonFire(String region) {
		return this.config.getInt("dungeon.region." + region + ".skeleton_flame_arrow");
	}

	public String getSpawnMob(String name) {
		return this.config.getString("dungeon.spawn." + name + ".mob_type");
	}

	public int getSpawnMobAmount(String name) {
		return this.config.getInt("dungeon.spawn." + name + ".mob_amount");
	}

	public Location getSpawnLocation(String name) {
		World world = Deity.server.getServer().getWorld(this.config.getString("dungeon.spawn." + name + ".world"));
		int x = this.config.getInt("dungeon.spawn." + name + ".x_coord");
		int y = this.config.getInt("dungeon.spawn." + name + ".y_coord");
		int z = this.config.getInt("dungeon.spawn." + name + ".z_coord");
		return new Location(world, x, y, z);
	}

	public void setSpawn(String name, String mobtype, int amount, Location location) {
		this.config.set("dungeon.spawn." + name + ".mob_type", mobtype);
		this.config.set("dungeon.spawn." + name + ".mob_amount", amount);
		this.config.set("dungeon.spawn." + name + ".world", location.getWorld().getName());
		this.config.set("dungeon.spawn." + name + ".x_coord", location.getBlockX());
		this.config.set("dungeon.spawn." + name + ".y_coord", location.getBlockY());
		this.config.set("dungeon.spawn." + name + ".z_coord", location.getBlockZ());
		this.save();
	}
}
