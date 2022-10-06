package me.ghost.hg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;

public class KitCMD implements CommandExecutor {
	private Main m;

	public KitCMD(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Só players podem escolher kit");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("kit")) {
			if (m.stage == Estagio.PREGAME) {
				if (m.PreGameTimer <= 15) {
					p.sendMessage("§cVocê não pode pegar kit agora");
					return true;
				}
			}
			if (args.length == 0) {
				m.kit.printKitChat(p);
			} else if (args.length == 1) {
				m.kit.setKit(p, args[0]);
			} else {
				m.kit.printKitChat(p);
			}
			return true;
		}
		return false;
	}
}
