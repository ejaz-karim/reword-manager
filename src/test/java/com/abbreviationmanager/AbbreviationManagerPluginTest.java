package com.abbreviationmanager;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class AbbreviationManagerPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(AbbreviationManagerPlugin.class);
		RuneLite.main(args);
	}
}