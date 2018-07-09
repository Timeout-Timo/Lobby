package de.timeout.lobby.menu;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface MenuItem extends Listener {

	public void onActivate(PlayerInteractEvent event);
	
	public ItemStack getItemStack();
	public MenuItemType getType();
	
	public enum MenuItemType {
		
		CUSTOM("custom"),
		COSMETICS("cosmetics"),
		NAVIGATOR("navigator");
		
		private String name;
		
		private MenuItemType(String name) {
			this.name = name;
		}
		
		public static MenuItemType getMenuItemType(String name) {
			for(MenuItemType type : values()) {
				if(type.getName().equalsIgnoreCase(name))return type;
			}
			throw new NullPointerException("Couldn't find Enum");
		}
		
		public String getName() {
			return name;
		}
	}
}
