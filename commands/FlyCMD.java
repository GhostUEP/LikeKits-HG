package me.ghost.hg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;

public class FlyCMD implements CommandExecutor {
	private Main m;

	public FlyCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("fly")) {
			if (!(sender instanceof Player))
				return true;
			Player p = (Player) sender;
			if (!m.perm.isPro(p)) {
				p.sendMessage("§cSem permissao, compre VIP para poder voar");
				return true;
			}
			if (m.stage == Estagio.PREGAME && m.PreGameTimer > 15) {
				if (!m.isFlying.contains(p.getUniqueId())) {
					p.sendMessage("§bAgora você pode voar");
					p.setAllowFlight(true);
					p.setFlying(true);
					m.isFlying.add(p.getUniqueId());
					return true;
				} else {
					p.sendMessage("§bVoce parou de voar");
					p.setAllowFlight(false);
					p.setFlying(false);
					m.isFlying.remove(p.getUniqueId());
					return true;
				}
			} else {
				p.sendMessage("§cVocê não pode usar isso agora");
				return true;
			}
		}
		return false;
	}
}
