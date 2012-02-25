package com.imdeity.deity.dungeon;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.deity.dungeon.cmds.DungeonAdminCommand;
import com.imdeity.deity.dungeon.helpers.DungeonMobListener;
import com.imdeity.deity.dungeon.helpers.DungeonPlayerListener;
import com.imdeity.deity.dungeon.objects.Dungeon;
import com.imdeity.deity.dungeon.objects.DungeonMessaging;
import com.imdeity.deity.dungeon.objects.DungeonSql;

public class DeityDungeon extends JavaPlugin {

	public static DungeonMessaging chat = new DungeonMessaging();
	public static HashMap<String, Dungeon> dungeons = new HashMap<String, Dungeon>();
	public static DeityDungeon plugin;
	private DungeonMobListener mobListener = new DungeonMobListener();
	private DungeonPlayerListener playerListener = new DungeonPlayerListener();

	public void onEnable() {
		DeityDungeon.plugin = this;
		this.registerCmds();
		this.registerEvents();
		DungeonSql.loadDungeonTables();
		this.loadDungeons();
		DeityDungeon.chat.sendConsoleMessage("Enabled");
	}

	public void onDisable() {
		DeityDungeon.chat.sendConsoleMessage("Disabled");
	}

	public void registerEvents() {
		this.getServer().getPluginManager().registerEvents(mobListener, this);
		this.getServer().getPluginManager().registerEvents(playerListener, this);
	}

	public void registerCmds() {
		this.getCommand("DungeonAdmin").setExecutor(new DungeonAdminCommand());
	}

	public void loadDungeons() {
		DeityDungeon.dungeons = DungeonSql.getDungeons();
	}

	public static Dungeon getDungeon(String region) {
		return DeityDungeon.dungeons.get(region);
	}
}
