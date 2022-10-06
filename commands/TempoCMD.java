package me.ghost.hg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.utils.DateUtils;

public class TempoCMD implements CommandExecutor {

	public Main m;

	public TempoCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tempo")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!m.perm.isTrial(p)) {
					p.sendMessage("§cSem permissao");
					return true;
				}
			}
			if (args.length == 0) {
				sender.sendMessage("§cUse: /tempo [tempo]");
				return true;
			}
			long time;
			try {
				time = DateUtils.parseDateDiff(args[0], true);
			} catch (Exception e) {
				sender.sendMessage("§cFormato invalido.");
				return true;
			}
			int seconds = (int) Math.floor((time - System.currentTimeMillis()) / 1000);
			if (m.stage == Estagio.PREGAME) {
				m.PreGameTimer = seconds;
				sender.sendMessage("§cTempo mudado para §7" + m.getTime(seconds));
				return true;
			} else if (m.stage == Estagio.INVENCIBILITY) {
				m.Invenci = seconds;
				sender.sendMessage("§cTempo mudado para §7" + m.getTime(seconds));
				return true;
			} else if (m.stage == Estagio.GAMETIME) {
				m.GameTimer = seconds;
				sender.sendMessage("§cTempo mudado para §7" + m.getTime(seconds));
				return true;
			}
		}
		return false;
	}
}
