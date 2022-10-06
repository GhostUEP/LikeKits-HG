package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Status;
import me.ghost.hg.constructors.Status.Order;

public class StatusCMD implements CommandExecutor {
	public Main m;

	public StatusCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("status")) {
			Status status = Status.getStatus(p.getUniqueId());
			if (args.length == 0) {
				if (status.getname().equalsIgnoreCase(""))
					p.sendMessage("§cSem status.");
				else {
					p.sendMessage("§7Wins: §6" + status.getWins());
					p.sendMessage("§7Kills: §6" + status.getKills());
					p.sendMessage("§7Mortes: §6" + status.getDeaths());
					p.sendMessage("§7KDR: §6" + status.getKDString());
				}
			} else if (args.length == 1) {
				Player p2 = Bukkit.getPlayer(args[0]);
				if (p2 == null) {
					p.sendMessage("§cJogador nao foi encontrado.");
					return true;
				}
				Status status2 = Status.getStatus(p2.getUniqueId());
				if (status2.getname().equalsIgnoreCase(""))
					p.sendMessage("§cSem status.");
				else {
					p.sendMessage("§7Wins: §6" + status2.getWins());
					p.sendMessage("§7Kills: §6" + status2.getKills());
					p.sendMessage("§7Mortes: §6" + status2.getDeaths());
					p.sendMessage("§7KDR: §6" + status2.getKDString());
				}
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("top10")) {
			if (args.length == 0) {
				p.sendMessage("§cUse /top10 [wins | kills | deaths | xp]");
				return true;
			} else if (args.length == 1) {
				if (!(args[0].equalsIgnoreCase("wins") || args[0].equalsIgnoreCase("kills")
						|| args[0].equalsIgnoreCase("deaths") || args[0].equalsIgnoreCase("xp"))) {
					p.sendMessage("§cUse /top10 [wins | kills | deaths | xp]");
					return true;
				}
				p.sendMessage("§6TOP 10 " + args[0].toUpperCase() + ":");
				int i = 0;
				for (Status stats : Status.getTopStatus(Integer.valueOf(10), Order.valueOf(args[0].toUpperCase()))) {
					i++;
					p.sendMessage("§c" + i + "º§7 - §6" + stats.getname() + "§7 com §6"
							+ stats.getTopStatus(Order.valueOf(args[0].toUpperCase())) + " " + args[0].toLowerCase());
				}
			} else {
				p.sendMessage("§cUse /top10 [wins | kills | deaths | xp]");
				return true;
			}
		}
		return false;
	}
}
