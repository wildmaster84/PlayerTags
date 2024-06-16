package me.wild.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import me.wild.PlayerTags;
import net.md_5.bungee.api.ChatColor;
public class TagsCommand implements CommandExecutor {

	@Override
	public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
		PlayerTags main = PlayerTags.getInstance();
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		if (main.tagsInv.get(player) == null) {
			main.tagsInv.put(player, Bukkit.createInventory(main.getTagsMenu(), 6*9, ChatColor.translateAlternateColorCodes('&', "&6Server Tags&f: &6") + main.getTagsMenu().total));
		}
		main.getTagsMenu().setInventory(main.tagsInv.get(player), main.tagsPage.get(player), player);
		
		player.openInventory(main.getTagsMenu().getInventory());
		
		return true;
	}
	

}
