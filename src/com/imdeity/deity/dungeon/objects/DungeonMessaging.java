package com.imdeity.deity.dungeon.objects;

import com.imdeity.deityapi.Deity;

public class DungeonMessaging {

	public DungeonMessaging() {
		
	}
	
	public void sendConsoleMessage(String msg) {
		Deity.chat.out("DeityDungeon", msg);
	}
}
