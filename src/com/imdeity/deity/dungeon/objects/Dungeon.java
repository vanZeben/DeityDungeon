package com.imdeity.deity.dungeon.objects;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deityapi.Deity;

public class Dungeon {

	public int id;
	public String regionName = "";
	public World world;
	public ArrayList<Spawner> spawners = new ArrayList<Spawner>();
	public ArrayList<Player> players = new ArrayList<Player>();
	public int bossSpawnRate = 5;
	public int deathCountdown = 10;

	public Dungeon(int id, String regionName, World world, int bossSpawnRate, int deathCountdown) {
		this.id = id;
		this.regionName = regionName;
		this.world = world;
		this.bossSpawnRate = bossSpawnRate;
		this.deathCountdown = deathCountdown;
		this.init();
	}

	public void init() {
		this.loadSpawners();
		this.spawnBoss();
	}

	public void loadSpawners() {
		this.spawners = DungeonSql.getDungeonSpawners(this.id);
	}

	public void spawnBoss() {
		Deity.server.getServer().getScheduler().scheduleAsyncDelayedTask(DeityDungeon.plugin, new DungeonTasks.BossSpawner(this.getBossSpawner(), this.regionName));
	}

	public Location getMaxLocation() {
		return Deity.sec.toLocation(this.world, Deity.sec.getRegionFromName(this.world, this.regionName).getMaximumPoint());
	}

	public Location getMinLocation() {
		return Deity.sec.toLocation(this.world, Deity.sec.getRegionFromName(this.world, this.regionName).getMinimumPoint());
	}

	public Spawner getSpawner(int id) {
		for (Spawner s : this.spawners) {
			if (s.id == id) {
				return s;
			}
		}
		return null;
	}

	public Spawner getSpawner(Location location) {
		for (Spawner s : this.spawners) {
			if (s.spawnLocation.equals(location)) {
				return s;
			}
		}
		return null;
	}

	public void spawnMobsFromSpawner(int spawnerId) {
		this.getSpawner(spawnerId).spawnMobs();
	}

	public void spawnAllMobs() {
		for (Spawner s : this.spawners) {
			if (!s.isBoss) {
				s.spawnMobs();
			}
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

	public boolean hasPlayer(String player) {
		for (Player p : this.players) {
			if (p.getName().equalsIgnoreCase(player)) {
				return true;
			}
		}
		return false;
	}

	public boolean isActive() {
		return !this.players.isEmpty();
	}

	public void addPlayer(Player player) {
		this.players.add(player);
		this.sendMessage(player.getName() + " has come to join the battle!");
		long time = this.deathCountdown * 60 * 20;
		Deity.server.getServer().getScheduler().scheduleSyncDelayedTask(DeityDungeon.plugin, new DungeonTasks.PlayerMessager(player, "You have " + ((time * 0.10) / 20) + " seconds remaining!"), (long) (time * 0.90));
		Deity.server.getServer().getScheduler().scheduleAsyncDelayedTask(DeityDungeon.plugin, new DungeonTasks.PlayerCounter(player, this.regionName), (long) (time * 0.10));
	}

	public void removePlayer(Player player) {
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
		for (Player p : this.players) {
			DeityDungeon.chat.sendPlayerMessage(p, msg);
		}
		DeityDungeon.chat.sendConsoleMessage(" [" + this.regionName + "] " + msg);
	}

}
