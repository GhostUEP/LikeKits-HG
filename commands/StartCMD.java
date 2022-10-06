package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;

public class StartCMD implements CommandExecutor {
	public Main m;

	public StartCMD(Main m) {
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("start")) {
			if (!(sender instanceof Player)) {
				if (m.stage != Estagio.PREGAME) {
					sender.sendMessage("§cO torneio ja iniciou!");
					return true;
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (m.adm.isSpectating(p)) {
						continue;
					}
					p.setFlying(false);
					p.setAllowFlight(false);
				}
				m.startGame();
				sender.sendMessage("§aPartida iniciada");
				return true;
			}
			Player p = (Player) sender;
			if (!m.perm.isAdmin(p)) {
				sender.sendMessage("§cSem permissão");
				return true;
			}
			if (m.stage != Estagio.PREGAME) {
				sender.sendMessage("§cO torneio ja iniciou!");
				return true;
			}
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (m.adm.isSpectating(player)) {
					continue;
				}
				player.setFlying(false);
				player.setAllowFlight(false);
			}
			m.startGame();
			sender.sendMessage("§aPartida iniciada");
			return true;
		}
		return false;
	}

}
