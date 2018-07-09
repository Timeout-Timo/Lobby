package de.timeout.lobby;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import de.timeout.lobby.utils.UTFConfig;

public class ConfigManager {
	
	private static final Lobby main = Lobby.plugin;

	private static File langFile;
	private static UTFConfig langCfg;
	
	private static File menuFile;
	private static UTFConfig menuCfg;
	
	private static File navigatorFile;
	private static UTFConfig navigatorCfg;
	
	private ConfigManager() {}
	
	public static void loadLanguage() {
		if(langFile == null)
			langFile = new File(main.getDataFolder().getPath() + "/language", main.getConfig().getString("language") + ".yml");
		langCfg = new UTFConfig(langFile);
	}
	
	public static UTFConfig getLanguageConfig() {
		if(langCfg == null)loadLanguage();
		langCfg = new UTFConfig(langFile);
		return langCfg;
	}
	
	public static void saveLanguageFile() {
		saveCustomConfig(langFile, langCfg);
	}
	
	private static void saveCustomConfig(File file, UTFConfig config) {
		try {
			config.save(file);
		} catch (IOException e) {
			main.getLogger().log(Level.WARNING, "Could not save", e);
		}
	}
	
	/* Menu-Configuration */
	
	public static void loadMenuFile() {
		if(menuFile == null) 
			menuFile = new File(main.getDataFolder().getPath(), "menu.yml");
		menuCfg = new UTFConfig(menuFile);
	}
	
	public static UTFConfig getMenu() {
		if(menuFile == null)loadMenuFile();
		return menuCfg;
	}
	
	public static void saveMenu() {
		saveCustomConfig(menuFile, menuCfg);
	}
	
	/* Navigator-Configuration */
	
	public static void loadNavigator() {
		if(navigatorFile == null)
			navigatorFile = new File(main.getDataFolder() + "/items", "navigator.yml");
		navigatorCfg = new UTFConfig(navigatorFile);
	}
	
	public static UTFConfig getNavigator() {
		if(navigatorFile == null)loadNavigator();
		return navigatorCfg;
	}
	
	public static void saveNavigator() {
		saveCustomConfig(navigatorFile, navigatorCfg);
	}
}
