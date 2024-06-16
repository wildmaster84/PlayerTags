package me.wild.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import me.wild.PlayerTags;
import net.md_5.bungee.api.ChatColor;

public class TagsMenu implements InventoryHolder {
	private List<ItemStack> tags = new ArrayList<>();
	public int total = 0;
	private Inventory inv;
	private PlayerTags main;
	
	public TagsMenu() {
		this.main = PlayerTags.getInstance();
		addItems(false);
		
	}
	
	public void addItems(boolean clear) {
		PlayerTags main = PlayerTags.getInstance();
		YamlConfiguration emotesConfig = main.getTags();
		List<String> lore = new ArrayList<>();
		if (clear) {
			tags.clear();
		}
		if (tags.isEmpty()) {
			emotesConfig.getConfigurationSection("playertags").getKeys(false).forEach(item -> {
				lore.clear();
				ItemStack tag = new ItemStack(Material.getMaterial(emotesConfig.getString("gui_item")));
				ItemMeta meta = tag.getItemMeta();
				String tagName = emotesConfig.getString("playertags." + item.toString() + ".tag");
				String perms = emotesConfig.getString("playertags." + item.toString() + ".permission");
				meta.setDisplayName(hexToColor("&6Tag&f: &6" + item.toString()));
				lore.add(hexToColor(tagName));
				
				meta.setLore(lore);
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "tag"), PersistentDataType.STRING, tagName);
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "perms"), PersistentDataType.STRING, perms);
				tag.setItemMeta(meta);
				
				tags.add(tag);
				total++;
			}); 
		}
	}

	private <T> List<List<T>> splitList(List<T> list, int size) {
        List<List<T>> sublists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            int endIndex = Math.min(i + size, list.size());
            sublists.add(list.subList(i, endIndex));
        }
        return sublists;
    }

	public String hexToColor(String message) {
		Pattern pattern = Pattern.compile("(#\\w{6})");
        Matcher matcher = pattern.matcher(message.replaceAll("\\[|\\]", ""));

        StringBuffer buffer = new StringBuffer();
        
        while (matcher.find()) {
          matcher.appendReplacement(buffer, ChatColor.of(matcher.group()).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }
	
	private void setPageItems(int page, Player player) {
		inv.clear();
		
		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Back");
		back.setItemMeta(meta);
		
		ItemStack next = new ItemStack(Material.ARROW);
		ItemMeta meta2 = next.getItemMeta();
		meta2.setDisplayName("Next");
		next.setItemMeta(meta2);
		
		ItemStack spacer = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta3 = next.getItemMeta();
		meta3.setDisplayName(" ");
		spacer.setItemMeta(meta3);
		
		ItemStack doors = new ItemStack(Material.IRON_DOOR);
		ItemMeta meta4 = next.getItemMeta();
		meta4.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cExit"));
		doors.setItemMeta(meta4);
		
		ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta5 = (SkullMeta) playerHead.getItemMeta();
		List<String> headLore = new ArrayList<>();
		String tagIdentifier = ChatColor.stripColor(hexToColor(main.currentTags.get(player.getUniqueId().toString())));
		meta5.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eCurrent tag&f: &6") + tagIdentifier);
		headLore.add(hexToColor(main.currentTags.get(player.getUniqueId().toString())));
		meta5.setLore(headLore);		
		meta5.setOwningPlayer(player);
		playerHead.setItemMeta(meta5);
		
		inv.setItem(36, spacer);
		inv.setItem(37, spacer);
		inv.setItem(38, spacer);
		inv.setItem(39, spacer);
		inv.setItem(40, spacer);
		inv.setItem(41, spacer);
		inv.setItem(42, spacer);
		inv.setItem(43, spacer);
		inv.setItem(44, spacer);
		inv.setItem(48, doors);
		inv.setItem(49, playerHead);
		inv.setItem(50, doors);
		
		
		if (!tags.isEmpty()) {
			
			int pages = splitList(tags, 36).size();
			
			// Page Buttons
			if (pages >= 2 && page != pages - 1) {
				inv.setItem(53, next);
			}
			if (page >= 1) {
				inv.setItem(45, back);
			}
			
			// Page Items
			for (ItemStack item: splitList(tags, 36).get(page)) {
				ItemMeta itemMeta = item.getItemMeta();
				List<String> lore = new ArrayList<>();
				
				if (player.hasPermission(itemMeta.getPersistentDataContainer().get(new NamespacedKey(main, "perms"), PersistentDataType.STRING))) {
					lore.add(item.getLore().get(0));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&aUnlocked"));
					item.setLore(lore);
				} else {
					lore.add(item.getLore().get(0));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&cLocked"));
					item.setLore(lore);
				}
				inv.addItem(item);
			}
		}
		
		//44 next page
		//36 last page
		
	}

	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return inv;
	}
	
	public void setInventory(Inventory inv, int page, Player player) {
		// TODO Auto-generated method stub
		this.inv = inv;
		setPageItems(page, player);
	}
	
	public List<ItemStack> getItems() {
		// TODO Auto-generated method stub
		return tags;
	}

}
