package de.timeout.lobby.world;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import de.timeout.lobby.Lobby;
import de.timeout.lobby.world.LobbyWorld.Weather;

public class LobbyManager implements Listener {
	
	private static final Lobby main = Lobby.plugin;
	
	private LobbyWorld world;
	
	public LobbyManager() {
		world = new LobbyWorld();
		prepareWorld();
	}
	
	/**
	 * prepare LobbyWorld with actual Serversettings
	 */
	private void prepareWorld() {
		if(world.getNormalWeather() != Weather.SUN) {
			if(world.getNormalWeather() == Weather.RAIN)world.getWorld().setStorm(true);
			else world.getWorld().setThundering(true);
		}
		world.getWorld().setGameRuleValue("doDaylightCycle", String.valueOf(world.isDayLight()));
		world.getWorld().setGameRuleValue("mobGriefing", "false");
		world.getWorld().setGameRuleValue("doMobSpawning", "false");
		
		world.getWorld().setSpawnLocation(world.getSpawnLocation().getBlockX(), world.getSpawnLocation().getBlockY(), world.getSpawnLocation().getBlockZ());
	}
	
	/* Management Listeners for LobbyWorld */
	
	/**
	 * Cancel EntityDamage
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(world.isInWorld(event.getEntity()))event.setCancelled(true);
	}
	
	/**
	 * cancel EntityDamage from Block
	 */
	@EventHandler
	public void onBlockDamage(EntityDamageByBlockEvent event) {
		if(world.isInWorld(event.getEntity()))event.setCancelled(true);
	}
	
	/**
	 * cancel EntityDamage from Entity
	 */
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		//Hier Ausnahme einfügen, wenn Damager Buildrechte besitzt.
		if(world.isInWorld(event.getEntity()))event.setCancelled(true);
	}
	
	/**
	 * Manage Weatherchange
	 */
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		World world = event.getWorld();
		if(this.world.isInstance(world) && !this.world.isChangeWeather()) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Cancel ItemDrops
	 */
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(world.isInWorld(event.getPlayer()))event.setCancelled(true);
	}
	
	/**
	 * Cancel Itemdrag in Menu
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onClick(InventoryClickEvent event) {
		if(!event.isCancelled() && event.getWhoClicked() instanceof Player) {
			if((event.getClickedInventory() != null) && (event.getCurrentItem() != null)) {
				Player p = (Player) event.getWhoClicked();
				Inventory inv = event.getClickedInventory();
				if(inv.getHolder() != null && !main.setupMode.contains(p)) {
					InventoryHolder holder = inv.getHolder();
					if(holder instanceof Player && (p == (Player)holder))event.setCancelled(true);
				}
			}
		}
	}
	
	/**
	 * cancel Hungerchange
	 */
	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent event) {
		if(world.isInWorld(event.getEntity()) && event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			p.setFoodLevel(20);
			event.setCancelled(true);
		}
	}
	
	/**
	 * Teleports Player to SpawnLocation
	 * Remove JoinMessage 
	 */
	@EventHandler
	public void onSpawn(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(world.isInWorld(p) && world.isSpawn()) p.teleport(world.getSpawnLocation());
		event.setJoinMessage(null);
	}
	
	/**
	 * Remove LeaveMessage
	 */
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
	
	/**
	 * Manage BlockPlace
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		if(!main.setupMode.contains(p)) {
			event.setBuild(false);
			event.setCancelled(true);
		} else event.setBuild(true);
	}
	
	/**
	 * Manage BlockBreak
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		if(!main.setupMode.contains(p))event.setCancelled(true);
	}
}
