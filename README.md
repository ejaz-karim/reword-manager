# Reword Manager

[![](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/installs/plugin/reword-manager)](https://runelite.net/plugin-hub/ejaz-karim) [![](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/rank/plugin/reword-manager)](https://runelite.net/plugin-hub/ejaz-karim) [![](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/rank/author/ejaz-karim)](https://runelite.net/plugin-hub/ejaz-karim)

A plugin for [RuneLite](https://runelite.net/plugin-hub/ejaz-karim)

This project uses Adoptium Eclipse Temurin 11 as its JDK.

## Introduction

Reword Manager will allow for Chat messages, Items, NPCs, and Objects to be reworded.

## Examples

NPC List

	Wise Old Man,Example
	Vannaka,Example1

Entering data in the lists must be in the CSV format ***x,y*** and is case-sensitive. You must use a new line for new entries.

## Exceptions

Avoid using special characters to remap in the Chat list.

Game messages containing `</col>` or `<br>` will not be modified. E.g. Checking Serpentine helm scales, Casting a line in Barbarian fishing.

If your modified message contains `<`,`>` and you haven't used it in a replacement word, it will appear as

	<gt> 
	<lt>
