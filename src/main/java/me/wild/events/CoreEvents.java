package me.wild.events;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.wild.PlayerTags;

public class CoreEvents implements Listener{
	

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayerTags main = PlayerTags.getInstance();
		main.tagsPage.put(event.getPlayer(), 0);
		
		if (main.currentTags.get(event.getPlayer().getUniqueId().toString()) == null) {
			
			main.currentTags.put(event.getPlayer().getUniqueId().toString(), "");
			main.getUserData().set(event.getPlayer().getUniqueId().toString() + ".tag", "");
			try {
				main.getUserData().save(main.getConfigManager().getConfig("data.yml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		PlayerTags main = PlayerTags.getInstance();
		main.tagsPage.remove(event.getPlayer());
	}
}
