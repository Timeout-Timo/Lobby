package de.timeout.lobby.menu.items;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.timeout.lobby.Lobby;
import de.timeout.lobby.menu.MenuGUIItem;


public class Cosmetics implements MenuGUIItem {
	
	private static final Lobby main = Lobby.plugin;
	
	private ItemStack item;
	private Player player;
	
	public Cosmetics(ItemStack item, Player player) {
		this.item = item;
		this.player = player;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	private Cosmetics() {}

	public static Cosmetics getCosmeticsManager() {
		return new Cosmetics();
	}
	
	@Override
	@EventHandler
	public void onActivate(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(event.getItem() != null) {
			if(this.item.isSimilar(event.getItem()) && this.player == p && !event.isCancelled()) {
				
			}
		}
	}

	@Override
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
	}
	
	@Override
	public ItemStack getItemStack() {
		return item;
	}

	@Override
	public MenuItemType getType() {
		return MenuItemType.COSMETICS;
	}

	public Player getPlayer() {
		return player;
	}

	public void setItemStack(ItemStack item) {
		this.item = item;
	}


}
