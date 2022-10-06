package me.ghost.hg.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class GamemodeCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gamemode") || cmd.getName().equalsIgnoreCase("gm")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!Main.plugin.perm.isTrial(p)) {
					p.sendMessage("§cSem permissão");
					return true;
				}
			}
			if (args.length == 0) {
				sender.sendMessage("§cUse /gamemode [gamemode]");
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("0")
						|| args[0].toLowerCase().equalsIgnoreCase("c") || args[0].toLowerCase().equalsIgnoreCase("s")) {
					if (!(sender instanceof Player)) {
						sender.sendMessage("Você não é um player");
						return true;
					}
					Player p = (Player) sender;
					if (args[0].equalsIgnoreCase("1") || args[0].toLowerCase().equalsIgnoreCase("c")) {
						p.setGameMode(GameMode.CREATIVE);
						p.sendMessage("§fModo de jogo alterado para §d§lCRIATIVO");
					} else if (args[0].equalsIgnoreCase("0") || args[0].toLowerCase().equalsIgnoreCase("s")) {
						p.setGameMode(GameMode.SURVIVAL);
						p.sendMessage("§fModo de jogo alterado para §a§lSURVIVAL");
					}
				} else {
					sender.sendMessage("§cUse /gamemode [gamemode]");
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("0")
						|| args[0].toLowerCase().equalsIgnoreCase("c") || args[0].toLowerCase().equalsIgnoreCase("s")) {
					Player target = Main.plugin.getServer().getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage("§cjogador offline");
						return true;
					}
					if (!(target instanceof Player)) {
						sender.sendMessage("§cjogador offline");
						return true;
					}
					if (args[0].equalsIgnoreCase("1") || args[0].toLowerCase().equalsIgnoreCase("c")) {
						if (sender instanceof Player) {
							Player p = (Player) sender;
							if (target == p) {
								p.setGameMode(GameMode.CREATIVE);
								p.sendMessage("§fModo de jogo alterado para §d§lCRIATIVO");
								return true;
							} else {
								target.setGameMode(GameMode.CREATIVE);
								p.sendMessage(
										"§fModo de jogo de " + target.getDisplayName() + " alterado para §d§lCRIATIVO");
								target.sendMessage("§5Seu modo de jogo foi alterado para criativo");
								return true;
							}
						} else {
							target.setGameMode(GameMode.CREATIVE);
							sender.sendMessage(
									"§fModo de jogo de " + target.getDisplayName() + " alterado para §d§lCRIATIVO");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("0") || args[0].toLowerCase().equalsIgnoreCase("s")) {
						if (sender instanceof Player) {
							Player p = (Player) sender;
							if (target == p) {
								p.setGameMode(GameMode.SURVIVAL);
								p.sendMessage("§fModo de jogo alterado para §a§lSURVIVAL");
								return true;
							} else {
								target.setGameMode(GameMode.SURVIVAL);
								p.sendMessage(
										"§fModo de jogo de " + target.getDisplayName() + " alterado para §a§lSURVIVAL");
								target.sendMessage("§5Seu modo de jogo foi alterado para sobrevivencia");
								return true;
							}
						} else {
							target.setGameMode(GameMode.SURVIVAL);
							sender.sendMessage(
									"§fModo de jogo de " + target.getDisplayName() + " alterado para §a§lSURVIVAL");
							return true;
						}
					}
				} else {
					sender.sendMessage("§cUse /gamemode [gamemode]");
					return true;
				}
			} else if (args.length >= 3) {
				sender.sendMessage("§cUse /gamemode [gamemode]");
				return true;
			}
		}
		return false;
	}
}
