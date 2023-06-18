package com.unabbreviator;

import com.unabbreviator.UnabbreviatorPlugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class UnabbreviatorPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(UnabbreviatorPlugin.class);
		RuneLite.main(args);
	}
}