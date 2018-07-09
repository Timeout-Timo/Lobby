package de.timeout.lobby.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;

import de.timeout.lobby.Lobby;

public class LobbyWorld {

	private static final Lobby main = Lobby.plugin;
	
	private boolean chat, dayLight, changeWeather, spawn;
	private Location spawnLocation;
	private Weather normalWeather;
	
	public LobbyWorld(Location spawn, boolean chat, boolean daylight, boolean spawnTeleport, String weather) {
		this.chat = chat;
		this.dayLight = daylight;
		this.spawnLocation = spawn;
		this.normalWeather = Weather.getWeather(weather);
		this.spawn = spawnTeleport;
	}
	
	public LobbyWorld() {
		String worldname = main.getConfig().getString("spawn.worldname");
		
		if(Bukkit.getServer().getWorld(worldname) == null)Bukkit.getServer().createWorld(new WorldCreator(worldname));
		this.spawnLocation = new Location(Bukkit.getServer().getWorld(worldname),
				main.getConfig().getDouble("spawn.x"), main.getConfig().getDouble("spawn.y"), main.getConfig().getDouble("spawn.z"));
		this.spawnLocation.setPitch((float) main.getConfig().getDouble("spawn.pitch"));
		this.spawnLocation.setYaw((float) main.getConfig().getDouble("spawn.yaw"));
		
		this.chat = main.getConfig().getBoolean("chat");
		this.dayLight = main.getConfig().getBoolean("daylightCircle");
		this.changeWeather = main.getConfig().getBoolean("weather.change");
		this.normalWeather = Weather.getWeather(main.getConfig().getString("weather.default"));
		this.spawn = main.getConfig().getBoolean("jointeleport");
	}

	public boolean isChat() {
		return chat;
	}

	public void setChat(boolean chat) {
		this.chat = chat;
	}
	
	public World getWorld() {
		return spawnLocation.getWorld();
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	public boolean isDayLight() {
		return dayLight;
	}

	public void setDayLight(boolean dayLight) {
		this.dayLight = dayLight;
	}
	
	public boolean isInWorld(Entity e) {
		return e.getWorld().getName().equalsIgnoreCase(getWorld().getName());
	}
	
	public boolean isInstance(World world) {
		return world.getName().equalsIgnoreCase(getWorld().getName());
	}

	public boolean isChangeWeather() {
		return changeWeather;
	}

	public void setChangeWeather(boolean changeWeather) {
		this.changeWeather = changeWeather;
	}

	public Weather getNormalWeather() {
		return normalWeather;
	}

	public void setNormalWeather(String normalWeather) {
		this.normalWeather = Weather.getWeather(normalWeather);
	}
	
	public void setNormalWeather(Weather weather) {
		this.normalWeather = weather;
	}
	
	public boolean isSpawn() {
		return spawn;
	}

	public void setSpawn(boolean spawn) {
		this.spawn = spawn;
	}
	
	public enum Weather {
		
		SUN("CLEAR"),
		RAIN("RAINING"),
		STORM("THUNDER");
		
		private String name;
		
		private Weather(String name) {
			this.name = name;
		}
		
		public static Weather getWeather(String name) {
			for(Weather w : Weather.values()) {
				if(w.getName().equalsIgnoreCase(name))return w;
			}
			throw new NullPointerException("Enum cannot be found");
		}
		
		public String getName() {
			return name;
		}
	}
}
