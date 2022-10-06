package me.ghost.hg.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ghost.hg.Main;

public class AdminCMD implements CommandExecutor, Listener {
	private Main main;
	private ItemStack specSelector;

	public AdminCMD(Main main) {
		this.main = main;
		specSelector = new ItemStack(Material.CHEST);
		ItemMeta im3 = specSelector.getItemMeta();
		im3.setDisplayName("§6Jogadores vivos.");
		specSelector.setItemMeta(im3);
	}

	public boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Você não é um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("admin")) {
			if (!main.perm.isTrial(p)) {
				p.sendMessage("§cSem permissão para esse comando.");
				return true;
			}
			if (main.adm.admin.contains(p)) {
				main.adm.setPlayer(p);
			} else {
				if (!main.isNotPlaying(p)) {
					String playerKit = "§6" + p.getName() + "(" + main.kit.getPlayerKit(p) + ")";
					String cor = "§7";
					main.listener.deathMessage.put(p.getUniqueId(), playerKit + cor + " desistiu do jogo");
					main.listener.deathMessage(playerKit + cor + " desistiu do jogo");
					main.dropItems(p, p.getLocation());
					main.adm.setAdmin(p);
					main.removeGamer(p);
					main.kit.FIRSTKITS.remove(p.getUniqueId());
					main.kit.SECONDKITS.remove(p.getUniqueId());
					main.kit.ANTIKIT.remove(p.getUniqueId());
					main.checkWinner();
				} else {
					main.adm.setAdmin(p);
				}
			}
			return true;
		}
		if (label.equalsIgnoreCase("desisto")) {
			if (!main.perm.isPro(p)) {
				p.sendMessage("§cSem permissão para esse comando.");
				return true;
			}
			if (!main.isNotPlaying(p)) {
				String playerKit = "§6" + p.getName() + "(" + main.kit.getPlayerKit(p) + ")";
				String cor = "§7";
				main.listener.deathMessage.put(p.getUniqueId(), playerKit + cor + " desistiu do jogo");
				main.listener.deathMessage(playerKit + cor + " desistiu do jogo");
				main.dropItems(p, p.getLocation());
				if (main.perm.isTrial(p)) {
					main.adm.setAdmin(p);
				} else {
					main.adm.setYoutuber(p);
					p.getInventory().clear();
					p.getInventory().setItem(0, specSelector);
				}
				main.removeGamer(p);
				main.kit.FIRSTKITS.remove(p.getUniqueId());
				main.kit.SECONDKITS.remove(p.getUniqueId());
				main.kit.ANTIKIT.remove(p.getUniqueId());
				main.checkWinner();
			}
			return true;
		}
		return false;
	}

}
