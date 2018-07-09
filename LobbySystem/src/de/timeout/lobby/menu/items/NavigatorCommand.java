package de.timeout.lobby.menu.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.lobby.ConfigManager;
import de.timeout.lobby.Lobby;
import de.timeout.lobby.utils.ItemStackAPI;
import de.timeout.lobby.utils.UTFConfig;
import net.md_5.bungee.api.ChatColor;

public class NavigatorCommand implements CommandExecutor, Listener {
	
	private static final Lobby main = Lobby.plugin;
	
	private String prefix = main.getLanguage("prefix");
	
	private String permissions = main.getLanguage("util.permissions");
	private String falseCommand = main.getLanguage("util.falseCommand");
	
	public NavigatorCommand() {
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("lobby.setup")) {
				if(args.length == 1) {
					String name = args[0];
					Inventory inv;
					switch(name.toLowerCase()) {
					case "setup":
						if(!setupInv.containsKey(p)) {
							Navigator manage = Navigator.getNavigatorManager();
							inv = manage.getManagementInventory();
							p.openInventory(inv);
							setupInv.put(p, inv);
							main.modifyMode.add(p);
						} else p.openInventory(setupInv.get(p));
						break;
					case "teleport":
						Navigator manage = Navigator.getNavigatorManager();
						inv = manage.getManagementInventory();
						p.openInventory(inv);
						teleportList.add(p);
						break;
					case "save":
						inv = Bukkit.createInventory(null, 9, saveTitle);
						for(int i = 0; i < inv.getSize(); i++)inv.setItem(i, ItemStackAPI.createItemStack(Material.STAINED_GLASS_PANE, (short) 7, 1, "§7"));
						inv.setItem(1, ItemStackAPI.createItemStack(Material.STAINED_GLASS_PANE, (short) 5, 1, saveYes));
						inv.setItem(7, ItemStackAPI.createItemStack(Material.STAINED_GLASS_PANE, (short) 14, 1, saveNo));
						p.openInventory(inv);
						break;
					default:
						break;
					}
				} else p.sendMessage(prefix + falseCommand.replace("[command]", "/navigator setup | save | teleport"));
			} else p.sendMessage(prefix + permissions);
		}
		return false;
	}

	/* Listener Part */
	
	private UTFConfig config = ConfigManager.getNavigator();
	private HashMap<Player, Inventory> setupInv = new HashMap<Player, Inventory>();
	private List<Player> teleportList = new ArrayList<Player>();
	
	private String naviName = ChatColor.translateAlternateColorCodes('&', config.getString("settings.name"));
	private String saveTitle = main.getLanguage("util.gui.navigator.save.title");
	private String saveYes = main.getLanguage("util.gui.navigator.save.true");
	private String saveNo = main.getLanguage("util.gui.navigator.save.false");
	
	/**
	 * Caches modified and non-saved Navigator
	 */
	@EventHandler
	public void onCache(InventoryCloseEvent event) {
		if(event.getPlayer() instanceof Player) {
			Player p = (Player) event.getPlayer();
			Inventory inv = event.getInventory();
			
			if(teleportList.contains(p))teleportList.remove(p);
			if(setupInv.containsKey(p) && main.modifyMode.contains(p)) {
				if(inv.getHolder() == null && inv.getName().equalsIgnoreCase(naviName))setupInv.replace(p, inv);
				main.modifyMode.remove(p);
			}
		}
	}
	
	/**
	 * Manage Teleport-Setup GUI
	 */
	@EventHandler
	public void onTeleportSetup(InventoryClickEvent event) {
		if((event.getWhoClicked() instanceof Player) && event.getClickedInventory() != null) {
			Player p = (Player) event.getWhoClicked();
			int slot = event.getSlot();
			if(teleportList.contains(p)) {
				event.setCancelled(true);
				Location loc = p.getLocation();
				Navigator manager = Navigator.getNavigatorManager();
				manager.addTeleportLocation(slot, loc);
				p.closeInventory();
			}
		}
	}
	
	/**
	 * Manage Save-GUI
	 */
	@EventHandler
	public void onSaveClick(InventoryClickEvent event) {
		if((event.getWhoClicked() instanceof Player) && event.getClickedInventory() != null && event.getCurrentItem() != null) {
			Player p = (Player) event.getWhoClicked();
			Inventory inv = event.getClickedInventory();
			if(inv.getName().equalsIgnoreCase(saveTitle)) {
				event.setCancelled(true);
				ItemStack item = event.getCurrentItem();
				if(item.getItemMeta().getDisplayName().equalsIgnoreCase(saveYes)) {
					Navigator manage = Navigator.getNavigatorManager();
					manage.convertToFile(setupInv.get(p));
					setupInv.remove(p);
					p.closeInventory();
				} else if(item.getItemMeta().getDisplayName().equalsIgnoreCase(saveNo))setupInv.remove(p);
			}
		}
	}
}
