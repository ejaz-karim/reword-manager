# Reword Manager

[![Endpoint Badge](https://img.shields.io/endpoint?url=https%3A%2F%2Fapi.runelite.net%2Fpluginhub%2Fshields%2Finstalls%2Fplugin%2Freword-manager&label=Active%20Users)](https://runelite.net/plugin-hub/ejaz-karim)

A plugin for [RuneLite](https://runelite.net/plugin-hub/ejaz-karim)

This project uses Adoptium Eclipse Temurin 11 as its JDK.

## Introduction

Reword Manager allows you to rename or modify: Chat messages, Item names, Item IDs, Spells, Prayers, Emotes, NPCs, Objects, Options, Teleport Submenus, Player names, Clan names

## Examples

NPC List

	Wise Old Man,Example
	Vannaka,Example1
 	Bob,Example2
 	Zaff,<col=ff0000>Zaff</col>

Item List (Items, IDs, Spells, Prayers, Emotes)

	Dragon scimitar,Example
	Fire Strike,Example1
	Smite,Example2
	Goblin Salute,Example3
	22798,Bird nest (seeds)
	22800,Bird nest (Wyson)
	5070,Bird nest (Red egg)
	5071,Bird nest (Green egg)
	5072,Bird nest (Blue egg)
	5074,Bird nest (ring)
	5075,Bird nest (empty)
	5076,Bird's egg (Red)
	5077,Bird's egg (Blue)
	5078,Bird's egg (Green)

Entering data in the lists must be in the CSV format ***x,y*** and is case-sensitive. You **MUST** use a new line for new entries. It is possible to recolour the names of NPCs, Items, etc. with HTML Hexadecimal colour codes. You can find item IDs at: https://oldschool.runescape.wiki/w/Item_IDs

## Limitations

- Known bug: Rewording player & clan names may not work if the name contains a space.

- Messages containing `</col>` or `<br>` will not be modified. E.g. Checking Serpentine helm scales, Casting a line in Barbarian fishing.

- Certain special characters cannot be reworded. For example, `*` cannot, whereas `!` and `:` can.

- If you are trying to modify a message in the Chat list containing `<`,`>` it will appear as `<lt>`,`<gt>` and may not be reworded.

- If you reword the option `Examine`, it will cause Menu Entry Swapper to not show the Swap options when you shift-right-click an item. This is because Menu Entry Swapper is hardcoded to look for the `Examine` option in order to show the Swap options. A workaround is to temporarily turn off Reword Manager before you change any Swap options.
