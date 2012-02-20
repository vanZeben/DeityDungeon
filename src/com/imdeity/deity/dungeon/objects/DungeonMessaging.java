package com.imdeity.deity.dungeon.objects;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.Deity;

public class DungeonMessaging {

	public DungeonMessaging() {

	}

	public void sendConsoleMessage(String msg) {
		Deity.chat.out("DeityDungeon", msg);
	}

	public void sendPlayerMessage(Player player, String msg) {
		Deity.chat.sendPlayerMessage(player, "&f" + msg);
	}
	
}
