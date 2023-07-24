package com.rewordmanager;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;

import java.util.HashMap;
import java.util.Map;
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
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;

@Slf4j
@PluginDescriptor(name = "Reword Manager", description = "Manage lists to reword Chat messages, Items, NPCs, Objects", tags = {
		"reword", "word", "rename", "replace", "acronym", "abbreviate", "chat", "message", "npc", "item", "object" })

public class RewordManagerPlugin extends Plugin {
	private final HashMap<String, String> chatListHashMap = new HashMap<>();
	private final HashMap<String, String> npcListHashMap = new HashMap<>();
	private final HashMap<String, String> itemListHashMap = new HashMap<>();
	private final HashMap<String, String> objectListHashMap = new HashMap<>();

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

	@Provides
	RewordManagerConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(RewordManagerConfig.class);
	}

	private void parseConfig() {
		chatListHashMap.clear();
		npcListHashMap.clear();
		itemListHashMap.clear();
		objectListHashMap.clear();

		try {
			parseHashMap(config.chatList(), chatListHashMap);
			parseHashMap(config.npcList(), npcListHashMap);
			parseHashMap(config.itemList(), itemListHashMap);
			parseHashMap(config.objectList(), objectListHashMap);
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

	@Subscribe
	protected void onConfigChanged(ConfigChanged event) {
		parseConfig();
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		String message = chatMessage.getMessage();
		boolean containsKeyword = false;

		for (String keyword : chatListHashMap.keySet()) {
			Pattern pattern = Pattern.compile("\\b" + keyword + "\\b");
			if (pattern.matcher(message).find()) {
				containsKeyword = true;
				break;
			}
		}

		if (!containsKeyword || message.contains("</col>") || message.contains("<br>")) {
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
		String message = overheadText.getOverheadText();
		boolean containsKeyword = false;

		for (String keyword : chatListHashMap.keySet()) {
			Pattern pattern = Pattern.compile("\\b" + keyword + "\\b");
			if (pattern.matcher(message).find()) {
				containsKeyword = true;
				break;
			}
		}

		if (!containsKeyword) {
			return;
		}

		String[] words = message.split(" ");

		String modified_message = "[Modified] ";
		for (String word : words) {
			String modifiedWord = chatListHashMap.getOrDefault(word, word);
			modified_message += modifiedWord + " ";
		}

		System.out.println(modified_message);

		overheadText.getActor().setOverheadText(modified_message);

	}

	private void remapWidgetText(Widget component, String text, HashMap<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (text.equalsIgnoreCase(entry.getKey())) {
				component.setText(text.replace(entry.getKey(), entry.getValue()));
				return;
			}
		}
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

	private void mapWidgetText(Widget[] childComponents) {
		for (Widget component : childComponents) {
			remapWidget(component);

			String text = component.getText();
			if (text.isEmpty()) {
				continue;
			}
			remapWidgetText(component, text, npcListHashMap);
			remapWidgetText(component, text, itemListHashMap);
			remapWidgetText(component, text, objectListHashMap);
		}
	}

	private void remapWidget(Widget widget) {
		final int groupId = WidgetInfo.TO_GROUP(widget.getId());
		final int CHAT_MESSAGE = 162, PRIVATE_MESSAGE = 163, FRIENDS_LIST = 429;

		if (groupId == CHAT_MESSAGE || groupId == PRIVATE_MESSAGE || groupId == FRIENDS_LIST) {
			return;
		}

		Widget[] children = widget.getDynamicChildren();
		if (children == null) {
			return;
		}

		Widget[] childComponents = widget.getDynamicChildren();
		if (childComponents != null) {
			mapWidgetText(childComponents);
		}

		childComponents = widget.getStaticChildren();
		if (childComponents != null) {
			mapWidgetText(childComponents);
		}

		childComponents = widget.getNestedChildren();
		if (childComponents != null) {
			mapWidgetText(childComponents);
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
	}

}
