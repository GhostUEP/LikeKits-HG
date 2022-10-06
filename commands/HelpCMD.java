package me.ghost.hg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;

public class HelpCMD implements CommandExecutor {
	private Main m;

	public HelpCMD(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSó players podem usar /help");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("help") || label.equalsIgnoreCase("info")) {
			if (m.stage == Estagio.PREGAME) {
				p.sendMessage("§6Iniciando em: §7" + m.getHourTime(m.PreGameTimer));
				p.sendMessage("§6Kit 1: §7" + m.kit.getPlayerKit1(p));
				p.sendMessage("§6Kit 2: §7" + m.kit.getPlayerKit2(p));
				p.sendMessage("§6Antikit: §7" + m.kit.getPlayerAntiKit(p));
			} else if (m.stage == Estagio.GAMETIME) {
				p.sendMessage("§6Jogadores vivos: §7" + m.gamers.size());
				p.sendMessage("§6Tempo de jogo: §7" + m.getHourTime(m.GameTimer));
				p.sendMessage("§6Kills: §7" + m.getKills(p));
				p.sendMessage("§6Kit 1: §7" + m.kit.getPlayerKit1(p));
				p.sendMessage("§6Kit 2: §7" + m.kit.getPlayerKit2(p));
				p.sendMessage("§6Antikit: §7" + m.kit.getPlayerAntiKit(p));
			} else if (m.stage == Estagio.INVENCIBILITY) {
				p.sendMessage("§6Invencibilidade acaba em: §7" + m.getHourTime(m.Invenci));
				p.sendMessage("§6Kills: §7" + m.getKills(p));
				p.sendMessage("§6Kit 1: §7" + m.kit.getPlayerKit1(p));
				p.sendMessage("§6Kit 2: §7" + m.kit.getPlayerKit2(p));
				p.sendMessage("§6Antikit: §7" + m.kit.getPlayerAntiKit(p));
			} else {
				p.sendMessage("§6Vitória");
				p.sendMessage("§6Kills: §7" + m.getKills(p));
				p.sendMessage("§6Kit 1: §7" + m.kit.getPlayerKit1(p));
				p.sendMessage("§6Kit 2: §7" + m.kit.getPlayerKit2(p));
				p.sendMessage("§6Antikit: §7" + m.kit.getPlayerAntiKit(p));
			}
			return true;
		}
		return false;
	}
}
