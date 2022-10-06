package me.ghost.hg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class MinifeastCMD implements CommandExecutor {
	private Main m;

	public MinifeastCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!m.perm.isMod(p)) {
				sender.sendMessage("§cSem permissao");
				return true;
			}
		}
		if (label.equalsIgnoreCase("fmini")) {
			new me.ghost.hg.utils.Minifeast(m);
			return true;
		}
		return false;
	}
}
