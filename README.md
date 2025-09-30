# Reword Manager

[![Endpoint Badge](https://img.shields.io/endpoint?url=https%3A%2F%2Fapi.runelite.net%2Fpluginhub%2Fshields%2Finstalls%2Fplugin%2Freword-manager&style=for-the-badge&label=Active%20installs)](https://runelite.net/plugin-hub/ejaz-karim)

A plugin for [RuneLite](https://runelite.net/plugin-hub/ejaz-karim)

This project uses Adoptium Eclipse Temurin 11 as its JDK.

## Introduction

Reword Manager will allow for Chat messages, Item names, Item IDs, Spells, Prayers, Emotes, NPCs, Objects, Options, Player, and Clan names to be modified.

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

## Exceptions and Limitations

Known bug: Rewording player & clan names may not work if the name contains a space.

Messages containing `</col>` or `<br>` will not be modified. E.g. Checking Serpentine helm scales, Casting a line in Barbarian fishing.

Some special characters in the Chat list may not be reworded.

If you are trying to modify a message in the Chat list containing `<`,`>` it will appear as `<lt>`,`<gt>` and may not be reworded.
