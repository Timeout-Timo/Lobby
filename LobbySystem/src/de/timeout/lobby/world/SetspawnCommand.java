package de.timeout.lobby.world;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.timeout.lobby.Lobby;

public class SetspawnCommand implements CommandExecutor {

	private static final Lobby main = Lobby.plugin;
	
	private String prefix = main.getLanguage("prefix");
	private String sucess = main.getLanguage("setspawn.sucess");
	
	private String permissions = main.getLanguage("util.permissions");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("lobby.setspawn")) {
				Location loc = p.getLocation();
				
				main.getConfig().set("spawn.x", loc.getX());
				main.getConfig().set("spawn.y", loc.getY());
				main.getConfig().set("spawn.z", loc.getZ());
				main.getConfig().set("spawn.pitch", loc.getPitch());
				main.getConfig().set("spawn.yaw", loc.getYaw());
				main.saveConfig();
				
				p.sendMessage(prefix + sucess.replace("[x]", String.valueOf(loc.getX())).replace("[y]", String.valueOf(loc.getY()))
						.replace("[z]", String.valueOf(loc.getZ())).replace("[world]", loc.getWorld().getName()));
			} else p.sendMessage(prefix + permissions);
		}
		return false;
	}

}
