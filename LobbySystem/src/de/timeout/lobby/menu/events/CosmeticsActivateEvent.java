package de.timeout.lobby.menu.events;

import org.bukkit.entity.Player;

import de.timeout.lobby.menu.items.Cosmetics;

public class CosmeticsActivateEvent extends PlayerItemActivateEvent {

	private Cosmetics cosmetics;
	
	public CosmeticsActivateEvent(Player who, Cosmetics cosmetics) {
		super(who);
		this.cosmetics = cosmetics;
	}

	@Override
	public Cosmetics getActivatedItem() {
		return null;
	}

	public Cosmetics getCosmetics() {
		return cosmetics;
	}
}
