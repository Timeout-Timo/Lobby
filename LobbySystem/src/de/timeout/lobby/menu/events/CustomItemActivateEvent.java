package de.timeout.lobby.menu.events;

import org.bukkit.entity.Player;

import de.timeout.lobby.menu.items.Custom;

public class CustomItemActivateEvent extends PlayerItemActivateEvent {
	
	private Custom item;

	public CustomItemActivateEvent(Player who, Custom custom) {
		super(who);
		this.item = custom;
	}

	@Override
	public Custom getActivatedItem() {
		return item;
	}

}
