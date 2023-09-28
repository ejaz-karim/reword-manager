package com.rewordmanager;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("rewordmanager")
public interface RewordManagerConfig extends Config {
	@ConfigSection(name = "Reword Lists", description = "Section for managing lists", position = 0)
	String rewordLists = "rewordLists";

	@ConfigItem(keyName = "chatList", name = "Chat List", description = "Use CSV format: x,y (case-sensitive). Each entry uses a new line.<br>For rewording chat messages.", position = 1, section = rewordLists)
	default String chatList() {
		return "Example0,Example1\nExample2,Example3";
	}

	@ConfigItem(keyName = "npcList", name = "NPC List", description = "Use CSV format: x,y (case-sensitive). Each entry uses a new line.<br>For rewording NPCs.", position = 2, section = rewordLists)
	default String npcList() {
		return "";
	}

	@ConfigItem(keyName = "itemList", name = "Item List (Items, Spells, Prayers, etc.)", description = "Use CSV format: x,y (case-sensitive). Each entry uses a new line.<br>For rewording anything considered an Item including: Items, Spells, Prayers, Emotes, etc.", position = 3, section = rewordLists)
	default String itemList() {
		return "";
	}

	@ConfigItem(keyName = "objectList", name = "Object List", description = "Use CSV format: x,y (case-sensitive). Each entry uses a new line.<br>For rewording objects.", position = 4, section = rewordLists)
	default String objectList() {
		return "";
	}

	@ConfigItem(keyName = "optionList", name = "Option List", description = "Use CSV format: x,y (case-sensitive). Each entry uses a new line.<br>For rewording options.", position = 5, section = rewordLists)
	default String optionList() {
		return "";
	}

	@ConfigItem(keyName = "overheadText", name = "Overhead Text", description = "", position = 6)
	default boolean overheadText() {
		return true;
	}
}
