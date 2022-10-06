package me.ghost.hg.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class TellCMD implements CommandExecutor {
	private Main m;
	private HashMap<UUID, List<String>> ignore = new HashMap<>();
	private ArrayList<UUID> ignoreall = new ArrayList<>();

	public TellCMD(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cVoce precisa ser player");
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("tell") || cmd.getName().equalsIgnoreCase("w")
				|| cmd.getName().equalsIgnoreCase("msg")) {
			if (args.length < 2) {
				p.sendMessage("§cUse /tell <Player> <Mensagem>");
				return true;
			}
			if (m.adm.isSpectating(p) && !m.perm.isTrial(p)) {
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				p.sendMessage("§cJogador offline");
				return true;
			}
			if (m.adm.isSpectating(player)) {
				p.sendMessage("§cJogador offline");
				return true;
			}
			if (ignore.containsKey(player.getUniqueId())) {
				if (ignore.get(player.getUniqueId()).contains(p.getName())) {
					p.sendMessage("§cJogador esta te ignorando");
					return true;
				}
			}
			if (ignoreall.contains(player.getUniqueId())) {
				p.sendMessage("§cJogador esta te ignorando");
				return true;
			}
			String mensagem = args.length > 1 ? StringUtils.join(args, ' ', 1, args.length) : null;
			m.reply.put(player, p);
			m.reply.put(p, player);
			String voce = "Voce";
			player.sendMessage("§7[" + ChatColor.stripColor(p.getDisplayName()) + " --> " + voce + "] " + mensagem);
			p.sendMessage("§7[" + voce + " --> " + player.getName() + "] " + mensagem);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("r")) {
			if (args.length == 0) {
				p.sendMessage("§cDigite /r <Mensagem>");
				return true;
			}
			if (m.adm.isSpectating(p) && !m.perm.isTrial(p)) {
				return true;
			}
			if (!m.reply.containsKey(p)) {
				p.sendMessage("§cVoce nao esta em uma conversa");
				return true;
			}
			String mensagem = args.length > 0 ? StringUtils.join(args, ' ', 0, args.length) : null;
			Player player = m.reply.get(p);
			if (!player.isOnline()) {
				p.sendMessage("§cJogador offline");
				return true;
			}
			if (ignore.containsKey(player.getUniqueId())) {
				if (ignore.get(player.getUniqueId()).contains(p.getName())) {
					p.sendMessage("§cJogador esta te ignorando");
					return true;
				}
			}
			if (ignoreall.contains(player.getUniqueId())) {
				p.sendMessage("§cJogador esta te ignorando");
				return true;
			}
			String voce = "Voce";
			player.sendMessage("§7[" + ChatColor.stripColor(p.getDisplayName()) + " --> " + voce + "] " + mensagem);
			p.sendMessage("§7[" + voce + " --> " + player.getName() + "] " + mensagem);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("ignore")) {
			if (args.length == 0) {
				p.sendMessage("§cUse /ignore [Player/all]");
				return true;
			} else if (args.length == 1) {
				if (args[0].toLowerCase().equalsIgnoreCase("all")) {
					if (ignoreall.contains(p.getUniqueId())) {
						ignoreall.remove(p.getUniqueId());
						p.sendMessage("§cVoce deixou de ignorar todo mundo");
						return true;
					} else {
						ignoreall.add(p.getUniqueId());
						p.sendMessage("§cVoce está ignorando todo mundo");
						return true;
					}
				} else {
					Player target = Bukkit.getPlayer(args[0]);
					List<String> lista = new ArrayList<>();
					if (target == null) {
						p.sendMessage("§cJogador offline");
						return true;
					}
					if (ignore.containsKey(p.getUniqueId())) {
						lista = ignore.get(p.getUniqueId());
						if (lista.contains(target.getName())) {
							lista.remove(target.getName());
							ignore.put(p.getUniqueId(), lista);
							p.sendMessage("§cVoce deixou de ignorar: " + target.getDisplayName());
							return true;
						} else {
							lista.add(target.getName());
							ignore.put(p.getUniqueId(), lista);
							p.sendMessage("§cVoce está ignorando: " + target.getDisplayName());
							return true;
						}
					} else {
						lista.add(target.getName());
						ignore.put(p.getUniqueId(), lista);
						p.sendMessage("§cVoce está ignorando: " + target.getDisplayName());
						return true;
					}
				}
			} else {
				p.sendMessage("§cUse /ignore [Player/all]");
				return true;
			}
		}
		return false;
	}
}
