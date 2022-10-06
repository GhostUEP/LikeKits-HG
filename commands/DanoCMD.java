package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class DanoCMD implements CommandExecutor {
	public Main m;

	public DanoCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("dano")) {
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
						m.dano = false;
						Bukkit.broadcastMessage("§fDano global: §4§lDesativado");
					} else if (args[0].equalsIgnoreCase("on")) {
						m.dano = true;
						Bukkit.broadcastMessage("§fDano global: §a§lAtivado");
					}
				} else {
					sender.sendMessage("§cUse: /dano [off/on]");
				}
			} else {
				sender.sendMessage("§cUse: /dano [off/on]");
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("pvp")) {
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
						m.pvp = false;
						Bukkit.broadcastMessage("§fPvP global: §4§lDesativado");
					} else if (args[0].equalsIgnoreCase("on")) {
						m.pvp = true;
						Bukkit.broadcastMessage("§fPvP global: §a§lAtivado");
					}
				} else {
					sender.sendMessage("§cUse: /pvp [off/on]");
				}
			} else {
				sender.sendMessage("§cUse: /pvp [off/on]");
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("evento")) {
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
						m.evento = false;
						Bukkit.broadcastMessage("§fEvento: §4§lDesativado");
					} else if (args[0].equalsIgnoreCase("on")) {
						m.evento = true;
						Bukkit.broadcastMessage("§fEvento: §a§lAtivado");
					}
				} else {
					sender.sendMessage("§cUse: /evento [off/on]");
				}
			} else {
				sender.sendMessage("§cUse: /evento [off/on]");
			}
			return true;
		}
		return false;
	}
}
