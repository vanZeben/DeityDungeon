package com.imdeity.deity.dungeon.objects;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

import com.imdeity.deityapi.Deity;
import com.imdeity.deityapi.records.DatabaseResults;

public class DungeonSql {

	public static void loadDungeonTables() {
		Deity.data.getDB().Write("CREATE TABLE IF NOT EXISTS " + Deity.data.getDB().tableName("deity_", "dungeons") + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`name` VARCHAR( 32 ) NOT NULL ," + "`world` VARCHAR( 32 ) NOT NULL ," + "`boss_spawn_interval` INT( 10 ) NOT NULL DEFAULT '10'," + "`death_countdown` INT( 10 ) NOT NULL DEFAULT '10'," + "UNIQUE (`name`)) ENGINE = MYISAM ;");
		Deity.data.getDB().Write("CREATE TABLE IF NOT EXISTS " + Deity.data.getDB().tableName("deity_", "dungeon_mobs") + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`name` VARCHAR( 32 ) NOT NULL ," + "`dungeon_id` INT( 16 ) NOT NULL ," + "`damage_dealt` INT( 8 ) NOT NULL ," + "`damage_received` INT( 8 ) NOT NULL ," + "`spawn_world` VARCHAR( 32 ) NOT NULL ," + "`spawn_loc_x` INT( 8 ) NOT NULL ," + "`spawn_loc_y` INT( 8 ) NOT NULL ," + "`spawn_loc_z` INT( 8 ) NOT NULL ," + "`spawn_amount` INT( 2 ) NOT NULL ," + "`is_boss` INT( 1 ) NOT NULL DEFAULT '0'" + ") ENGINE = MYISAM ;");
		Deity.data.getDB().Write("CREATE TABLE IF NOT EXISTS " + Deity.data.getDB().tableName("deity_", "dungeon_mob_drops") + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`mob_id` INT( 16 ) NOT NULL ," + "`item_id` INT( 16 ) NOT NULL ," + "`item_damage_value` INT( 16 ) NOT NULL ," + "`item_amount` INT( 2 ) NOT NULL" + ") ENGINE = MYISAM ;");
	}

	public static boolean getSpawnLocation(String entityName, Location location) {
		String sql = "";
		DatabaseResults query = null;
		sql = "SELECT `id` FROM " + Deity.data.getDB().tableName("deity_", "dungeon_mobs") + " WHERE name = ? AND spawn_world = ? AND spawn_loc_x = ? AND spawn_loc_y = ? AND spawn_loc_z = ?;";
		query = Deity.data.getDB().Read2(sql, entityName, location.getWorld().getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
		if (query != null && query.hasRows()) {
			return true;
		}
		return false;
	}

	public static int getDungeonIdFromName(String name) {
		String sql = "";
		DatabaseResults query = null;
		System.out.println("name:" + name);
		sql = "SELECT `id` FROM " + Deity.data.getDB().tableName("deity_", "dungeons") + " WHERE name = ?;";
		query = Deity.data.getDB().Read2(sql, name);
		if (query != null && query.hasRows()) {
			try {
				return query.getInteger(0, "id");
			} catch (SQLDataException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	//
	// public static String getSpawnDungeon(String entityName, Location
	// location) {
	// String sql = "";
	// DatabaseResults query = null;
	// sql = "SELECT d.`name` FROM " + Deity.data.getDB().tableName("deity_",
	// "dungeon_mobs") + " dm, " + Deity.data.getDB().tableName("deity_",
	// "dungeons") +
	// " d WHERE dm.`dungeon_id` = d.`id` AND dm.`name` = ? AND dm.`spawn_world` = ? AND dm.`spawn_loc_x` = ? AND dm.`spawn_loc_y` = ? AND dm.`spawn_loc_z` = ?;";
	// query = Deity.data.getDB().Read2(sql, entityName,
	// location.getWorld().getName(), (int) location.getX(), (int)
	// location.getY(), (int) location.getZ());
	// if (query.hasRows()) {
	// try {
	// return query.getString(0, "name");
	// } catch (SQLDataException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }

	public static HashMap<String, Dungeon> getDungeons() {
		HashMap<String, Dungeon> dungeons = new HashMap<String, Dungeon>();
		String sql = "SELECT `id`, `name`, `world`, `boss_spawn_interval`, `death_countdown` FROM " + Deity.data.getDB().tableName("deity_", "dungeons") + ";";
		DatabaseResults query = Deity.data.getDB().Read2(sql);
		if (query != null && query.hasRows()) {
			for (int i = 0; i < query.rowCount(); i++) {
				try {
					dungeons.put(query.getString(i, "name"), new Dungeon(query.getInteger(i, "id"), query.getString(i, "name"), Deity.server.getServer().getWorld(query.getString(i, "world")), query.getInteger(i, "boss_spawn_interval"), query.getInteger(i, "death_countdown")));
				} catch (SQLDataException e) {
					e.printStackTrace();
				}
			}
		}
		if (dungeons.isEmpty()) {
			return null;
		}
		return dungeons;
	}

	public static ArrayList<Spawner> getDungeonSpawners(int id) {
		ArrayList<Spawner> spawners = new ArrayList<Spawner>();
		String sql = "SELECT `id`, `name`, `damage_dealt`, `damage_received`, `spawn_world`, `spawn_loc_x`, `spawn_loc_y`, `spawn_loc_z`, `spawn_amount`, `is_boss` FROM " + Deity.data.getDB().tableName("deity_", "dungeon_mobs") + " WHERE `dungeon_id` = ?;";
		DatabaseResults query = Deity.data.getDB().Read2(sql, id);
		if (query != null && query.hasRows()) {
			for (int i = 0; i < query.rowCount(); i++) {
				try {
					spawners.add(new Spawner(query.getInteger(i, "id"), id, Deity.mob.getCreatureType(query.getString(i, "name")), query.getInteger(i, "spawn_amount"), query.getInteger(i, "damage_dealt"), query.getInteger(i, "damage_received"), new Location(Deity.server.getServer().getWorld(query.getString(i, "spawn_world")), query.getInteger(i, "spawn_loc_x"), query.getInteger(i, "spawn_loc_y"), query.getInteger(i, "spawn_loc_z")), (query.getInteger(i, "is_boss") == 0 ? false : true)));
				} catch (SQLDataException e) {
					e.printStackTrace();
				}
			}
		}
		if (spawners.isEmpty()) {
			return null;
		}
		return spawners;
	}

	public static void addDungeonMobSpawn(String dungeon, String name, int damageDealt, int damageReceived, Location location, int amount, boolean isBoss) {
		String sql = "INSERT INTO " + Deity.data.getDB().tableName("deity_", "dungeon_mobs") + " (name, dungeon_id, damage_dealt, damage_received, spawn_world, spawn_loc_x, spawn_loc_y, spawn_loc_z, spawn_amount, is_boss) VALUES (?,?,?,?,?,?,?,?,?,?);";
		Deity.data.getDB().Write(sql, name, DungeonSql.getDungeonIdFromName(dungeon), damageDealt, damageReceived, location.getWorld().getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ(), amount, (isBoss ? 1 : 0));
	}

	public static void addDungeonMobDrops(String name, int itemId, int itemDamage, int itemAmount) {
		String sql = "INSERT INTO " + Deity.data.getDB().tableName("deity_", "dungeon_mob_drops") + " (mob_id, item_id, item_damage_value, item_amount) VALUES ((SELECT id FROM " + Deity.data.getDB().tableName("deity_", "dungeon_mobs") + " WHERE name = ?), ?,?,?);";
		Deity.data.getDB().Write(sql, name, itemId, itemDamage, itemAmount);
	}
}
