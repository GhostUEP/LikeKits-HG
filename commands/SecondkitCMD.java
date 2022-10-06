package me.ghost.hg.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;

public class SecondkitCMD implements CommandExecutor {
	private Main m;

	public SecondkitCMD(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Você não é um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("second")) {
			if (!m.perm.isAdmin(p)) {
				p.sendMessage("§cSem permissao");
				return true;
			}
			if (m.stage != Estagio.PREGAME) {
				p.sendMessage("§cVocê só pode usar esse comando antes do jogo começar");
				return true;
			}
			if (m.box.invAberto.contains(p.getUniqueId())) {
				p.sendMessage("§cVocê não pode escolher kit enquanto abre uma caixa");
				return true;
			}
			if (args.length == 0) {

				if (m.kitToSecond.containsKey(p.getUniqueId())) {
					m.kitToSecond.remove(p.getUniqueId());
					p.sendMessage("§aVocê removeu seu kit secundário específico");
					return true;
				} else {
					p.sendMessage("§cUse /second [kit]");
					return true;
				}
			} else if (args.length == 1) {
				if (!m.kit.isKit(args[0])) {
					p.sendMessage("§cEsse kit não existe");
					return true;
				}
				if (!m.kit.FIRSTKITS.containsKey(p.getUniqueId())) {
					p.sendMessage("§cEscolha o kit 1 primeiro");
					return true;
				}
				if (m.kit.FIRSTKITS.get(p.getUniqueId()).equalsIgnoreCase(args[0])) {
					p.sendMessage("§cEste já é seu kit primário");
					return true;
				}
				m.kitToSecond.put(p.getUniqueId(), args[0]);
				p.sendMessage("§aVocê setou seu kit secundário");
				return true;
			} else {
				p.sendMessage("§cUse /second [kit]");
				return true;
			}
		}
		return false;
	}
}
