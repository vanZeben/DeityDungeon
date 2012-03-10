package com.imdeity.deity.dungeon.cmds;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deity.dungeon.objects.Dungeon;
import com.imdeity.deity.dungeon.objects.DungeonSql;
import com.imdeity.deityapi.Deity;
import com.imdeity.deityapi.utils.StringMgmt;

public class DungeonAdminCommand implements CommandExecutor {

	/**
	 * For Testing
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Deity.perm.isLeastSubAdmin(player)) {
				this.parseCmds(player, args);
				return true;
			}
		}
		return false;
	}

	public void parseCmds(Player player, String[] split) {
		if (split.length == 0) {
		} else if (split[0].equalsIgnoreCase("reload")) {
			DeityDungeon.dungeons.clear();
			DeityDungeon.plugin.loadDungeons();
		} else if (split[0].equalsIgnoreCase("add-dungeon")) {
			String dungeon = split[1];
			String world = player.getWorld().getName();
			int bossSpawnInterval = Integer.valueOf(split[2]);
			int deathCountdown = Integer.valueOf(split[3]);
			int mobLifeSpan = Integer.valueOf(split[4]);
			DungeonSql.addDungeon(dungeon, world, bossSpawnInterval, deathCountdown, mobLifeSpan);
			Deity.chat.sendPlayerMessage(player, "DeityDungeon", "Added new dungeon");
		} else if (split[0].equalsIgnoreCase("add-mob")) {
			String dungeon = split[1];
			String mobtype = split[2];
			int amount = Integer.valueOf(split[3]);
			int exp = Integer.valueOf(split[4]);
			int damagesent = Integer.valueOf(split[5]);
			int damagereceived = Integer.valueOf(split[6]);
			Location location = player.getLocation();
			DungeonSql.addDungeonMobSpawn(dungeon, mobtype, damagesent, damagereceived, location, amount, false, exp);
			Deity.chat.sendPlayerMessage(player, "DeityDungeon", "Added new mob");
		} else if (split[0].equalsIgnoreCase("add-boss")) {
			String dungeon = split[1];
			String mobtype = split[2];
			int amount = Integer.valueOf(split[3]);
			int exp = Integer.valueOf(split[4]);
			int damagesent = Integer.valueOf(split[5]);
			int damagereceived = Integer.valueOf(split[6]);
			Location location = player.getLocation();
			DungeonSql.addDungeonMobSpawn(dungeon, mobtype, damagesent, damagereceived, location, amount, true, exp);
			Deity.chat.sendPlayerMessage(player, "DeityDungeon", "Added new boss");
		} else if (split[0].equalsIgnoreCase("add-drops")) {
			int mobId = Integer.valueOf(split[1]);
			int itemId = Integer.valueOf(split[2]);
			int itemDamage = Integer.valueOf(split[3]);
			int itemAmount = Integer.valueOf(split[4]);
			DungeonSql.addDungeonMobDrops(mobId, itemId, itemDamage, itemAmount);
			Deity.chat.sendPlayerMessage(player, "DeityDungeon", "Added new mob drop");
		} else if (split[0].equalsIgnoreCase("info")) {
			String dungeonName = split[1];
			Dungeon dungeon = DeityDungeon.getDungeon(dungeonName);
			StringMgmt mgmt = new StringMgmt();
			Deity.chat.sendPlayerMessageNoTitleNewLine(player, "&3Dungeon Name: &b" + dungeon.regionName);
			Deity.chat.sendPlayerMessageNoTitleNewLine(player, "&3Dungeon Id: &b" + dungeon.id);
			Deity.chat.sendPlayerMessageNoTitleNewLine(player, "&3Dungeon World: &b" + dungeon.world.getName());
			Deity.chat.sendPlayerMessageNoTitleNewLine(player, "&3Is Active: &b" + dungeon.isActive());
			Deity.chat.sendPlayerMessageNoTitleNewLine(player, "&3Player Lifespan: &b" + dungeon.deathCountdown);
			Deity.chat.sendPlayerMessageNoTitleNewLine(player, "&3Mob Lifespan: &b" + dungeon.mobLifeSpan);
			Deity.chat.sendPlayerMessageNoTitleNewLine(player, "&3Boss Spawn Rate: &b" + dungeon.bossSpawnRate);
			Deity.chat.sendPlayerMessageNoTitleNewLine(player, "&3Current Players: &b" + mgmt.join(dungeon.getPlayers(), ", "));
			Deity.chat.sendPlayerMessageNoTitleNewLine(player, "&3Spawners: &b" + mgmt.join(dungeon.getFormattedSpawners(), "<newline>&7-----<newline>"));
		} else if (split[0].equalsIgnoreCase("help")) {
			Deity.chat.sendPlayerMessage(player, "DeityDungeon", "/dungeonadmin add-dungeon <dungeon name> <boss spawn interval> <player lifespan> <mob lifespan>");
			Deity.chat.sendPlayerMessage(player, "DeityDungeon", "/dungeonadmin add-mob <dungeon name> <mob type> <amount> <exp> <damage dealt> <damage received>");
			Deity.chat.sendPlayerMessage(player, "DeityDungeon", "/dungeonadmin add-boss <dungeon name> <mob type> <amount> <exp> <damage dealt> <damage received>");
			Deity.chat.sendPlayerMessage(player, "DeityDungeon", "/dungeonadmin add-drops <mob id> <item id> <item damage> <item amount>");
			Deity.chat.sendPlayerMessage(player, "DeityDungeon", "/dungeonadmin info <dungeon name>");
		}
	}
}
