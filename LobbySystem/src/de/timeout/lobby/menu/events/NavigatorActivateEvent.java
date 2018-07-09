package de.timeout.lobby.menu.events;

import org.bukkit.entity.Player;

import de.timeout.lobby.menu.items.Navigator;

public class NavigatorActivateEvent extends PlayerItemActivateEvent {
	
	private Navigator navigator;
	
	public NavigatorActivateEvent(Player who, Navigator navigator) {
		super(who);
		this.navigator = navigator;
	}

	@Override
	public Navigator getActivatedItem() {
		return navigator;
	}

}
