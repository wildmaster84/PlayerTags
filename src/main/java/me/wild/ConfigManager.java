package me.wild;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
	PlayerTags plugin = PlayerTags.getInstance();
	private File configFile;
    public File getConfig(String name) {
    	configFile = new File(plugin.getDataFolder(), name);

        if (!configFile.exists()) {
            plugin.saveResource(name, false);
        }
        return configFile;
    	
    }

}
