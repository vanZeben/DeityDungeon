package com.imdeity.deity.dungeon.objects;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deityapi.Deity;
import com.imdeity.deityapi.records.DatabaseResults;

public class DungeonSql {

	public static void loadDungeonTables() {
		Deity.data.getDB().Write("CREATE TABLE IF NOT EXISTS " + Deity.data.getDB().tableName("deity_", "dungeons") + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`name` VARCHAR( 32 ) NOT NULL ," + "`world` VARCHAR( 32 ) NOT NULL ," + "`boss_spawn_interval` INT( 10 ) NOT NULL DEFAULT '10'," + "`death_countdown` INT( 10 ) NOT NULL DEFAULT '10' ," + "`mob_life_span` INT( 2 ) NOT NULL DEFAULT '5' ," + "UNIQUE (`name`)) ENGINE = MYISAM ;");
		Deity.data.getDB().Write("CREATE TABLE IF NOT EXISTS " + Deity.data.getDB().tableName("deity_", "dungeon_mobs") + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`name` VARCHAR( 32 ) NOT NULL ," + "`dungeon_id` INT( 16 ) NOT NULL ," + "`damage_dealt` INT( 8 ) NOT NULL ," + "`damage_received` INT( 8 ) NOT NULL ," + "`spawn_world` VARCHAR( 32 ) NOT NULL ," + "`spawn_loc_x` INT( 8 ) NOT NULL ," + "`spawn_loc_y` INT( 8 ) NOT NULL ," + "`spawn_loc_z` INT( 8 ) NOT NULL ," + "`spawn_amount` INT( 2 ) NOT NULL ," + "`is_boss` INT( 1 ) NOT NULL DEFAULT '0' ," + "`exp_dropped` INT( 2 ) NOT NULL DEFAULT '0'" + ") ENGINE = MYISAM ;");
		Deity.data.getDB().Write("CREATE TABLE IF NOT EXISTS " + Deity.data.getDB().tableName("deity_", "dungeon_mob_drops") + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`mob_id` INT( 16 ) NOT NULL ," + "`item_id` INT( 16 ) NOT NULL ," + "`item_damage_value` INT( 16 ) NOT NULL ," + "`item_amount` INT( 2 ) NOT NULL" + ") ENGINE = MYISAM ;");
	}

	public static int getDungeonIdFromName(String name) {
		String sql = "";
		DatabaseResults query = null;
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

	public static HashMap<String, Dungeon> getDungeons() {
		HashMap<String, Dungeon> dungeons = new HashMap<String, Dungeon>();
		String sql = "SELECT `id`, `name`, `world`, `boss_spawn_interval`, `death_countdown`, `mob_life_span` FROM " + Deity.data.getDB().tableName("deity_", "dungeons") + ";";
		DatabaseResults query = Deity.data.getDB().Read2(sql);
		if (query != null && query.hasRows()) {
			for (int i = 0; i < query.rowCount(); i++) {
				try {
					dungeons.put(query.getString(i, "name"), new Dungeon(query.getInteger(i, "id"), query.getString(i, "name"), Deity.server.getServer().getWorld(query.getString(i, "world")), query.getInteger(i, "boss_spawn_interval"), query.getInteger(i, "death_countdown"), query.getInteger(i, "mob_life_span")));
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
		String sql = "SELECT `id`, `name`, `damage_dealt`, `damage_received`, `spawn_world`, `spawn_loc_x`, `spawn_loc_y`, `spawn_loc_z`, `spawn_amount`, `is_boss`, `exp_dropped` FROM " + Deity.data.getDB().tableName("deity_", "dungeon_mobs") + " WHERE `dungeon_id` = ?;";
		DatabaseResults query = Deity.data.getDB().Read2(sql, id);
		if (query != null && query.hasRows()) {
			for (int i = 0; i < query.rowCount(); i++) {
				try {
					ArrayList<ItemStack> tmp = new ArrayList<ItemStack>();
					sql = "SELECT `item_id`, `item_amount`, `item_damage_value` FROM " + Deity.data.getDB().tableName("deity_", "dungeon_mob_drops") + " WHERE `mob_id` = ?;";
					DatabaseResults query2 = Deity.data.getDB().Read2(sql, query.getInteger(i, "id"));
					if (query2 != null && query2.hasRows()) {
						for (int ii = 0; ii < query2.rowCount(); ii++) {
							short damage = 1;
							while (damage <= query2.getInteger(ii, "item_damage_value")) {
								damage++;
							}
							tmp.add(new ItemStack(query2.getInteger(ii, "item_id"), query2.getInteger(ii, "item_amount"), damage));
						}
					}
					spawners.add(new Spawner(query.getInteger(i, "id"), id, Deity.mob.getEntityType(query.getString(i, "name")), query.getInteger(i, "spawn_amount"), query.getInteger(i, "damage_dealt"), query.getInteger(i, "damage_received"), new Location(Deity.server.getServer().getWorld(query.getString(i, "spawn_world")), query.getInteger(i, "spawn_loc_x"), query.getInteger(i, "spawn_loc_y"), query.getInteger(i, "spawn_loc_z")), (query.getInteger(i, "is_boss") == 0 ? false : true), tmp, query.getInteger(i, "exp_dropped")));
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

	public static void addDungeon(String dungeon, String world, int bossSpawnInterval, int deathCountdown, int mobLifeSpan) {
		String sql = "INSERT INTO " + Deity.data.getDB().tableName("deity_", "dungeons") + " (`name`, `world`, `boss_spawn_interval`, `death_countdown`, `mob_life_span`) VALUES (?,?,?,?,?);";
		Deity.data.getDB().Write(sql, dungeon, world, bossSpawnInterval, deathCountdown, mobLifeSpan);
	}

	public static void addDungeonMobSpawn(String dungeon, String name, int damageDealt, int damageReceived, Location location, int amount, boolean isBoss, int expDropped) {
		String sql = "INSERT INTO " + Deity.data.getDB().tableName("deity_", "dungeon_mobs") + " (name, dungeon_id, damage_dealt, damage_received, spawn_world, spawn_loc_x, spawn_loc_y, spawn_loc_z, spawn_amount, is_boss, exp_dropped) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
		Deity.data.getDB().Write(sql, name, DungeonSql.getDungeonIdFromName(dungeon), damageDealt, damageReceived, location.getWorld().getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ(), amount, (isBoss ? 1 : 0), expDropped);
	}

	public static void addDungeonMobDrops(int mobId, int itemId, int itemDamage, int itemAmount) {
		String sql = "INSERT INTO " + Deity.data.getDB().tableName("deity_", "dungeon_mob_drops") + " (mob_id, item_id, item_damage_value, item_amount) VALUES (?,?,?,?);";
		Deity.data.getDB().Write(sql, mobId, itemId, itemDamage, itemAmount);
	}
}
