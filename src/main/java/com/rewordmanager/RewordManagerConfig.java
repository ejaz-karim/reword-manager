package com.rewordmanager;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("rewordmanager")
public interface RewordManagerConfig extends Config {
	@ConfigSection(name = "Reword Lists", description = "Section for managing lists", position = 0)
	String rewordLists = "rewordLists";

	@ConfigItem(keyName = "chatList", name = "Chat List", description = "CSV format x,y - case-sensitive - New line for new entries. For rewording chat messages", position = 1, section = rewordLists)
	default String chatList() {
		return "Example,Example1";
	}

	@ConfigItem(keyName = "npcList", name = "NPC List", description = "CSV format x,y - case-sensitive - New line for new entries. For rewording NPCs", position = 2, section = rewordLists)
	default String npcList() {
		return "";
	}

	@ConfigItem(keyName = "itemList", name = "Item List", description = "CSV format x,y - case-sensitive - New line for new entries. For rewording items", position = 3, section = rewordLists)
	default String itemList() {
		return "";
	}

	//object list
	@ConfigItem(keyName = "objectList", name = "Object List", description = "CSV format x,y - case-sensitive - New line for new entries. For rewording objects", position = 4, section = rewordLists)
	default String objectList() {
		return "";
	}

}
