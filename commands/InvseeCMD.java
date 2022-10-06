package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class InvseeCMD implements CommandExecutor {
	public Main m;

	public InvseeCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("invsee")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cVocê não é um player");
				return true;
			}
			Player p = (Player) sender;
			if (!Main.plugin.perm.isTrial(p)) {
				p.sendMessage("§cSem permissao");
				return true;
			}
			if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					p.sendMessage("§cO jogador não existe");
					return true;
				}
				p.openInventory(target.getInventory());
			} else {
				p.sendMessage("§cUse: /invsee [Player]");
				return true;
			}
		}
		return false;
	}
}
