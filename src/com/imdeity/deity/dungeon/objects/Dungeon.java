package com.imdeity.deity.dungeon.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deityapi.Deity;
import com.imdeity.deityapi.utils.HumanTime;

public class Dungeon {

	public int id;
	public String regionName = "";
	public World world;
	public ArrayList<Spawner> spawners = new ArrayList<Spawner>();
	public HashMap<Player, int[]> players = new HashMap<Player, int[]>();
	public int bossSpawnRate = 5;
	public int bossTaskId = -1;
	public int mobTaskId = -1;
	public int deathCountdown = 10;
	public int mobLifeSpan = -1;

	public Dungeon(int id, String regionName, World world, int bossSpawnRate, int deathCountdown, int mobLifeSpan) {
		this.id = id;
		this.regionName = regionName;
		this.world = world;
		this.bossSpawnRate = bossSpawnRate;
		this.deathCountdown = deathCountdown;
		this.mobLifeSpan = mobLifeSpan;
		this.loadSpawners();
	}

	public void loadSpawners() {
		this.spawners = DungeonSql.getDungeonSpawners(this.id);
	}

	public void spawnBoss() {
		this.bossTaskId = Deity.server.getServer().getScheduler().scheduleAsyncDelayedTask(DeityDungeon.plugin, new DungeonTasks.BossSpawner(this.regionName));
	}

	public void spawnMobs() {
		this.mobTaskId = Deity.server.getServer().getScheduler().scheduleAsyncDelayedTask(DeityDungeon.plugin, new DungeonTasks.MobSpawner(this.regionName));
	}

	public Spawner getSpawner(int id) {
		for (Spawner s : this.spawners) {
			if (s.id == id) {
				return s;
			}
		}
		return null;
	}

	public Spawner getSpawnerFromMobId(int entityId) {
		for (Spawner s : this.spawners) {
			if (s.getSpawnedMobs().contains(entityId)) {
				return s;
			}
		}
		return null;
	}

	public void spawnMobsFromSpawner(int spawnerId) {
		this.getSpawner(spawnerId).spawnMobs();
	}

	public void spawnMobsExceptBoss() {
		for (Spawner s : this.spawners) {
			if (!s.isBoss) {
				s.spawnMobs();
			}
		}
	}

	public void spawnMobsBoss() {
		for (Spawner s : this.spawners) {
			if (s.isBoss) {
				s.spawnMobs();
			}
		}
	}

	public void spawnAllMobs() {
		this.spawnBoss();
		this.spawnMobs();
		this.spawnMobsExceptBoss();
	}

	public void killAllMobs() {
		Deity.server.getServer().getScheduler().cancelTask(bossTaskId);
		Deity.server.getServer().getScheduler().cancelTask(mobTaskId);
		for (Spawner s : this.spawners) {
			s.killMobs();
		}
	}

	public Spawner getBossSpawner() {
		for (Spawner s : this.spawners) {
			if (s.isBoss) {
				return s;
			}
		}
		return null;
	}

	public boolean isBoss(int entityId) {
		return this.getBossSpawner().getSpawnedMobs().contains(entityId);
	}

	public Location getMaxLocation() {
		return Deity.sec.toLocation(this.world, Deity.sec.getRegionFromName(this.world, this.regionName).getMaximumPoint());
	}

	public Location getMinLocation() {
		return Deity.sec.toLocation(this.world, Deity.sec.getRegionFromName(this.world, this.regionName).getMinimumPoint());
	}

	public boolean hasPlayer(Player player) {
		return this.players.containsKey(player);
	}

	public boolean isActive() {
		return !this.players.isEmpty();
	}

	public void addPlayer(Player player) {
		this.sendMessage(player.getName() + " has come to join the battle!");
		long time = this.deathCountdown * 60 * 20;
		int[] timers = { Deity.server.getServer().getScheduler().scheduleSyncDelayedTask(DeityDungeon.plugin, new DungeonTasks.PlayerMessager(player, "You have " + HumanTime.exactly((long) ((time * 0.10) / 20)) + " remaining!"), (long) (time * 0.90)), Deity.server.getServer().getScheduler().scheduleAsyncDelayedTask(DeityDungeon.plugin, new DungeonTasks.PlayerCounter(player, this.regionName), time) };
		this.players.put(player, timers);
	}

	public void removePlayer(Player player) {
		for (int i : this.players.get(player)) {
			Deity.server.getServer().getScheduler().cancelTask(i);
		}
		this.players.remove(player);
		this.sendMessage(player.getName() + " left the battle!");
	}

	public void removePlayerFailed(Player player) {
		this.players.remove(player);
		Deity.player.teleport(player, this.world.getSpawnLocation());
		this.sendMessage(player.getName() + " failed to kill the boss in time!");
	}

	public void sendMessage(String msg) {
		if (!this.isActive()) {
			return;
		}
		for (Player p : this.players.keySet()) {
			DeityDungeon.chat.sendPlayerMessage(p, msg);
		}
		DeityDungeon.chat.sendConsoleMessage("[" + this.regionName + "] " + msg);
	}

}
