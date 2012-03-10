package com.imdeity.deity.dungeon.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deityapi.Deity;

public class Spawner {

	public int id = 0;
	public int dungeonId = 0;
	public EntityType mob;
	public int amount;
	public int damageDealt = 0;
	public int damageReceived = 0;
	public Location spawnLocation = null;
	public boolean isBoss = false;
	public ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
	public int expDropped = 10;
	private ArrayList<Integer> spawnedId = new ArrayList<Integer>();

	public Spawner(int id, int dungeonId, EntityType mob, int amount, int damageDealt, int damageReceived, Location spawnLocation, boolean isBoss, ArrayList<ItemStack> drops, int expDropped) {
		this.id = id;
		this.dungeonId = dungeonId;
		this.mob = mob;
		this.amount = amount;
		this.damageDealt = damageDealt;
		this.damageReceived = damageReceived;
		this.spawnLocation = spawnLocation;
		this.isBoss = isBoss;
		this.drops = drops;
		this.expDropped = expDropped;
	}

	public void spawnMobs() {
		for (Integer i : Deity.mob.schedualMobSpawn(mob, amount, spawnLocation)) {
			this.addMob(i);
		}
	}

	public void addMob(int id) {
		this.spawnedId.add(id);
	}

	public ArrayList<Integer> getSpawnedMobs() {
		return this.spawnedId;
	}

	public void killMobs() {
		for (int id : spawnedId) {
			Deity.mob.schedualMobDespawn(id, this.spawnLocation.getWorld());
		}
		this.spawnedId.clear();
	}

	public int compareLocation(Location location) {
		return (int) fixLocation(this.spawnLocation).distance(fixLocation(location));
	}

	private Location fixLocation(Location loc) {
		loc.setX((int) loc.getX());
		loc.setY((int) loc.getY());
		loc.setZ((int) loc.getZ());
		loc.setPitch(0);
		loc.setYaw(0);
		return loc;
	}

	public List<String> getFormatted() {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add("&3Spawner ID: &b" + this.id);
		if (this.mob != null) {
			tmp.add("&3Mob Name: &b" + this.mob.toString());
		}
		tmp.add("&3Mob Amount: &b" + this.amount);
		tmp.add("&3Is Boss: &b" + this.isBoss);
		tmp.add("&3Damage Dealt: &b" + this.damageDealt);
		tmp.add("&3Damage Received: &b" + this.damageReceived);
		tmp.add("&3Exp Dropped: &b" + this.expDropped);
		return tmp;
	}
}
