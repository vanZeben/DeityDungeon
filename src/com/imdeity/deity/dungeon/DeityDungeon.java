package com.imdeity.deity.dungeon;

import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.deity.dungeon.cmds.DungeonAdminCommand;
import com.imdeity.deity.dungeon.helpers.DungeonMobListener;
import com.imdeity.deity.dungeon.helpers.DungeonPlayerListener;
import com.imdeity.deity.dungeon.objects.Dungeon;
import com.imdeity.deity.dungeon.objects.DungeonMessaging;
import com.imdeity.deity.dungeon.objects.DungeonSettings;
import com.imdeity.deity.dungeon.objects.DungeonStorage;

public class DeityDungeon extends JavaPlugin {

	public static DungeonMessaging chat = new DungeonMessaging();
	public static DungeonSettings settings = new DungeonSettings();
	private DungeonMobListener mobListener = new DungeonMobListener();
	private DungeonPlayerListener playerListener = new DungeonPlayerListener();

	public void onEnable() {
		DeityDungeon.settings.loadDefaults();
		this.getServer().getPluginManager().registerEvents(mobListener, this);
		this.getServer().getPluginManager().registerEvents(playerListener, this);
		this.getCommand("DungeonAdmin").setExecutor(new DungeonAdminCommand());
		loadDungeonBoss();
		DeityDungeon.chat.sendConsoleMessage("Enabled");
	}

	public void onDisable() {
		DeityDungeon.chat.sendConsoleMessage("Disabled");
	}
	
	public void loadDungeonBoss() {
		for (Dungeon d: DungeonStorage.getDungeons()) {
			d.spawnBoss();
		}
	}
}
