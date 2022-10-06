package me.ghost.hg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.listeners.GameListener;

public class FeastCMD implements CommandExecutor {
	public Main m;

	public FeastCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cVocê não é um player");
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("feast")) {
			if (GameListener.feast == null || !GameListener.feast.isSpawned()) {
				p.sendMessage("§cO Feast ainda não spawnou");
				return true;
			}
			p.setCompassTarget(GameListener.feast.central.getLocation());
			p.sendMessage("§6Bussola apontando para o §7Feast§6: [§7" + GameListener.feast.central.getX() + "§6, §7"
					+ GameListener.feast.central.getY() + "§6, §7" + GameListener.feast.central.getZ() + "§6]");
			return true;
		}
		return false;
	}
}
