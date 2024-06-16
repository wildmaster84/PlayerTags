package me.wild.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.wild.PlayerTags;
import me.wild.menus.TagsMenu;
public class TagsEvents implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getInventory().getHolder() instanceof TagsMenu) {
			PlayerTags main = PlayerTags.getInstance();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			if (event.getClickedInventory().getHolder() instanceof TagsMenu) {
				if (clickedItem.getType() == Material.ARROW && event.getSlot() == 53) {
					main.tagsPage.replace(player, main.tagsPage.get(player) + 1);
					
					main.getTagsMenu().setInventory(main.tagsInv.get(player), main.tagsPage.get(player), player);
			
				}
				if (clickedItem.getType() == Material.ARROW && event.getSlot() == 45) {
					main.tagsPage.replace(player, main.tagsPage.get(player) - 1);
					main.getTagsMenu().setInventory(main.tagsInv.get(player), main.tagsPage.get(player), player);	
				}
				
				if (clickedItem.getType() == Material.PLAYER_HEAD && event.getSlot() == 49) {
					if (main.currentTags.get(player.getUniqueId().toString()) != null || 
							!main.currentTags.get(player.getUniqueId().toString()).isEmpty() ||
							!main.currentTags.get(player.getUniqueId().toString()).isBlank()) {
						
						main.currentTags.put(player.getUniqueId().toString(), "");
						main.getUserData().set(player.getUniqueId().toString() + ".tag", "");
						try {
							main.getUserData().save(main.getConfigManager().getConfig("data.yml"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						player.closeInventory();
					}
				}
				
				if (clickedItem.getType() == Material.IRON_DOOR && event.getSlot() == 48 || clickedItem.getType() == Material.IRON_DOOR && event.getSlot() == 50) {
					player.closeInventory();
				}
	
				if (clickedItem.getType() == Material.NAME_TAG){
					ItemMeta meta = clickedItem.getItemMeta();
					String tag = meta.getPersistentDataContainer().get(new NamespacedKey(main, "tag"), PersistentDataType.STRING);
					String perms = meta.getPersistentDataContainer().get(new NamespacedKey(main, "perms"), PersistentDataType.STRING);
					if (player.hasPermission(perms)) {
						main.currentTags.put(player.getUniqueId().toString(), tag);
						
						main.getUserData().set(player.getUniqueId().toString() + ".tag", tag);
						try {
							main.getUserData().save(main.getConfigManager().getConfig("data.yml"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//Save To Config
						player.closeInventory();
					}
					
					
										
				}
			}
			return;
		}
		
	}
}
