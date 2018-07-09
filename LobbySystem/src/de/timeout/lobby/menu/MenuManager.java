package de.timeout.lobby.menu;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.timeout.lobby.Lobby;

public class MenuManager implements Listener {
	
	protected static final Lobby main = Lobby.plugin;
	
	private HashMap<Player, Menu> menuCache = new HashMap<Player, Menu>();
	
	public MenuManager() {
		Bukkit.getServer().getOnlinePlayers().forEach(this :: addMenu);
	}
	
	private void addMenu(Player p) {
		Menu menu = new Menu(p);
		menuCache.put(p, menu);
		
		for(int i = 0; i < menu.getInventory().getSize(); i++) 
			p.getInventory().setItem(i, menu.getInventory().getItem(i));
		p.updateInventory();
	}
	
	/* Registering Listeners */
	
	/**
	 * Sets Inventory for the Player
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		addMenu(p);
	}
	
	/**
	 * Removes Listener after Logout
	 */
	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Menu menu = menuCache.get(p);
		
		menu.unregisterListeners();
		menuCache.remove(p);
	}
}
