package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class ChatCMD implements CommandExecutor {
	public Main m;

	public ChatCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("chat")) {
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
						m.chat = false;
						Bukkit.broadcastMessage("§fO Chat foi: §4§lDesativado");
					} else if (args[0].equalsIgnoreCase("on")) {
						m.chat = true;
						Bukkit.broadcastMessage("§fO Chat foi: §a§lAtivado");
					}
				} else {
					sender.sendMessage("§cUse: /chat [off/on]");
				}
			} else {
				sender.sendMessage("§cUse: /chat [off/on]");
			}
			return true;
		}
		return false;
	}
}
