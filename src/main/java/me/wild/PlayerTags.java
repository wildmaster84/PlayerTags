package me.wild;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.wild.commands.TagsCommand;
import me.wild.events.CoreEvents;
import me.wild.events.TagsEvents;
import me.wild.menus.TagsMenu;

public class PlayerTags extends JavaPlugin {
	private static PlayerTags instance;
	private static ConfigManager config;
	private static YamlConfiguration tags;
	private static YamlConfiguration users;
	private TagsMenu playerTags;
	public HashMap<Player, Inventory> tagsInv = new HashMap<>();
	public HashMap<Player, Integer> tagsPage = new HashMap<>();
	public HashMap<String, String> currentTags = new HashMap<>();

	public void onEnable() {
		instance = this;
		reloadConfigs();
		getCommand("tags").setExecutor(new TagsCommand());
		getServer().getPluginManager().registerEvents(new CoreEvents(), this);
		getServer().getPluginManager().registerEvents(new TagsEvents(), this);
		playerTags = new TagsMenu();
		
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPIHook().register();
        }
	}
	
	private void reloadConfigs() {
		if (config == null) {
			config = new ConfigManager();
		}
		config.getConfig("tags.yml");
		config.getConfig("data.yml");
		tags = YamlConfiguration.loadConfiguration(config.getConfig("tags.yml"));
		users = YamlConfiguration.loadConfiguration(config.getConfig("data.yml"));
		for (String user: users.getKeys(false)) {
			currentTags.put(user, users.getString(user + ".tag"));
		}
	}


	public static PlayerTags getInstance() {
		return instance;
	}
	
	public ConfigManager getConfigManager() {
		return config;
	}
	
	public YamlConfiguration getTags() {
		return tags;
	}
	public YamlConfiguration getUserData() {
		return users;
	}
	public TagsMenu getTagsMenu() {
		return playerTags;
	}
}
