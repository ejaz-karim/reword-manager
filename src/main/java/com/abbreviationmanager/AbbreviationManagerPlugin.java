package com.abbreviationmanager;

import com.google.inject.Provides;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ScriptID;
import net.runelite.api.VarClientStr;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.vars.InputType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyListener;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.event.KeyEvent;

import javax.inject.Singleton;

import net.runelite.api.VarClientInt;

import net.runelite.client.callback.ClientThread;

import java.lang.reflect.Field;

@Slf4j
@PluginDescriptor(name = "Abbreviation Manager", description = "Abbreviate & Unabbreviate lists of words for chat", tags = {
		"chat", "acronym", "replace", "word" })
public class AbbreviationManagerPlugin extends Plugin {
	private final HashMap<String, String> list1Map = new HashMap<>();

	@Inject
	private Client client;

	@Inject
	private AbbreviationManagerConfig config;

	@Override
	protected void startUp() throws Exception {
		log.info("Abbreviation Manager started!");
		parseConfig();
	}

	@Override
	protected void shutDown() throws Exception {
		log.info("Abbreviation Manager stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Abbreviation Manager says " + config.list1(), null);
		}
	}

	@Provides
	AbbreviationManagerConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(AbbreviationManagerConfig.class);
	}

	@Inject
	private ClientThread clientThread;

	private void parseConfig() {
		list1Map.clear();

		try {
			String list = config.list1();
			if (list.isEmpty())
				return;

			String[] pairs = list.split("\n");
			for (String pair : pairs) {
				String[] kv = pair.split(",");
				if (kv.length != 2)
					continue;
				list1Map.put(kv[0], kv[1]);
				// print the contents pair of list1Map
				// for (String key : list1Map.keySet()) {
				// System.out.println("key: " + key + " value: " + list1Map.get(key));
				// }

			}
		} catch (Exception ignored) {
		}
	}

	@Subscribe
	protected void onConfigChanged(ConfigChanged event) {
		if (!event.getGroup().equals("abbreviationmanager"))
			return;

		parseConfig();
	}

	private void applyText(int inputType, String replacement) {
		if (inputType == InputType.NONE.getType()) {
			client.setVarcStrValue(VarClientStr.CHATBOX_TYPED_TEXT, replacement);
			client.runScript(ScriptID.CHAT_PROMPT_INIT);
		} else if (inputType == InputType.PRIVATE_MESSAGE.getType()) {
			client.setVarcStrValue(VarClientStr.INPUT_TEXT, replacement);
			client.runScript(ScriptID.CHAT_TEXT_INPUT_REBUILD, "");
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		log.info("Chat message logged!");

		if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE) {
			return;
		}

		String message = chatMessage.getMessage();

		// Perform word replacement
		for (Map.Entry<String, String> entry : list1Map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			message = message.replaceAll("(?i)\\b" + key + "\\b", value);
		}

		chatMessage.setMessage(message);
	}

}
