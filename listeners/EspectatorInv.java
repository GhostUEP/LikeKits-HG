package me.ghost.hg.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;

public class EspectatorInv implements Listener {

	private Main m;
	private int pagina = 1;
	private int page = 1;
	private int paginaNumbers;
	private Inventory spectate;

	public EspectatorInv(Main m) {
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	public void nextPage(Player p) {
		if (paginaNumbers >= pagina + 1) {
			pagina += 1;
			Player[] players = Bukkit.getOnlinePlayers();
			openSpectateGUI(p, players, 6, new ItemStack(Material.SKULL_ITEM, 1, (byte) 3));
		}
	}

	@SuppressWarnings("deprecation")
	public void previusPage(Player p) {
		if (pagina > 0) {
			Player[] players = Bukkit.getOnlinePlayers();
			openSpectateGUI(p, players, 6, new ItemStack(Material.SKULL_ITEM, 1, (byte) 3));
		}
	}

	private void setPages(Inventory inv) {
		if (pagina > 1)
			inv.setItem(53, getGreen("§aPagina Anterior"));
		if (pagina != paginaNumbers)
			inv.setItem(53, getGreen("§aProxima Pagina"));
	}

	private ItemStack getGreen(String name) {
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 10);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(name);
		item.setItemMeta(itemmeta);
		return item;
	}

	public void openSpectateGUI(Player p, Player[] players, int rows, ItemStack item) {
		ItemStack is = item;
		SkullMeta im = (SkullMeta) is.getItemMeta();
		spectate = Bukkit.createInventory(null, rows * 9, "§6Jogadores vivos.");
		int slot = 0;
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			paginaNumbers = players.length / 54 + 1;
			setPages(spectate);
			if (page < pagina) {
				if (slot == 53) {
					slot = 0;
					page += 1;
					for (int j = 0; j < 53; j++) {
						spectate.setItem(j, null);
					}
				}
			}
			if (slot >= 53) {
				break;
			}
			if (player != p) {
				if (m.isNotPlaying(player))
					continue;
				im.setDisplayName(player.getName());
				ArrayList<String> lore = new ArrayList<>();
				lore.add("§7Kills: §6" + m.getKills(player));
				lore.add("§7Kit: §6" + m.kit.getKitName(m.kit.getPlayerKit(player)));
				lore.add("§7AntiKit: §6" + m.kit.getKitName(m.kit.getPlayerAntiKit(player)));
				lore.add(" ");
				lore.add("§7Clique para ir até o");
				lore.add("§7jogador");
				im.setLore(lore);
				im.setOwner(player.getName());
				is.setItemMeta(im);
				spectate.setItem(slot, is);
				slot++;
				lore.clear();
			}
		}
		p.openInventory(spectate);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item == null)
			return;
		if (item.getType() != Material.CHEST)
			return;
		if (m.stage == Estagio.PREGAME)
			return;
		if (!m.isNotPlaying(p))
			return;
		if (!m.adm.isSpectating(p))
			return;
		Player[] players = Bukkit.getOnlinePlayers();
		event.setCancelled(true);
		openSpectateGUI(p, players, 6, new ItemStack(Material.SKULL_ITEM, 1, (byte) 3));
	}

	@EventHandler
	public void onInteract(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		if (event.getWhoClicked() != p)
			return;
		if (!event.getInventory().getName().equalsIgnoreCase("§6Jogadores vivos."))
			return;
		event.setCancelled(true);
		p.updateInventory();
		p.setItemOnCursor(null);
		ItemStack item = event.getCurrentItem();
		if (item == null)
			return;
		if (event.getClick().isRightClick()) {
			event.setCancelled(true);
			return;
		}
		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasDisplayName())
			return;
		if (item.getItemMeta().getDisplayName().contains("Proxima")) {
			nextPage(p);
		}
		if (item.getItemMeta().getDisplayName().contains("Anterior")) {
			previusPage(p);
		}
		if (event.getRawSlot() >= 0 && event.getRawSlot() < 53) {
			String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
			Player p2 = Bukkit.getPlayerExact(name);
			if (p2 == null) {
				p.sendMessage("§c" + name + " não está online");
				return;
			}
			p.teleport(p2);
			p.sendMessage("§7Voce foi teleportado para " + p2.getDisplayName());
			spectate.clear();
			page = 1;
			pagina = 1;
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (!e.getInventory().getTitle().equalsIgnoreCase("§6Jogadores vivos."))
			return;
		if (pagina > 1) {
			pagina = 1;
			page = 1;
		}
	}

}
