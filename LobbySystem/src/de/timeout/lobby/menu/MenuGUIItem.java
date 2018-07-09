package de.timeout.lobby.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface MenuGUIItem extends MenuItem {

	public void onClick(InventoryClickEvent event);
}
