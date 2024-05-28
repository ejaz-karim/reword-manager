# Reword Manager

[![](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/installs/plugin/reword-manager&label=Active%20installs)](https://runelite.net/plugin-hub/ejaz-karim) [![](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/rank/plugin/reword-manager)](https://runelite.net/plugin-hub/ejaz-karim) [![](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/rank/author/ejaz-karim)](https://runelite.net/plugin-hub/ejaz-karim)

A plugin for [RuneLite](https://runelite.net/plugin-hub/ejaz-karim)

This project uses Adoptium Eclipse Temurin 11 as its JDK.

## Introduction

Reword Manager will allow for Chat messages, Items, NPCs, Objects, Options, Player and Clan names to be reworded.

## Examples

NPC List

	Wise Old Man,Example
	Vannaka,Example1
 	Bob,Example2
 	Zaff,<col=ff0000>Zaff</col>

Entering data in the lists must be in the CSV format ***x,y*** and is case-sensitive. You **MUST** use a new line for new entries. It is possible to recolour the names of NPCs, Items, etc. with HTML Hexadecimal colour codes.

## Exceptions

Known bug: Rewording player & clan names may not work if the name contains a space. Rewording player names may not work if the player is an ironman.

Messages containing `</col>` or `<br>` will not be modified. E.g. Checking Serpentine helm scales, Casting a line in Barbarian fishing.

Some special characters in the Chat list may not be reworded.

If you are trying to modify a message in the Chat list containing `<`,`>` it will appear as `<lt>`,`<gt>` and may not be reworded.
