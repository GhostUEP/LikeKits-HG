package me.ghost.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class TpCMD implements CommandExecutor {

	public Main m;

	public TpCMD(Main m) {
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("teleport")) {
			Player p = (Player) sender;
			if (!m.adm.isSpectating(p)) {
				p.sendMessage("§cVocê não é um espectador");
				return true;
			}
			Player player = (Player) sender;
			if (args.length == 0) {
				p.sendMessage("§cUse: /teleport [Player]");
			} else if (args.length == 1) {
				Player targetPlayer = player.getServer().getPlayer(args[0]);
				if (targetPlayer == null) {
					p.sendMessage("§cO Jogador não existe");
					return true;
				}
				if (m.isNotPlaying(targetPlayer)) {
					p.sendMessage("§cO Jogador não existe");
					return true;
				}
				player.teleport(targetPlayer.getLocation());
			}
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("tp")) {
			if (!m.perm.isTrial(p)) {
				p.sendMessage("§cSem permissao");
				return true;
			}
			if (args.length < 1 || args.length > 4) {
				p.sendMessage("§c/tp (jogador) ou /tp [x] [y] [z]");
				return true;
			}
			if (args.length == 1) {
				Player player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					p.sendMessage("§cJogador offline");
					return true;
				}
				p.teleport(player);
				String seTeleportou = " se teleportou para ";
				for (Player todos : Bukkit.getOnlinePlayers()) {
					if (Main.plugin.perm.isTrial(todos)) {
						todos.sendMessage("§7[§r" + p.getDisplayName() + "§7(" + p.getName() + ")" + seTeleportou + "§r"
								+ player.getDisplayName() + "§7(" + player.getName() + ") ]");
					}
				}
				return true;
			}
			if (args.length == 2) {
				Player player1 = Bukkit.getPlayer(args[0]);
				Player player2 = Bukkit.getPlayer(args[1]);
				if (player1 == null) {
					p.sendMessage("§cO Jogador [1] esta offline!");
					return true;
				}
				if (player2 == null) {
					p.sendMessage("§cO Jogador [2] esta offline!");
					return true;
				}
				player1.teleport(player2);
				for (Player todos : Bukkit.getOnlinePlayers()) {
					if (Main.plugin.perm.isTrial(todos)) {
						todos.sendMessage("§7[" + p.getDisplayName() + "§7(" + p.getName() + ") " + ("teleportou") + " "
								+ player1.getDisplayName() + "§7(" + player1.getName() + ") " + ("para") + " "
								+ player2.getDisplayName() + "§7(" + player2.getName() + ") ]");
					}
				}
				return true;
			}
			if (args.length == 3) {
				if (!isNumeric(args[0]) || !isNumeric(args[1]) || !isNumeric(args[2])) {
					p.sendMessage("§cUma ou mais coordenadas não sao numeros");
					return true;
				}
				int x = Integer.valueOf(args[0]).intValue();
				int y = Integer.valueOf(args[1]).intValue();
				int z = Integer.valueOf(args[2]).intValue();
				p.teleport(p.getWorld().getBlockAt(x, y, z).getLocation());
				for (Player todos : Bukkit.getOnlinePlayers()) {
					if (Main.plugin.perm.isTrial(todos)) {
						todos.sendMessage(
								"§7[" + p.getDisplayName() + "§7(" + p.getName() + ") " + ("se teleportou para") + " "
										+ String.format("(%s,%s,%s)", new Object[] { x, y, z }) + " ]");
					}
				}
				return true;
			}
			if (args.length == 4) {
				Player player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					p.sendMessage("§cJogador offline!");
					return true;
				}
				if (!isNumeric(args[1]) || !isNumeric(args[2]) || !isNumeric(args[3])) {
					p.sendMessage("§c/tp (jogador) [x] [y] [z]");
					return true;
				}
				int x = Integer.valueOf(args[1]).intValue();
				int y = Integer.valueOf(args[2]).intValue();
				int z = Integer.valueOf(args[3]).intValue();
				player.teleport(player.getWorld().getBlockAt(x, y, z).getLocation());
				String teleportou = "teleportou";
				String para = "para";
				for (Player todos : Bukkit.getOnlinePlayers()) {
					if (Main.plugin.perm.isTrial(todos)) {
						todos.sendMessage("§7[§r" + p.getDisplayName() + "(" + p.getName() + ") §7" + teleportou + " §r"
								+ player.getDisplayName() + "(" + player.getName() + ") §7" + para + " "
								+ String.format("(%s,%s,%s)", new Object[] { x, y, z }) + " ]");
					}
				}
				return true;
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("tphere")) {
			if (!m.perm.isTrial(p)) {
				p.sendMessage("§cSem permissao");
				return true;
			}
			if (args.length != 1) {
				p.sendMessage("§c/tphere (jogador)");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				p.sendMessage("§cJogador offline");
				return true;
			}
			player.teleport(p.getLocation());
			player.sendMessage("§6O Jogador " + p.getName() + " teleportou voce para " + p.getName());
			p.sendMessage("§6Você teleportou o jogador " + player.getName() + " para você.");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("tpall")) {
			if (!m.perm.isTrial(p)) {
				p.sendMessage("§cSem permissao");
				return true;
			}
			for (Player ps : Bukkit.getOnlinePlayers()) {
				if (ps != p) {
					ps.teleport(p);
					ps.sendMessage("§b" + p.getName() + " teleportou você para " + p.getName());
				}
			}
			p.sendMessage("§6Você teleportou todos para sua localização");
			return true;
		}
		return false;
	}

	public boolean isNumeric(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
