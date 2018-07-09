package de.timeout.lobby.menu;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.lobby.ConfigManager;
import de.timeout.lobby.menu.MenuItem.MenuItemType;
import de.timeout.lobby.menu.items.Custom;
import de.timeout.lobby.menu.items.Navigator;
import de.timeout.lobby.utils.ItemStackAPI;
import de.timeout.lobby.utils.Reflections;
import de.timeout.lobby.utils.Skull;
import de.timeout.lobby.utils.UTFConfig;
import net.md_5.bungee.api.ChatColor;

public class Menu {
	
	private Player player;
	private HashMap<Integer, MenuItem> contents = new HashMap<Integer, MenuItem>();
	
	public Menu(Player player) {
		this.player = player;
		fillMap();
	}
	
	private Menu() {
		fillMap();
	}
	
	public static Menu getMenuManager() {
		return new Menu();
	}
	
	private void fillMap() {
		UTFConfig cfg = ConfigManager.getMenu();
		
		ConfigurationSection section = cfg.getConfigurationSection("menu");
		for(int i = 0; i < 9; i++) {
			if(section.contains(String.valueOf(i))) {
				ConfigurationSection item = section.getConfigurationSection(String.valueOf(i));
				String perm = item.getString("required");
				
				if(perm == null || (perm != null || perm != "")) {
					if(player == null || (player != null || player.hasPermission(perm))) {
						MenuItemType type = MenuItemType.getMenuItemType(item.getString("type"));
						String name = ChatColor.translateAlternateColorCodes('&', item.getString("displayname"));
						Material mat = Material.valueOf(item.getString("material"));
						short subid = (short) (item.getInt("subid") != 0 ? item.getInt("subid") : 0);
						
						ItemStack menuitem = (mat == Material.SKULL_ITEM && subid == 3) ? Skull.getSkull(Reflections.getGameProfile(player), name) : ItemStackAPI.createItemStack(mat, subid, 1, name);
						if(type == MenuItemType.NAVIGATOR) {
							Navigator navi = player != null ? new Navigator(menuitem, player) : Navigator.getNavigatorManager();
							contents.put(i, navi);
						} else if(type == MenuItemType.COSMETICS) {
							
						} else contents.put(i, new Custom(menuitem));
					}
				}
			}
		}
	}
 
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void addItem(Object item) {
		if(item instanceof MenuItem) {
			contents.put(contents.size() -1, (MenuItem)item);
		} else throw new IllegalArgumentException("Parameter isn't instance of MenuItem");
	}
	
	public void setItem(Object item, int slot) {
		if(item instanceof MenuItem) {
			if(contents.containsKey(slot))contents.replace(slot, (MenuItem) item);
			else contents.put(slot, (MenuItem) item);
		} else throw new IllegalArgumentException("Parameter isn't instance of MenuItem");
	}
	
	public void removeItem(int slot) {
		contents.remove(slot);
	}

	public HashMap<Integer, MenuItem> getContents() {
		return contents;
	}
	
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(null, player.getInventory().getSize());
		for(int i = 0; i < inv.getSize(); i++) {
			if(contents.containsKey(i)) {
				MenuItem item = contents.get(i);
				inv.setItem(i, item.getItemStack());
			}
		}
		return inv;
	}
	
	public void unregisterListeners() {
		contents.keySet().forEach(i -> PlayerInteractEvent.getHandlerList().unregister(contents.get(i)));
		contents.keySet().forEach(i -> InventoryClickEvent.getHandlerList().unregister(contents.get(i)));
	}
}
