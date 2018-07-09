package de.timeout.lobby;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.timeout.lobby.menu.MenuManager;
import de.timeout.lobby.menu.items.NavigatorCommand;
import de.timeout.lobby.utils.UTFConfig;
import de.timeout.lobby.world.LobbyManager;
import de.timeout.lobby.world.SetspawnCommand;
import de.timeout.lobby.world.SetupCommand;
import net.md_5.bungee.api.ChatColor;

public class Lobby extends JavaPlugin {
	
	public static Lobby plugin;
	private UTFConfig config;
	
	public List<Player> setupMode = new ArrayList<Player>();
	public List<Player> modifyMode = new ArrayList<Player>();
		
	@Override
	public void onEnable() {
		plugin = this;
		ConfigCreator.loadConfigs();
		config = new UTFConfig(new File(getDataFolder(), "config.yml"));
		
		//Registering
		registerCommands();
		registerListener();
	}
	
	@Override
	public void onDisable() {

	}

	@Override
	public UTFConfig getConfig() {
		return config;
	}
	
	private void registerListener() {
		Bukkit.getServer().getPluginManager().registerEvents(new LobbyManager(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new MenuManager(), this);
	}
	
	private void registerCommands() {
		this.getCommand("setspawn").setExecutor(new SetspawnCommand());
		this.getCommand("navigator").setExecutor(new NavigatorCommand());
		this.getCommand("setup").setExecutor(new SetupCommand());
	}
	
	public String getLanguage(String path) {
		return ChatColor.translateAlternateColorCodes('&', ConfigManager.getLanguageConfig().getString(path));
	}
}
