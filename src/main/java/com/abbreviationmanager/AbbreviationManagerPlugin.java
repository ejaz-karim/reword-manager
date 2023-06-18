package com.abbreviationmanager;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Abbreviation Manager",
	description = "Abbreviate & Unabbreviate lists of words for chat",
	tags = {"chat", "acronym", "replace", "word"}
)
public class AbbreviationManagerPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private AbbreviationManagerConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Abbreviation Manager started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Abbreviation Manager stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Abbreviation Manager says " + config.list1(), null);
		}
	}

	@Provides
	AbbreviationManagerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AbbreviationManagerConfig.class);
	}
}
