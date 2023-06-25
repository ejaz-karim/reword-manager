package com.abbreviationmanager;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("abbreviationmanager")
public interface AbbreviationManagerConfig extends Config {
	@ConfigSection(name = "Abbreviation Lists", description = "Manage the lists", position = 0)
	String abbreviationLists = "abbreviationLists";

	@ConfigItem(keyName = "chatList", name = "Chat List", description = "For changing chat messages\nCSV format x,y\nNew line for new entries", position = 1, section = abbreviationLists)
	default String chatList() {
		return "Msb,Magic";
	}

	@ConfigItem(keyName = "npcList", name = "NPC List", description = "For changing NPC names\nCSV format x,y\nNew line for new entries", position = 2, section = abbreviationLists)
	default String npcList() {
		return "";
	}

	@ConfigItem(keyName = "itemList", name = "Item List", description = "For changing item names\nCSV format x,y\nNew line for new entries", position = 3, section = abbreviationLists)
	default String itemList() {
		return "";
	}

	//object list

}
