package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class ForcekitCMD implements CommandExecutor {
	private Main m;

	public ForcekitCMD(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("forcekit")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!Main.plugin.perm.isTrial(p)) {
					p.sendMessage("§cSem permissao");
					return true;
				}
			}
			if (args.length == 0) {
				sender.sendMessage("§cUse: /forcekit [Player/All] [Kit]");
				return true;
			} else if (args.length == 1) {
				sender.sendMessage("§cUse: /forcekit [Player/All] [Kit]");
				return true;
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("all")) {
					String all = args[0].toString();
					String kit = args[1].toString();
					if (!m.kit.isKit(kit.toLowerCase())) {
						sender.sendMessage("§cEsse kit não existe");
						return true;
					}
					sender.sendMessage("§aVoce setou o kit " + m.kit.getKitName(kit) + " para todos os jogadores");
					m.kit.forceKit(all.toLowerCase(), kit.toLowerCase());
					return true;
				} else {
					String all = args[0].toString();
					String kit = args[1].toString();
					Player target = Bukkit.getPlayer(all);
					if (target == null) {
						sender.sendMessage("§cO jogador não existe");
						return true;
					}
					if (!m.kit.isKit(kit.toLowerCase())) {
						sender.sendMessage("§cEsse kit não existe");
						return true;
					}
					sender.sendMessage(
							"§aVoce setou o kit " + m.kit.getKitName(kit) + " para o jogador " + target.getName());
					m.kit.forceKit(target.getName(), kit.toLowerCase());
					return true;
				}
			}
		}
		return false;
	}
}
