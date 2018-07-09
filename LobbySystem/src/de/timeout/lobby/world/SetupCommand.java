package de.timeout.lobby.world;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.timeout.lobby.Lobby;

public class SetupCommand implements CommandExecutor {
	
	private static final Lobby main = Lobby.plugin;
		
	private String prefix = main.getLanguage("prefix");
	private String permissions = main.getLanguage("util.permissions");
	private String activated = main.getLanguage("setup.activate");
	private String deactivate = main.getLanguage("setup.deactivate");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("lobby.setup")) {
				if(!main.setupMode.contains(p)) {
					main.setupMode.add(p);
					p.sendMessage(prefix + activated);
				} else {
					main.setupMode.remove(p);
					p.sendMessage(prefix + deactivate);
				}
			} else p.sendMessage(prefix + permissions);
		}
		return false;
	}
}
