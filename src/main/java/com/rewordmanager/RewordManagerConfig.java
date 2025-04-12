package com.rewordmanager;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("rewordmanager")
public interface RewordManagerConfig extends Config {
	@ConfigSection(name = "Reword Lists", description = "Section for managing lists", position = 0)
	String rewordLists = "rewordLists";

	@ConfigItem(keyName = "chatList", name = "Chat List", description = "Use CSV format: x,y (case-sensitive).<br>Each entry uses a new line.<br>For rewording chat messages.", position = 1, section = rewordLists)
	default String chatList() {
		return "Example,Example1\nExample1,Example2\nExample2,Example3";
	}

	@ConfigItem(keyName = "npcList", name = "NPC List", description = "Use CSV format: x,y (case-sensitive).<br>Each entry uses a new line.<br>For rewording NPCs.", position = 2, section = rewordLists)
	default String npcList() {
		return "";
	}

	@ConfigItem(keyName = "itemList", name = "Item List (Items/IDs, Spells, Prayers)", description = "Use CSV format: x,y (case-sensitive).<br>Each entry uses a new line.<br>For rewording anything considered an Item including: Items, Spells, Prayers, Emotes, etc.", position = 3, section = rewordLists)
	default String itemList() {
		return "";
	}

	@ConfigItem(keyName = "objectList", name = "Object List", description = "Use CSV format: x,y (case-sensitive).<br>Each entry uses a new line.<br>For rewording objects.", position = 4, section = rewordLists)
	default String objectList() {
		return "";
	}

	@ConfigItem(keyName = "optionList", name = "Option List", description = "Use CSV format: x,y (case-sensitive).<br>Each entry uses a new line.<br>For rewording options.", position = 5, section = rewordLists)
	default String optionList() {
		return "";
	}

	@ConfigItem(keyName = "playerList", name = "Player List", description = "Use CSV format: x,y (case-sensitive).<br>Each entry uses a new line.<br>For rewording Player names.", position = 6, section = rewordLists)
	default String playerList() {
		return "";
	}

	@ConfigItem(keyName = "clanList", name = "Clan List", description = "Use CSV format: x,y (case-sensitive).<br>Each entry uses a new line.<br>For rewording Clan and Chat-channel names.", position = 7, section = rewordLists)
	default String clanList() {
		return "";
	}

	@ConfigItem(keyName = "overheadText", name = "Overhead Text", description = "Toggle to enable modifications to the overhead text", position = 8)
	default boolean overheadText() {
		return true;
	}

	@ConfigItem(keyName = "altNaming", name = "Alternative Naming Method", description = "Toggle to have an alternative naming method, which preserves ranks in clan chats", position = 9)
	default boolean altNaming() {
		return false;
	}

}
