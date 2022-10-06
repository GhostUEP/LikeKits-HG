package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.utils.SimpleKit;

public class SkitCMD implements CommandExecutor {
	public Main m;

	public SkitCMD(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("skit")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cVocê não é um player");
				return true;
			}
			Player p = (Player) sender;
			if (!m.perm.isTrial(p)) {
				p.sendMessage("§cSem permissao");
				return true;
			}
			if (args.length > 0) {
				if (args.length == 2) {
					String kit = args[1];
					if (args[0].equalsIgnoreCase("create")) {
						SimpleKit.addKit(p, kit, new SimpleKit(p));
						return true;
					}
					if (args[0].equalsIgnoreCase("apply")) {
						SimpleKit.applyKit(p, kit, null);
						return true;
					}
					if (args[0].equalsIgnoreCase("remove")) {
						SimpleKit.removeKit(p, kit);
						return true;
					}
				}
				if (args.length == 3) {
					Player target = Bukkit.getPlayer(args[2]);
					if (target == null) {
						p.sendMessage("§cO jogador não existe");
						return true;
					}
					if (args[0].equalsIgnoreCase("apply")) {
						String kit = args[1];
						SimpleKit.applyKit(p, kit, target);
						return true;
					}

				}
			}
			p.sendMessage("§cUse: /skit [create | apply | remove] [kit] [player]");
			return true;
		}
		return false;
	}
}
