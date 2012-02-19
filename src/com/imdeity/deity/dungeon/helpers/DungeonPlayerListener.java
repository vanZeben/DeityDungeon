package com.imdeity.deity.dungeon.helpers;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deityapi.Deity;
import com.imdeity.deityapi.cmds.OpenInvPluginCommand;
import com.imdeity.deityapi.exception.InventoryAlreadySavedException;
import com.imdeity.deityapi.exception.NoInventorySavedException;
import com.imdeity.deityapi.utils.PlayerInventoryChest;

public class DungeonPlayerListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		if (event.getPlayer() == null) {
			return;
		}
		Player player = event.getPlayer();
		if (player.getWorld().getName().equalsIgnoreCase("events")) {
			try {
				Deity.player.serializedPlayer.savePlayerInventory(player.getName(), Deity.player.serializedPlayer.getPlayerInventory(player), "Dungeon-main");
				DeityDungeon.chat.sendPlayerMessage(player, "&cYour inventory has been saved for when you go back to the main world");
			} catch (InventoryAlreadySavedException e) {
				DeityDungeon.chat.sendConsoleMessage("WARNING: Inventory already saved for " + player.getName());
				DeityDungeon.chat.sendPlayerMessage(player, "&cUnable to save your inventory - Inventory already saved previously");
			}
			Deity.player.clearAllInventory(player);
			try {
				Deity.player.serializedPlayer.setPlayerInventory(player, Deity.player.serializedPlayer.loadPlayerInventory(player, "Dungeon-dungeon"));
				DeityDungeon.chat.sendPlayerMessage(player, "&cHere is your saved inventory!");
			} catch (NoInventorySavedException e) {
				DeityDungeon.chat.sendConsoleMessage("WARNING: No saved inventory found for " + player.getName());
				DeityDungeon.chat.sendPlayerMessage(player, "&cUnable to restore inventory - No saved inventory found");
			}
		}
		if (event.getFrom().getName().equalsIgnoreCase("events")) {
			try {
				Deity.player.serializedPlayer.savePlayerInventory(player.getName(), Deity.player.serializedPlayer.getPlayerInventory(player), "Dungeon-dungeon");
				DeityDungeon.chat.sendPlayerMessage(player, "&cYour inventory has been saved for when you go back to the dungeons world");
			} catch (InventoryAlreadySavedException e) {
				DeityDungeon.chat.sendConsoleMessage("WARNING: Inventory already saved for " + player.getName());
				DeityDungeon.chat.sendPlayerMessage(player, "&cUnable to save your inventory - Inventory already saved previously");
			}
			Deity.player.clearAllInventory(player);
			try {
				Deity.player.serializedPlayer.setPlayerInventory(player, Deity.player.serializedPlayer.loadPlayerInventory(player, "Dungeon-main"));
				DeityDungeon.chat.sendPlayerMessage(player, "&cHere is your saved inventory!");
			} catch (NoInventorySavedException e) {
				DeityDungeon.chat.sendConsoleMessage("WARNING: No saved inventory found for " + player.getName());
				DeityDungeon.chat.sendPlayerMessage(player, "&cUnable to restore inventory - No saved inventory found");
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		// dealing with saved chests
		if (event.isCancelled()) {
			return;
		}
		boolean Offline = false;
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (block != null) {
			Material blockType = block.getType();
			if (blockType.equals(Material.WALL_SIGN)) {
				Sign sign = (Sign) block.getState();
				if (sign.getLine(1).equalsIgnoreCase("[Dungeon]") && sign.getLine(2).equalsIgnoreCase("[Chest]")) {
					String name = "[" + player.getName() + "]";
					Player target = Deity.server.getServer().getPlayer(name);
					if (target == null) {
						try {
							MinecraftServer server = ((CraftServer) Deity.server.getServer()).getServer();
							EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0), name, new ItemInWorldManager(server.getWorldServer(0)));
							target = entity == null ? null : (Player) entity.getBukkitEntity();
							if (target != null) {
								Offline = true;
								target.loadData();
							} else {
								System.out.println(ChatColor.RED + "Player is null!");
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
					}
					EntityPlayer entityplayer = ((CraftPlayer) player).getHandle();
					EntityPlayer entitytarget = ((CraftPlayer) target).getHandle();
					PlayerInventoryChest inv = new PlayerInventoryChest(entitytarget.inventory, entitytarget);
					inv.Opener = player;
					inv.Target = target;
					if (Offline) {
						inv.Offline = true;
						OpenInvPluginCommand.offlineInv.put(target, inv);
					}
					entityplayer.a(inv);
					return;
				}
			}
		}
	}
}
