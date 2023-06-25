package com.abbreviationmanager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.ScriptID;
import net.runelite.api.VarClientStr;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.vars.InputType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageBuilder;

@Slf4j
@PluginDescriptor(name = "Abbreviation Manager", description = "Abbreviate & Unabbreviate lists of words for chat", tags = {
		"chat", "acronym", "replace", "word" })
public class AbbreviationManagerPlugin extends Plugin {
	private final HashMap<String, String> chatListHashMap = new HashMap<>();
	private final HashMap<String, String> npcListHashMap = new HashMap<>();
	private final HashMap<String, String> itemListHashMap = new HashMap<>();

	private static final Set<MenuAction> NPC_MENU_ACTIONS = ImmutableSet.of(
			MenuAction.NPC_FIRST_OPTION, MenuAction.NPC_SECOND_OPTION,
			MenuAction.NPC_THIRD_OPTION, MenuAction.NPC_FOURTH_OPTION,
			MenuAction.NPC_FIFTH_OPTION, MenuAction.WIDGET_TARGET_ON_NPC,
			MenuAction.EXAMINE_NPC, MenuAction.EXAMINE_OBJECT);

	private static final Set<MenuAction> ITEM_MENU_ACTIONS = ImmutableSet.of(
			MenuAction.GROUND_ITEM_FIRST_OPTION, MenuAction.GROUND_ITEM_SECOND_OPTION,
			MenuAction.GROUND_ITEM_THIRD_OPTION, MenuAction.GROUND_ITEM_FOURTH_OPTION,
			MenuAction.GROUND_ITEM_FIFTH_OPTION, MenuAction.EXAMINE_ITEM_GROUND,
			// Inventory + Using Item on Players/NPCs/Objects
			MenuAction.CC_OP, MenuAction.CC_OP_LOW_PRIORITY, MenuAction.WIDGET_TARGET,
			MenuAction.WIDGET_TARGET_ON_PLAYER, MenuAction.WIDGET_TARGET_ON_NPC,
			MenuAction.WIDGET_TARGET_ON_GAME_OBJECT, MenuAction.WIDGET_TARGET_ON_GROUND_ITEM,
			MenuAction.WIDGET_TARGET_ON_WIDGET);

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

	}

	@Provides
	AbbreviationManagerConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(AbbreviationManagerConfig.class);
	}

	@Inject
	private ClientThread clientThread;

	private void parseConfig() {
		chatListHashMap.clear();
		npcListHashMap.clear();
		itemListHashMap.clear();

		try {
			parseHashMap(config.chatList(), chatListHashMap);
			parseHashMap(config.npcList(), npcListHashMap);
			parseHashMap(config.itemList(), itemListHashMap);
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
		String message = chatMessage.getMessage();

		// Iterate over each entry in the abbreviation map and replace words in the chat
		// message
		for (Map.Entry<String, String> entry : chatListHashMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			// Replace all occurrences of the key with the corresponding value
			message = message.replace(key, value);
		}

		// Log the modified message
		log.info(message);
	}

	@Subscribe
	public void onScriptCallbackEvent(ScriptCallbackEvent event) {

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
		for (Map.Entry<String, String> entry : map.entrySet()) {
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
		}
	}

	// @Subscribe
	// private void onBeforeRender(BeforeRender event) {
	// if (client.getGameState() != GameState.LOGGED_IN)
	// return;

	// for (Widget widgetRoot : client.getWidgetRoots()) {
	// remapWidget(widgetRoot);
	// }
	// }

}
