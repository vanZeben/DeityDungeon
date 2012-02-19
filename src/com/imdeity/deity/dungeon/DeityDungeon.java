package com.imdeity.deity.dungeon;

import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.deity.dungeon.cmds.DungeonAdminCommand;
import com.imdeity.deity.dungeon.helpers.DungeonMobsListener;
import com.imdeity.deity.dungeon.objects.DungeonMessaging;
import com.imdeity.deity.dungeon.objects.DungeonSettings;

public class DeityDungeon extends JavaPlugin {

	public static DungeonMessaging chat = new DungeonMessaging();
	public static DungeonSettings settings = new DungeonSettings();
	private DungeonMobsListener mobListener = new DungeonMobsListener();

	public void onEnable() {
		DeityDungeon.settings.loadDefaults();
		this.getServer().getPluginManager().registerEvents(mobListener, this);
		this.getCommand("DungeonAdmin").setExecutor(new DungeonAdminCommand());
		DeityDungeon.chat.sendConsoleMessage("Enabled");
	}

	public void onDisable() {
		DeityDungeon.chat.sendConsoleMessage("Disabled");
	}
}
