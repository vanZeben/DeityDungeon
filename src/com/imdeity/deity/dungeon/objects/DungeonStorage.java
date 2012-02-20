package com.imdeity.deity.dungeon.objects;

import java.util.ArrayList;

import org.bukkit.Location;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deityapi.Deity;

public class DungeonStorage {

	public static Dungeon getDungeon(String regionname) {
		return new Dungeon(regionname, DeityDungeon.settings.getMobBossSpawnInterval(regionname));
	}

	public static ArrayList<Dungeon> getDungeons() {
		ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();
		int i = 1;
		while (DeityDungeon.settings.getRegionSkeletonFire("dungeon_" + i) != 0) {
			dungeons.add(DungeonStorage.getDungeon("dungeon_" + i));
			i++;
		}
		return dungeons;
	}

	public static boolean isActive(Location location) {
		for (String s : Deity.sec.getRegionsAtLocation(location)) {
			if (s.startsWith("dungeon_")) {
				return true;
			}
		}
		return false;
	}

	public static String getActiveDungeon(Location location) {
		for (String s : Deity.sec.getRegionsAtLocation(location)) {
			if (s.startsWith("dungeon_")) {
				return s;
			}
		}
		return "";
	}
}
