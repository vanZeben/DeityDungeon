package com.imdeity.deity.dungeon.helpers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deity.dungeon.objects.Dungeon;
import com.imdeity.deityapi.Deity;
import com.imdeity.deityapi.exception.InventoryAlreadySavedException;
import com.imdeity.deityapi.exception.NoInventorySavedException;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.event.MVPortalEvent;

public class DungeonPlayerListener implements Listener {

	// @EventHandler(priority = EventPriority.HIGH)
	// public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
	// if (event.getPlayer() == null) {
	// return;
	// }
	// Player player = event.getPlayer();
	// if (player.getWorld().getName().equalsIgnoreCase("events")) {
	// try {
	// Deity.player.serializedPlayer.savePlayerInventory(player.getName(),
	// Deity.player.serializedPlayer.getPlayerInventory(player),
	// "dungeon-main");
	// DeityDungeon.chat.sendPlayerMessage(player,
	// "&cYour inventory has been saved for when you go back to the main world");
	// } catch (InventoryAlreadySavedException e) {
	// DeityDungeon.chat.sendConsoleMessage("WARNING: Inventory already saved for "
	// + player.getName());
	// DeityDungeon.chat.sendPlayerMessage(player,
	// "&cUnable to save your inventory - Inventory already saved previously");
	// }
	// Deity.player.clearAllInventory(player);
	// try {
	// Deity.player.serializedPlayer.setPlayerInventory(player,
	// Deity.player.serializedPlayer.loadPlayerInventory(player,
	// "dungeon-dungeon"));
	// DeityDungeon.chat.sendPlayerMessage(player,
	// "&cHere is your saved inventory!");
	// } catch (NoInventorySavedException e) {
	// DeityDungeon.chat.sendConsoleMessage("WARNING: No saved inventory found for "
	// + player.getName());
	// DeityDungeon.chat.sendPlayerMessage(player,
	// "&cUnable to restore inventory - No saved inventory found");
	// }
	// }
	// if (event.getFrom().getName().equalsIgnoreCase("events")) {
	// try {
	// Deity.player.serializedPlayer.savePlayerInventory(player.getName(),
	// Deity.player.serializedPlayer.getPlayerInventory(player),
	// "dungeon-dungeon");
	// DeityDungeon.chat.sendPlayerMessage(player,
	// "&cYour inventory has been saved for when you go back to the dungeons world");
	// } catch (InventoryAlreadySavedException e) {
	// DeityDungeon.chat.sendConsoleMessage("WARNING: Inventory already saved for "
	// + player.getName());
	// DeityDungeon.chat.sendPlayerMessage(player,
	// "&cUnable to save your inventory - Inventory already saved previously");
	// }
	// Deity.player.clearAllInventory(player);
	// try {
	// Deity.player.serializedPlayer.setPlayerInventory(player,
	// Deity.player.serializedPlayer.loadPlayerInventory(player,
	// "dungeon-main"));
	// DeityDungeon.chat.sendPlayerMessage(player,
	// "&cHere is your saved inventory!");
	// } catch (NoInventorySavedException e) {
	// DeityDungeon.chat.sendConsoleMessage("WARNING: No saved inventory found for "
	// + player.getName());
	// DeityDungeon.chat.sendPlayerMessage(player,
	// "&cUnable to restore inventory - No saved inventory found");
	// }
	// }
	// }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPortalEvent(MVPortalEvent event) {
		if (event.isCancelled()) {
			return;
		}
		MVPortal portal = event.getSendingPortal();
		if (portal.getName().startsWith("dungeon-send-")) {
			Player player = event.getTeleportee();
			String dungeonName = portal.getName().split("-")[2];
			Dungeon dungeon = DeityDungeon.getDungeon(dungeonName);
			if (!dungeon.hasPlayer(player)) {
				if (!dungeon.isActive()) {
					dungeon.spawnAllMobs();
				}
				dungeon.addPlayer(player);
				DeityDungeon.chat.sendPlayerMessage(player, "You joined " + dungeonName);
			} else {
				DeityDungeon.chat.sendPlayerMessage(player, "&cYou are already in this dungeon!");
				event.setCancelled(true);
				return;
			}
		} else if (portal.getName().startsWith("dungeon-end-")) {
			Player player = event.getTeleportee();
			String dungeonName = portal.getName().split("-")[2];
			Dungeon dungeon = DeityDungeon.getDungeon(dungeonName);
			if (dungeon.hasPlayer(player)) {
				dungeon.removePlayer(player);
				if (!dungeon.isActive()) {
					dungeon.killAllMobs();
				}
				DeityDungeon.chat.sendPlayerMessage(player, "You left " + dungeonName);
			} else {
				DeityDungeon.chat.sendPlayerMessage(player, "&cYou are not in this dungeon!");
				event.setCancelled(true);
				return;
			}
		} else if (portal.getName().equalsIgnoreCase("dungeon_sender")) {
			Player player = event.getTeleportee();
			try {
				Deity.player.serializedPlayer.savePlayerInventory(player.getName(), Deity.player.serializedPlayer.getPlayerInventory(player), "main_world-dungeon");
				Deity.player.serializedPlayer.savePlayerStats(player.getName(), Deity.player.serializedPlayer.getPlayerStats(player), "main_world-dungeon");
				Deity.player.clearAllInventory(player);
				Deity.player.clearAll(player);
				DeityDungeon.chat.sendPlayerMessage(player, "Your inventory was saved for when you go back to the main world");
			} catch (InventoryAlreadySavedException e) {
				e.printStackTrace();
			}
			try {
				Deity.player.serializedPlayer.setPlayerInventory(player, Deity.player.serializedPlayer.loadPlayerInventory(player, "dungeon-main_world"));
				Deity.player.serializedPlayer.setPlayerStats(player, Deity.player.serializedPlayer.loadPlayerStats(player, "dungeon-main_world"));
			} catch (NoInventorySavedException e) {
				e.printStackTrace();
			}
		} else if (portal.getName().equalsIgnoreCase("dungeon_receiver")) {
			Player player = event.getTeleportee();
			try {
				Deity.player.serializedPlayer.savePlayerInventory(player.getName(), Deity.player.serializedPlayer.getPlayerInventory(player), "dungeon-main_world");
				Deity.player.serializedPlayer.savePlayerStats(player.getName(), Deity.player.serializedPlayer.getPlayerStats(player), "dungeon-main_world");
				Deity.player.clearAllInventory(player);
				Deity.player.clearAll(player);
				DeityDungeon.chat.sendPlayerMessage(player, "Your inventory was saved for when you go back to the dungeons");
			} catch (InventoryAlreadySavedException e) {
				e.printStackTrace();
			}
			try {
				Deity.player.serializedPlayer.setPlayerInventory(player, Deity.player.serializedPlayer.loadPlayerInventory(player, "main_world-dungeon"));
				Deity.player.serializedPlayer.setPlayerStats(player, Deity.player.serializedPlayer.loadPlayerStats(player, "main_world-dungeon"));
			} catch (NoInventorySavedException e) {
				e.printStackTrace();
			}
		}
	}
}
