package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;

public class MacrotestCMD implements CommandExecutor {
	public Main m;

	public MacrotestCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("macrotest")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!m.perm.isTrial(p)) {
					p.sendMessage("§cSem permissao");
				}
			}
			if (args.length != 1) {
				sender.sendMessage("§cVocê não mencionou o player");
				return true;
			}
			final Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage("§cJogador não encontrado");
				return true;
			}

			if (m.clicks.contains(player.getUniqueId())) {
				sender.sendMessage("§cEste jogador já esta em teste!");
				return true;
			}

			m.clicks.put(player.getUniqueId(), 0);
			new BukkitRunnable() {
				public void run() {
					sender.sendMessage("§cMacroTest §8- §c" + player.getDisplayName());
					sender.sendMessage("§cClicks §7--> §c" + m.clicks.get(player.getUniqueId()));
					sender.sendMessage(
							"§cProbabilidade §7--> §r" + getProbabilidade(m.clicks.get(player.getUniqueId())));
					m.clicks.remove(player.getUniqueId());
				}
			}.runTaskLater(Main.plugin, 20L);
			return true;
		}
		return false;
	}

	protected String getProbabilidade(int i) {
		if (i < 6) {
			return "§fNenhuma";
		}
		if (i < 12) {
			return "§aBaixa";
		}
		if (i < 15) {
			return "§eMedia";
		}
		if (i < 21) {
			return "§cAlta";
		}
		if (i < 30) {
			return "§4Alta";
		}
		if (i < 40) {
			return "§4Maxima";
		}
		if (i < Integer.MAX_VALUE) {
			return "§4§lCerteza";
		}
		return "§4Error";
	}
}
