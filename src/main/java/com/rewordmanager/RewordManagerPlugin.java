package com.rewordmanager;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.MessageNode;
import net.runelite.api.NPC;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;

@Slf4j
@PluginDescriptor(name = "Reword Manager", description = "Reword Chat messages, Items, NPCs, Objects, Options", tags = {
		"reword", "word", "text", "rename", "replace", "acronym", "abbreviate", "chat", "message", "npc", "item",
		"object", "option" })

public class RewordManagerPlugin extends Plugin {
	private final HashMap<String, String> chatListHashMap = new HashMap<>();
	private final HashMap<String, String> npcListHashMap = new HashMap<>();
	private final HashMap<String, String> itemListHashMap = new HashMap<>();
	private final HashMap<String, String> objectListHashMap = new HashMap<>();
	private final HashMap<String, String> optionListHashMap = new HashMap<>();

	private static final Set<MenuAction> NPC_MENU_ACTIONS = ImmutableSet.of(
			MenuAction.NPC_FIRST_OPTION,
			MenuAction.NPC_SECOND_OPTION,
			MenuAction.NPC_THIRD_OPTION,
			MenuAction.NPC_FOURTH_OPTION,
			MenuAction.NPC_FIFTH_OPTION,
			MenuAction.WIDGET_TARGET_ON_NPC,
			MenuAction.EXAMINE_NPC);

	private static final Set<MenuAction> ITEM_MENU_ACTIONS = ImmutableSet.of(
			MenuAction.GROUND_ITEM_FIRST_OPTION,
			MenuAction.GROUND_ITEM_SECOND_OPTION,
			MenuAction.GROUND_ITEM_THIRD_OPTION,
			MenuAction.GROUND_ITEM_FOURTH_OPTION,
			MenuAction.GROUND_ITEM_FIFTH_OPTION,
			MenuAction.EXAMINE_ITEM_GROUND,
			MenuAction.WIDGET_TARGET_ON_GROUND_ITEM,
			MenuAction.CC_OP,
			MenuAction.CC_OP_LOW_PRIORITY,
			MenuAction.WIDGET_TARGET);

	private static final Set<MenuAction> OBJECT_MENU_ACTIONS = ImmutableSet.of(
			MenuAction.GAME_OBJECT_FIRST_OPTION,
			MenuAction.GAME_OBJECT_SECOND_OPTION,
			MenuAction.GAME_OBJECT_THIRD_OPTION,
			MenuAction.GAME_OBJECT_FOURTH_OPTION,
			MenuAction.GAME_OBJECT_FIFTH_OPTION,
			MenuAction.WIDGET_TARGET_ON_GAME_OBJECT,
			MenuAction.EXAMINE_OBJECT);

	@Inject
	private Client client;

	@Inject
	private RewordManagerConfig config;

	@Override
	protected void startUp() throws Exception {
		parseConfig();
	}

	@Override
	protected void shutDown() throws Exception {
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
	}

	@Subscribe
	protected void onConfigChanged(ConfigChanged event) {
		parseConfig();
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		String message = chatMessage.getMessage();
		if (!checkMessage(message)) {
			return;
		}
		final ChatMessageBuilder builder = new ChatMessageBuilder();
		builder.append(ChatColorType.HIGHLIGHT).append("[Modified] ");

		String[] words = message.split(" ");
		for (String word : words) {
			String modifiedWord = chatListHashMap.getOrDefault(word, word);
			builder.append(ChatColorType.NORMAL).append(modifiedWord).append(" ");
		}
		String response = builder.build();
		MessageNode messageNode = chatMessage.getMessageNode();
		messageNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}

	@Subscribe
	public void onOverheadTextChanged(OverheadTextChanged overheadText) {
		if (config.overheadText()) {
			String message = overheadText.getOverheadText();
			if (!checkMessage(message)) {
				return;
			}
			String[] words = message.split(" ");
			String modified_message = "[Modified] ";
			for (String word : words) {
				String modifiedWord = chatListHashMap.getOrDefault(word, word);
				modified_message += modifiedWord + " ";
			}
			overheadText.getActor().setOverheadText(modified_message);
		} else {
			return;
		}
	}

	@Subscribe
	protected void onMenuEntryAdded(MenuEntryAdded event) {
		MenuEntry entry = event.getMenuEntry();
		if (NPC_MENU_ACTIONS.contains(entry.getType())) {
			remapMenuEntryText(entry, npcListHashMap);
		} else if (ITEM_MENU_ACTIONS.contains(entry.getType())) {
			remapMenuEntryText(entry, itemListHashMap);
		} else if (OBJECT_MENU_ACTIONS.contains(entry.getType())) {
			remapMenuEntryText(entry, objectListHashMap);
		}
		remapOptionText(entry);
	}

	@Provides
	RewordManagerConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(RewordManagerConfig.class);
	}

	private void parseConfig() {
		chatListHashMap.clear();
		npcListHashMap.clear();
		itemListHashMap.clear();
		objectListHashMap.clear();
		optionListHashMap.clear();
		try {
			parseHashMap(config.chatList(), chatListHashMap);
			parseHashMap(config.npcList(), npcListHashMap);
			parseHashMap(config.itemList(), itemListHashMap);
			parseHashMap(config.objectList(), objectListHashMap);
			parseHashMap(config.optionList(), optionListHashMap);
		} catch (Exception ignored) {
		}
	}

	private void parseHashMap(String csv, HashMap<String, String> hashMap) {
		if (csv.isEmpty())
			return;
		String[] pairs = csv.split("\n");
		for (String pair : pairs) {
			String[] kv = pair.split(",");
			if (kv.length != 2)
				continue;
			hashMap.put(kv[0], kv[1]);
		}
	}

	private boolean checkMessage(String message) {
		if (message.contains("</col>") || message.contains("<br>")) {
			return false;
		}
		for (String keyword : chatListHashMap.keySet()) {
			Pattern pattern = Pattern.compile("(?<!\\p{Punct})\\b" + keyword + "\\b(?!\\p{Punct})");
			if (pattern.matcher(message).find()) {
				return true;
			}
		}
		return false;
	}

	private void remapMenuEntryText(MenuEntry menuEntry, HashMap<String, String> map) {
		String target = menuEntry.getTarget();
		NPC npc = menuEntry.getNpc();
		String cleanTarget = null;
		if (npc != null) {
			cleanTarget = Text.removeTags(npc.getName());
		} else {
			cleanTarget = Text.removeTags(target);
		}
		for (HashMap.Entry<String, String> entry : map.entrySet()) {
			if (cleanTarget.equals(entry.getKey())) {
				menuEntry.setTarget(target.replace(entry.getKey(), entry.getValue()));
			}
		}
	}

	private void remapOptionText(MenuEntry event) {
		if (optionListHashMap.containsKey(event.getOption())) {
			event.setOption(optionListHashMap.get(event.getOption()));
		}
	}
}
