package com.abbreviationmanager;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

@ConfigGroup("abbreviationmanager")
public interface AbbreviationManagerConfig extends Config {
	@ConfigSection(name = "Abbreviation Lists", description = "Manage the lists", position = 0)
	String abbreviationLists = "abbreviationLists";

	@ConfigItem(keyName = "list1", name = "List 1 of abbreviations", description = "CSV format x,y new line for new entries", position = 1, section = abbreviationLists)
	default String list1() {
		return "Msb,Magic";
	}

}
