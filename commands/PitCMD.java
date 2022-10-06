package me.ghost.hg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.utils.Pit;

public class PitCMD implements CommandExecutor {
	public Main m;

	public PitCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("pit")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!m.perm.isTrial(p)) {
					p.sendMessage("§cSem permissao");
					return true;
				}
			}
			if (m.stage != Estagio.GAMETIME) {
				sender.sendMessage("§cA partida ainda não começou");
				return true;
			}
			if (Pit.spawned) {
				sender.sendMessage("§cO Pit já foi spawnado");
				return true;
			}
			Main.pit2.spawnPit();
			return true;
		}
		return false;
	}
}
