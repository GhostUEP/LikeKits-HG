package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class DropsCMD implements CommandExecutor {
	public Main m;

	public DropsCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("toggledrops")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!m.perm.isTrial(p)) {
					p.sendMessage("§cSem permissao");
					return true;
				}
			}
			if (args.length > 0) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("off")) {
						m.drops = false;
						Bukkit.broadcastMessage("§fDrops global: §4§lDesativado");
					} else if (args[0].equalsIgnoreCase("on")) {
						m.drops = true;
						Bukkit.broadcastMessage("§fDrops global: §a§lAtivado");
					}
				} else {
					sender.sendMessage("§cUse: /toggledrops [off/on]");
				}
			} else {
				sender.sendMessage("§cUse: /toggledrops [off/on]");
			}
			return true;
		}
		return false;
	}
}
