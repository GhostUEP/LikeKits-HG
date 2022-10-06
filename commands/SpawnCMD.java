package me.ghost.hg.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;

public class SpawnCMD implements CommandExecutor {
	public Main m;

	public SpawnCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!m.perm.isAdmin(p)) {
					p.sendMessage("§cSem permissao");
					return true;
				}
				Location loc = m.getRespawnLocation();
				new BukkitRunnable() {

					@Override
					public void run() {
						p.teleport(loc.clone().add(0, 0.5, 0));

					}
				}.runTaskLater(Main.plugin, 1);
			}
			return true;
		}
		return false;
	}
}
