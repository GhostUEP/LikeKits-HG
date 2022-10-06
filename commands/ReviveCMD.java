package me.ghost.hg.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;

public class ReviveCMD implements CommandExecutor {
	private Main m;

	public ReviveCMD(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSó players podem usar /revive");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("revive")) {
			if (!m.perm.isTrial(p)) {
				p.sendMessage("§cSem permissao");
				return true;
			}
			if (m.stage == Estagio.PREGAME || m.stage == Estagio.WINNER) {
				p.sendMessage("§cVocê não pode usar isso agora");
				return true;
			}
			if (!m.isNotPlaying(p)) {
				p.sendMessage("§cVocê não está morto");
				return true;
			}
			if (args.length == 0) {
				if (m.adm.admin.contains(p)) {
					m.adm.setPlayer(p);
				}
				p.setFlying(false);
				p.setGameMode(GameMode.SURVIVAL);
				m.gamers.add(p.getUniqueId());
				m.kit.giveItem(p);
				p.sendMessage("§6Você reviveu sem kit");
				return true;
			} else if (args.length == 1) {
				String kit1 = args[0];
				if (!m.kit.isKit(kit1.toLowerCase())) {
					p.sendMessage("§c" + kit1 + " não é um Kit");
					return true;
				}
				if (m.adm.admin.contains(p)) {
					m.adm.setPlayer(p);
				}
				p.setFlying(false);
				p.setAllowFlight(false);
				p.setGameMode(GameMode.SURVIVAL);
				m.gamers.add(p.getUniqueId());
				m.kit.FIRSTKITS.put(p.getUniqueId(), kit1.toLowerCase());
				m.kit.giveItem(p);
				p.sendMessage("§6Você reviveu com Kit1: " + m.kit.getKitName(kit1));
				return true;
			} else if (args.length == 2) {
				String kit1 = args[0];
				String kit2 = args[1];
				if (!m.kit.isKit(kit1.toLowerCase())) {
					p.sendMessage("§c" + kit1 + " não é um Kit");
					return true;
				}
				if (!m.kit.isKit(kit2.toLowerCase())) {
					p.sendMessage("§c" + kit2 + " não é um Kit");
					return true;
				}
				if (kit1.equalsIgnoreCase(kit2) || kit2.equalsIgnoreCase(kit1)) {
					p.sendMessage("§cEscolha kits diferentes");
					return true;
				}
				if (m.adm.admin.contains(p)) {
					m.adm.setPlayer(p);
				}
				p.setFlying(false);
				p.setAllowFlight(false);
				p.setGameMode(GameMode.SURVIVAL);
				m.gamers.add(p.getUniqueId());
				m.kit.FIRSTKITS.put(p.getUniqueId(), kit1.toLowerCase());
				m.kit.SECONDKITS.put(p.getUniqueId(), kit2.toLowerCase());
				m.kit.giveItem(p);
				p.sendMessage(
						"§6Você reviveu com o Kit1: " + m.kit.getKitName(kit1) + " e Kit2: " + m.kit.getKitName(kit2));
				return true;
			} else if (args.length == 3) {
				String kit1 = args[0];
				String kit2 = args[1];
				String antikit = args[2];
				if (!m.kit.isKit(kit1.toLowerCase())) {
					p.sendMessage("§c" + kit1 + " não é um Kit");
					return true;
				}
				if (!m.kit.isKit(kit2.toLowerCase())) {
					p.sendMessage("§c" + kit2 + " não é um Kit");
					return true;
				}
				if (kit1.equalsIgnoreCase(kit2) || kit2.equalsIgnoreCase(kit1)) {
					p.sendMessage("§cEscolha kits diferentes");
					return true;
				}
				if (!m.kit.isAntikit(antikit.toLowerCase())) {
					p.sendMessage("§c" + antikit + " não é um AntiKit");
					return true;
				}
				if (m.adm.admin.contains(p)) {
					m.adm.setPlayer(p);
				}
				p.setFlying(false);
				p.setAllowFlight(false);
				p.setGameMode(GameMode.SURVIVAL);
				m.gamers.add(p.getUniqueId());
				m.kit.FIRSTKITS.put(p.getUniqueId(), kit1.toLowerCase());
				m.kit.SECONDKITS.put(p.getUniqueId(), kit2.toLowerCase());
				m.kit.ANTIKIT.put(p.getUniqueId(), antikit.toLowerCase());
				m.kit.giveItem(p);
				p.sendMessage("§6Você reviveu com o Kit1: " + m.kit.getKitName(kit1) + ", Kit2: "
						+ m.kit.getKitName(kit2) + " e AntiKit: " + m.kit.getKitName(antikit));
				return true;
			} else {
				p.sendMessage("§cUse /revive [kit1] [kit2] [antikit]");
			}
			return true;
		}
		return false;
	}
}
