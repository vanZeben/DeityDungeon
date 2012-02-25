package com.imdeity.deity.dungeon.cmds;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.deity.dungeon.objects.DungeonSql;

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
		} else if (split[0].equalsIgnoreCase("ar")) {
			String dungeon = "dungeon_one";
			String mobtype = split[1];
			int amount = Integer.valueOf(split[2]);
			Location location = player.getLocation();
			DungeonSql.addDungeonMobSpawn(dungeon, mobtype, 10, 10, location, amount, false);
			DungeonSql.addDungeonMobDrops(mobtype, 261, 0, 4);
		} else if (split[0].equalsIgnoreCase("ab")) {
			String dungeon = "dungeon_one";
			String mobtype = split[1];
			int amount = Integer.valueOf(split[2]);
			Location location = player.getLocation();
			DungeonSql.addDungeonMobSpawn(dungeon, mobtype, 10, 10, location, amount, true);
			DungeonSql.addDungeonMobDrops(mobtype, 261, 0, 4);
		}
	}
}
