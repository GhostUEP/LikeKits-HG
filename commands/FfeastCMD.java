package me.ghost.hg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.utils.Feast;

public class FfeastCMD implements CommandExecutor {
	private Main m;

	public FfeastCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!m.perm.isMod(p)) {
				sender.sendMessage("§cSem permissao");
				return true;
			}
		}
		if (label.equalsIgnoreCase("ffeast")) {
			Feast f = new Feast(m, 100, 25, true);
			f.generateFeast();
			f.generateChests();
			m.getServer().broadcastMessage("§6O Feast spawnou" + " [§7" + f.central.getX() + ", " + f.central.getY()
					+ ", " + f.central.getZ() + "§6]");
			return true;
		}
		return false;
	}
}
