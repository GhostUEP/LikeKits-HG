package me.ghost.hg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class SpecsCMD implements CommandExecutor {
	public Main m;

	public SpecsCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("specs")) {
			Player p = (Player) sender;
			if (m.perm.isTrial(p)) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("on")) {
						m.vanish.setSpectatorEnabled(p, true);
						m.vanish.updateVanished();
						p.sendMessage("§7Espectadores §a§lATIVADOS!");
					} else if (args[0].equalsIgnoreCase("off")) {
						m.vanish.setSpectatorEnabled(p, false);
						m.vanish.updateVanished();
						p.sendMessage("§7Espectadores: §c§lDESATIVADOS!");
					} else {
						sender.sendMessage("§cUse: /specs [on - off]");
					}
				} else {
					sender.sendMessage("§cUse: /specs [on - off]");
				}
			} else {
				sender.sendMessage("§cSem permissão");
			}
			return true;
		}
		return false;
	}
}
