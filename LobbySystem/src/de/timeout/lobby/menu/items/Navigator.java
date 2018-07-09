package de.timeout.lobby.menu.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.lobby.ConfigManager;
import de.timeout.lobby.Lobby;
import de.timeout.lobby.menu.MenuGUIItem;
import de.timeout.lobby.menu.events.NavigatorActivateEvent;
import de.timeout.lobby.utils.ItemStackAPI;
import de.timeout.lobby.utils.Sounds;
import de.timeout.lobby.utils.UTFConfig;
import net.md_5.bungee.api.ChatColor;

public class Navigator implements MenuGUIItem {

	private static final Lobby main = Lobby.plugin;
	private static final String NAME_CONST = "settings.name";
	private static final String AMOUNT = "amount";
	private static final String MATERIAL = "material";
	
	private ItemStack item;
	private int lines;
	private String name;
	
	public Navigator(ItemStack item) {
		this.item = item;
		this.lines = ConfigManager.getNavigator().getInt("settings.size");
		this.name = ChatColor.translateAlternateColorCodes('&', ConfigManager.getNavigator().getString(NAME_CONST));
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	private Navigator(ItemStack item, int lines, String name) {
		this.item = item;
		this.name = name;
		this.lines = lines;
	}
	
	public static Navigator getNavigatorManager() {
		return new Navigator(null, ConfigManager.getNavigator().getInt("settings.size"),
				ChatColor.translateAlternateColorCodes('&', ConfigManager.getNavigator().getString(NAME_CONST)));
	}

	@Override
	@EventHandler
	public void onActivate(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(event.getItem() != null) {
			if(this.item.isSimilar(event.getItem())) {
				NavigatorActivateEvent e = new NavigatorActivateEvent(p, this);
				Bukkit.getPluginManager().callEvent(e);
				if(!e.isCancelled()) {
					Inventory inv = getNavigatorInventory();
					p.openInventory(inv);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlaySound(NavigatorActivateEvent event) {
		if(!event.isCancelled()) {
			Player p = event.getPlayer();
			p.playSound(p.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 1F, 1F);
		}
	}
	
	@Override
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		UTFConfig cfg = ConfigManager.getNavigator();
		String invname = ChatColor.translateAlternateColorCodes('&', cfg.getString(NAME_CONST));
		if(!event.isCancelled()) {
			if((event.getWhoClicked() instanceof Player) && event.getClickedInventory() != null && event.getCurrentItem() != null) {
				Player p = (Player) event.getWhoClicked();
				Inventory inv = event.getClickedInventory();
				if(inv.getName().equalsIgnoreCase(invname) && !main.modifyMode.contains(p)) {
					event.setCancelled(true);
					int slot = event.getSlot();
					 
					ConfigurationSection navi = cfg.getConfigurationSection("navigation");
					if(navi != null && navi.contains(String.valueOf(slot))) {
						ConfigurationSection rawtp = navi.getConfigurationSection(String.valueOf(slot));
						Location teleportLoc = new Location(Bukkit.getServer().getWorld(rawtp.getString("world")),
								rawtp.getDouble("x"), rawtp.getDouble("y"), rawtp.getDouble("z"));
						teleportLoc.setPitch((float) rawtp.getDouble("pitch"));
						teleportLoc.setYaw((float) rawtp.getDouble("yaw"));
						 
						p.teleport(teleportLoc);
						p.closeInventory();
					 }
				 }
			 }
		}
	 }

	@Override
	public ItemStack getItemStack() {
		return item;
	}
	
	public Inventory getNavigatorInventory() {
		Inventory inv = Bukkit.createInventory(null, lines *9, name);
		UTFConfig cfg = ConfigManager.getNavigator();
		
		ConfigurationSection inventory = cfg.getConfigurationSection("inventory");
		for(int i = 0; i < inv.getSize(); i++) {
			ConfigurationSection itemraw = inventory.contains(String.valueOf(i)) ? inventory.getConfigurationSection(String.valueOf(i)) : inventory.getConfigurationSection("n");
			
			String name = ChatColor.translateAlternateColorCodes('&', itemraw.getString("name"));
			Material mat = Material.valueOf(itemraw.getString(MATERIAL));
			int amount = itemraw.getInt(AMOUNT);
			short subid = (short) itemraw.getInt("subid");
				
			ItemStack item = ItemStackAPI.createItemStack(mat, subid, amount, name);
			inv.setItem(i, item);
		}
		
		return inv;
	}
	
	public Inventory getManagementInventory() {
		Inventory inv = Bukkit.createInventory(null, lines * 9, name);
		UTFConfig cfg = ConfigManager.getNavigator();
		
		ConfigurationSection inventory = cfg.getConfigurationSection("inventory");
		for(int i = 0; i < inv.getSize(); i++) {
			if(inventory.get(String.valueOf(i)) != null) {
				ConfigurationSection itemraw = inventory.getConfigurationSection(String.valueOf(i));
				
				String name = ChatColor.translateAlternateColorCodes('&', itemraw.getString("name"));
				Material mat = Material.valueOf(itemraw.getString(MATERIAL));
				int amount = itemraw.getInt(AMOUNT);
				short subid = (short) itemraw.getInt("subid");
				
				ItemStack item = ItemStackAPI.createItemStack(mat, subid, amount, name);
				inv.setItem(i, item);
			}
		}
		return inv;
	}
	
	public void convertToFile(Inventory inv) {
		for(int i = 0; i < inv.getSize(); i++) {
			if(inv.getItem(i) != null) {
				ItemStack item = inv.getItem(i);
				
				String name = ChatColor.translateAlternateColorCodes('&', item.getItemMeta().getDisplayName() != null ?
						item.getItemMeta().getDisplayName().replaceAll("&", "§") : "");
				Material mat = item.getType();
				int amount = item.getAmount();
				short subid = item.getDurability();
				
				final String path = "inventory." + i;
				
				ConfigManager.getNavigator().set(path + ".name", name);
				ConfigManager.getNavigator().set(path + ".material", mat.name());
				ConfigManager.getNavigator().set(path + ".amount", amount);
				ConfigManager.getNavigator().set(path + ".subid", subid);
				
				ConfigManager.saveNavigator();
			}
		}
	}
	
	public void addTeleportLocation(int slot, Location loc) {		
		final String path = "navigation." + slot;
		
		ConfigManager.getNavigator().set(path + ".world", loc.getWorld().getName());
		ConfigManager.getNavigator().set(path + ".x", loc.getX());
		ConfigManager.getNavigator().set(path + ".y", loc.getY());
		ConfigManager.getNavigator().set(path + ".z", loc.getZ());
		ConfigManager.getNavigator().set(path + ".yaw", loc.getYaw());
		ConfigManager.getNavigator().set(path + ".pitch", loc.getPitch());
		
		ConfigManager.saveNavigator();
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public MenuItemType getType() {
		return MenuItemType.NAVIGATOR;
	}
}
