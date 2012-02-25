package com.imdeity.deity.dungeon.helpers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deity.dungeon.objects.Dungeon;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.event.MVPortalEvent;

public class DungeonPlayerListener implements Listener {

	/*
	 * @EventHandler(priority = EventPriority.HIGH) public void
	 * onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) { if
	 * (event.getPlayer() == null) { return; } Player player =
	 * event.getPlayer(); if
	 * (player.getWorld().getName().equalsIgnoreCase("events")) { try {
	 * Deity.player.serializedPlayer.savePlayerInventory(player.getName(),
	 * Deity.player.serializedPlayer.getPlayerInventory(player),
	 * "Dungeon-main"); DeityDungeon.chat.sendPlayerMessage(player,
	 * "&cYour inventory has been saved for when you go back to the main world"
	 * ); } catch (InventoryAlreadySavedException e) {
	 * DeityDungeon.chat.sendConsoleMessage
	 * ("WARNING: Inventory already saved for " + player.getName());
	 * DeityDungeon.chat.sendPlayerMessage(player,
	 * "&cUnable to save your inventory - Inventory already saved previously");
	 * } Deity.player.clearAllInventory(player); try {
	 * Deity.player.serializedPlayer.setPlayerInventory(player,
	 * Deity.player.serializedPlayer.loadPlayerInventory(player,
	 * "Dungeon-dungeon")); DeityDungeon.chat.sendPlayerMessage(player,
	 * "&cHere is your saved inventory!"); } catch (NoInventorySavedException e)
	 * {
	 * DeityDungeon.chat.sendConsoleMessage("WARNING: No saved inventory found for "
	 * + player.getName()); DeityDungeon.chat.sendPlayerMessage(player,
	 * "&cUnable to restore inventory - No saved inventory found"); } } if
	 * (event.getFrom().getName().equalsIgnoreCase("events")) { try {
	 * Deity.player.serializedPlayer.savePlayerInventory(player.getName(),
	 * Deity.player.serializedPlayer.getPlayerInventory(player),
	 * "Dungeon-dungeon"); DeityDungeon.chat.sendPlayerMessage(player,
	 * "&cYour inventory has been saved for when you go back to the dungeons world"
	 * ); } catch (InventoryAlreadySavedException e) {
	 * DeityDungeon.chat.sendConsoleMessage
	 * ("WARNING: Inventory already saved for " + player.getName());
	 * DeityDungeon.chat.sendPlayerMessage(player,
	 * "&cUnable to save your inventory - Inventory already saved previously");
	 * } Deity.player.clearAllInventory(player); try {
	 * Deity.player.serializedPlayer.setPlayerInventory(player,
	 * Deity.player.serializedPlayer.loadPlayerInventory(player,
	 * "Dungeon-main")); DeityDungeon.chat.sendPlayerMessage(player,
	 * "&cHere is your saved inventory!"); } catch (NoInventorySavedException e)
	 * {
	 * DeityDungeon.chat.sendConsoleMessage("WARNING: No saved inventory found for "
	 * + player.getName()); DeityDungeon.chat.sendPlayerMessage(player,
	 * "&cUnable to restore inventory - No saved inventory found"); } } }
	 * 
	 * @EventHandler(priority = EventPriority.NORMAL) public void
	 * onPlayerInteract(PlayerInteractEvent event) { // dealing with saved
	 * chests if (event.isCancelled()) { return; } boolean Offline = false;
	 * Player player = event.getPlayer(); Block block = event.getClickedBlock();
	 * if (block != null) { Material blockType = block.getType(); if
	 * (blockType.equals(Material.WALL_SIGN)) { Sign sign = (Sign)
	 * block.getState(); if (sign.getLine(1).equalsIgnoreCase("[Dungeon]") &&
	 * sign.getLine(2).equalsIgnoreCase("[Chest]")) { String name = "[" +
	 * player.getName() + "]"; Player target =
	 * Deity.server.getServer().getPlayer(name); if (target == null) { try {
	 * MinecraftServer server = ((CraftServer)
	 * Deity.server.getServer()).getServer(); EntityPlayer entity = new
	 * EntityPlayer(server, server.getWorldServer(0), name, new
	 * ItemInWorldManager(server.getWorldServer(0))); target = entity == null ?
	 * null : (Player) entity.getBukkitEntity(); if (target != null) { Offline =
	 * true; target.loadData(); } else { System.out.println(ChatColor.RED +
	 * "Player is null!"); return; } } catch (Exception e) {
	 * e.printStackTrace(); return; } } EntityPlayer entityplayer =
	 * ((CraftPlayer) player).getHandle(); EntityPlayer entitytarget =
	 * ((CraftPlayer) target).getHandle(); PlayerInventoryChest inv = new
	 * PlayerInventoryChest(entitytarget.inventory, entitytarget); inv.Opener =
	 * player; inv.Target = target; if (Offline) { inv.Offline = true;
	 * OpenInvPluginCommand.offlineInv.put(target, inv); } entityplayer.a(inv);
	 * return; } } } }
	 */

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
			if (!dungeon.hasPlayer(player.getName())) {
				dungeon.addPlayer(player);
				dungeon.spawnAllMobs();
			} else {
				DeityDungeon.chat.sendPlayerMessage(player, "&cYou are already in this dungeon!");
				event.setCancelled(true);
				return;
			}
		} else if (portal.getName().startsWith("dungeon-receive-")) {
			Player player = event.getTeleportee();
			String dungeonName = portal.getName().split("-")[2];
			Dungeon dungeon = DeityDungeon.getDungeon(dungeonName);
			if (dungeon.hasPlayer(player.getName())) {
				dungeon.removePlayer(player);
			} else {
				DeityDungeon.chat.sendPlayerMessage(player, "&cYou are not in this dungeon!");
				event.setCancelled(true);
				return;
			}
		}
	}
}
