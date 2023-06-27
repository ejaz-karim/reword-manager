package com.rewordmanager;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class RewordManagerPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(RewordManagerPlugin.class);
		RuneLite.main(args);
	}
}
