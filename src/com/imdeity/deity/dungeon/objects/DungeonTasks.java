package com.imdeity.deity.dungeon.objects;

import org.bukkit.entity.Player;

import com.imdeity.deity.dungeon.DeityDungeon;

public class DungeonTasks {
	
	public static class BossSpawner implements Runnable {
		private Spawner spawner;
		private String dungeon = "";

		public BossSpawner(Spawner spawner, String dungeon) {
			this.spawner = spawner;
			this.dungeon = dungeon;
		}

		@Override
		public void run() {
			try {
				while (true) {
					Dungeon dungeon = DeityDungeon.getDungeon(this.dungeon);
					int totalTime = dungeon.bossSpawnRate * 60 * 1000;
					long time = (long) (totalTime * 0.90);

					Thread.sleep(time);
					dungeon.sendMessage("&7A chill runs down your spine...");
					time = (long) (totalTime * 0.10);

					Thread.sleep(time);
					dungeon.sendMessage("&aA shreak pierces your ears as the boss spawns");
					if (dungeon.isActive()) {
						spawner.spawnMobs();
					}
				}
			} catch (Exception ex) {
			}
		}
	}

	public static class PlayerCounter implements Runnable {
		private Player player;
		private String region;

		public PlayerCounter(Player player, String region) {
			this.player = player;
			this.region = region;
		}

		public void run() {
			try {
				Dungeon dungeon = DeityDungeon.getDungeon(this.region);
				DeityDungeon.chat.sendPlayerMessage(this.player, "Times up! You failed to complete your mission.");
				dungeon.removePlayerFailed(player);
			} catch (Exception e) {
			}
		}
	}

	public static class PlayerMessager implements Runnable {

		private Player player;
		private String msg = "";

		public PlayerMessager(Player player, String msg) {
			this.player = player;
			this.msg = msg;
		}

		public void run() {
			try {
				DeityDungeon.chat.sendPlayerMessage(this.player, this.msg);
			} catch (Exception e) {
			}
		}
	}
}
