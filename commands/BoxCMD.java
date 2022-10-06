package me.ghost.hg.commands;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.enums.BoxType;
import me.ghost.utils.UUIDFetcher;

public class BoxCMD implements CommandExecutor {
	private Main m;

	public BoxCMD(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("givebox")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!m.perm.isAdmin(p)) {
					p.sendMessage("§cSem permissao");
					return true;
				}
			}
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (m.box.invAberto.contains(p.getUniqueId())) {
					p.sendMessage("§cVocê não pode usar esse comando enquanto abre uma caixa");
					return true;
				}
			}
			if (args.length == 0) {
				sender.sendMessage("§cUse /givebox [player] [Tipo] [quantidade]");
				return true;
			} else if (args.length == 1) {
				sender.sendMessage("§cUse /givebox [player] [Tipo] [quantidade]");
				return true;
			} else if (args.length == 2) {
				sender.sendMessage("§cUse /givebox [player] [Tipo] [quantidade]");
				return true;
			} else if (args.length == 3) {
				if (args[0].length() > 16) {
					sender.sendMessage("§cNome longo, use até 16 letras");
					return true;
				}
				BoxType caixa = null;
				int quantidade = 0;
				try {
					caixa = BoxType.valueOf(args[1].toUpperCase());
				} catch (Exception e) {
					sender.sendMessage("§cEsse tipo de caixa nao existe");
					return true;
				}
				if (caixa == null) {
					sender.sendMessage("§cEsse tipo de caixa nao existe");
					return true;
				}
				try {
					quantidade = Integer.valueOf(args[2]);
				} catch (Exception e) {
					sender.sendMessage("§cQuantidade inválida");
					return true;
				}
				if (!(quantidade > 0)) {
					sender.sendMessage("§cQuantidade inválida");
					return true;
				}
				Player target = Bukkit.getPlayer(args[0]);
				BoxType tipocaixa = caixa;
				int qnt = quantidade;
				new BukkitRunnable() {

					@Override
					public void run() {
						UUID uuid = null;
						if (target != null) {
							uuid = target.getUniqueId();
						} else {
							try {
								uuid = UUIDFetcher.getUUIDOf(args[0]);
							} catch (Exception e) {
							}
						}
						if (uuid == null) {
							sender.sendMessage("§cJogador não existe!");
							return;
						}
						try {
							m.sqlcmd.addPlayerBoxes(uuid, tipocaixa, qnt);
						} catch (SQLException e) {
							sender.sendMessage("§cErro ao setar as caixas!");
							return;
						}

						sender.sendMessage("§aSetado com sucesso!");
						return;

					}
				}.runTaskAsynchronously(m);
				return true;
			} else {
				sender.sendMessage("§cUse /givebox [player] [Tipo] [quantidade]");
				return true;
			}
		}
		return false;
	}
}
