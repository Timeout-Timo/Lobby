package de.timeout.lobby.menu.items;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.timeout.lobby.Lobby;
import de.timeout.lobby.menu.MenuItem;
import de.timeout.lobby.menu.events.CustomItemActivateEvent;

public class Custom implements MenuItem {
	
	protected static final Lobby main = Lobby.plugin;

	protected ItemStack item;
	
	public Custom(ItemStack item) {
		this.item = item;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onActivate(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(!event.isCancelled() && event.getItem() != null) {
			ItemStack item = event.getItem();
			if(item.isSimilar(this.item)) {
				CustomItemActivateEvent e = new CustomItemActivateEvent(p, this);
				Bukkit.getPluginManager().callEvent(e);
			}
		}
	}

	@Override
	public ItemStack getItemStack() {
		return item;
	}

	@Override
	public MenuItemType getType() {
		return MenuItemType.CUSTOM;
	}

	public void setItemStack(ItemStack item) {
		this.item = item;
	}
	
}
