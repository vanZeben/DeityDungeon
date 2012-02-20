package com.imdeity.deity.dungeon.cmds;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.deity.dungeon.DeityDungeon;

public class DungeonAdminCommand implements CommandExecutor {

	/**
	 * For Testing
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			this.parseCmds(player, args);
		}
		return true;
	}

	public void parseCmds(Player player, String[] split) {
		if (split.length == 0) {
			// } else if (split[0].equalsIgnoreCase("spawn") && split.length ==
			// 2) {
			// String name = split[1];
			// DungeonMobs.schedualMobSpawn(name);
		} else if (split[0].equalsIgnoreCase("add-spawn")) {
			String region = "dungeon_one";
			String name = split[1];
			String mobtype = split[2];
			int amount = Integer.valueOf(split[3]);
			Location location = player.getLocation();
			DeityDungeon.settings.setSpawn(region, name, mobtype, amount, location);
		}
	}
}
