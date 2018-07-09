package de.timeout.lobby.menu.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public abstract class PlayerItemActivateEvent extends PlayerEvent implements Cancellable {
	
	protected static HandlerList handlers = new HandlerList();
	protected boolean cancel;
		
	public PlayerItemActivateEvent(Player who) {
		super(who);
	}
	
	public abstract Object getActivatedItem();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}
	
	@Override
	public void setCancelled(boolean arg0) {
		cancel = arg0;
	}
}
