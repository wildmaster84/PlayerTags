package me.wild;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wild.menus.TagsMenu;
import net.md_5.bungee.api.ChatColor;


public class PAPIHook extends PlaceholderExpansion {    
    // Getting our main class with the stuff here
    private PlayerTags plugin;
    
    public PAPIHook() {
        super();
        this.plugin = PlayerTags.getInstance();
    }
    
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // Placeholder: %playertags_tag%
        if(identifier.equals("tag")) {
            Pattern pattern = Pattern.compile("(#\\w{6})");
            Matcher matcher = pattern.matcher(plugin.currentTags.get(player.getUniqueId().toString()).replaceAll("\\[|\\]", ""));

            StringBuffer buffer = new StringBuffer();
            
            while (matcher.find()) {
              matcher.appendReplacement(buffer, ChatColor.of(matcher.group()).toString());
            }

            return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
        }
        
        // We always check, if player is null for player-related placeholders.
        if(player == null) {
            return "";
        }
       
        
        // We return null if any other identifier was provided
        return null;
    }

	@Override
	public @NotNull String getIdentifier() {
		// TODO Auto-generated method stub
		return plugin.getDescription().getName();
	}

	@Override
	public @NotNull String getAuthor() {
		// TODO Auto-generated method stub
		return plugin.getDescription().getAuthors().get(0);
	}

	@Override
	public @NotNull String getVersion() {
		// TODO Auto-generated method stub
		return plugin.getDescription().getVersion();
	}
}