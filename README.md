# Reword Manager

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

The chat list cannot remap words containing `<`,`>`. However the replacement word can contain `<`,`>`

If your modified message contains `<`,`>` and you haven't used it as a replacement word, it will appear as

	<gt> 
	<lt>
