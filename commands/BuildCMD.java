package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class BuildCMD implements CommandExecutor {
	public Main m;

	public BuildCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("build")) {
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
						m.build = false;
						Bukkit.broadcastMessage("§fConstrução de blocos: §4§lDesativado");
					} else if (args[0].equalsIgnoreCase("on")) {
						m.build = true;
						Bukkit.broadcastMessage("§fConstrução de blocos: §a§lAtivado");
					}
				} else {
					sender.sendMessage("§cUse: /build [off/on]");
				}
			} else {
				sender.sendMessage("§cUse: /build [off/on]");
			}
			return true;
		}
		return false;
	}
}
