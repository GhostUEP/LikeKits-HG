package me.ghost.hg.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Status;
import me.ghost.hg.enums.BoxType;

public class BoxManager {
	public Main m;
	private List<WonItem> itemsToWin = new ArrayList<WonItem>();
	public ArrayList<UUID> isOpeningBox = new ArrayList<>();
	public ArrayList<UUID> invAberto = new ArrayList<>();
	public HashMap<UUID, Integer> goldBoxes = new HashMap<>();
	public HashMap<UUID, Integer> diamondBoxes = new HashMap<>();
	public HashMap<UUID, Integer> bronzeBoxes = new HashMap<>();
	public HashMap<UUID, Integer> prataBoxes = new HashMap<>();
	public HashMap<UUID, List<ItemStack>> itemtoGive = new HashMap<>();

	public BoxManager(Main m) {
		this.m = m;
		this.itemsToWin.add(new WonItem(new ItemStack(Material.DIAMOND), 250));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.OBSIDIAN, 6), 20));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.GOLDEN_APPLE), 250));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.MUSHROOM_SOUP, 32), 1));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.TNT, 8), 1));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.EXP_BOTTLE, 5), 1));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.POTION, 2, (short) 8194), 1));
		ItemStack espada = new ItemStack(Material.IRON_SWORD);
		espada.setDurability((short) 150);
		this.itemsToWin.add(new WonItem(espada, 20));
		ItemStack arco = new ItemStack(Material.BOW);
		arco.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		this.itemsToWin.add(new WonItem(arco, 10));
	}

	private class WonItem {

		boolean isItemStack = false;
		int chance = 0;

		ItemStack itemstack = null;

		public WonItem(ItemStack i, int chance) {
			this.isItemStack = true;
			this.itemstack = i;
			this.chance = chance;
		}

		public boolean isItem() {
			return this.isItemStack;
		}

		public ItemStack getItem() {
			return this.itemstack;
		}

		public int getChance() {
			return this.chance;
		}

	}

	public void openBox(Player p, BoxType tipo) {
		Inventory inv;
		ItemStack diamante = new ItemStack(Material.DIAMOND);
		ItemStack ouro = new ItemStack(Material.GOLD_INGOT);
		ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		switch (tipo) {
		case BRONZE:
			if (m.PreGameTimer <= 15) {
				p.sendMessage("§cVocê não pode abrir caixas agora");
				return;
			}
			if (invAberto.contains(p.getUniqueId())) {
				p.sendMessage("§cEspere para abrir outra caixa");
				return;
			}
			if (!bronzeBoxes.containsKey(p.getUniqueId())) {
				p.sendMessage("§cErro ao carregar suas caixas");
				return;
			}
			if (!(bronzeBoxes.get(p.getUniqueId()).intValue() > 0)) {
				p.sendMessage("§cVocê não tem caixas de Bronze");
				return;
			}
			try {
				m.sqlcmd.removePlayerBoxes(p.getUniqueId(), BoxType.BRONZE, 1);
			} catch (SQLException e) {
				p.sendMessage("§cErro ao setar suas caixas, tente novamente mais tarde");
				return;
			}
			bronzeBoxes.put(p.getUniqueId(), bronzeBoxes.get(p.getUniqueId()) - 1);
			inv = Bukkit.createInventory(p, 27, "Caixa - Bronze");
			inv.setItem(0, ouro);
			inv.setItem(1, ouro);
			inv.setItem(2, ouro);
			inv.setItem(3, ouro);
			inv.setItem(4, ouro);
			inv.setItem(5, ouro);
			inv.setItem(6, ouro);
			inv.setItem(7, ouro);
			inv.setItem(8, ouro);
			inv.setItem(9, ouro);
			inv.setItem(17, ouro);
			inv.setItem(18, ouro);
			inv.setItem(19, ouro);
			inv.setItem(20, ouro);
			inv.setItem(21, ouro);
			inv.setItem(22, ouro);
			inv.setItem(23, ouro);
			inv.setItem(24, ouro);
			inv.setItem(25, ouro);
			inv.setItem(26, ouro);
			setVidro(inv, vidro);

			p.openInventory(inv);
			isOpeningBox.add(p.getUniqueId());
			invAberto.add(p.getUniqueId());
			new BukkitRunnable() {

				@Override
				public void run() {
					if (isOpeningBox.contains(p.getUniqueId())) {
						isOpeningBox.remove(p.getUniqueId());
						ItemStack item = inv.getItem(13);
						// m.kit.setSecondKit(p,
						// item.getItemMeta().getDisplayName());
						Integer xp = Integer.valueOf(item.getItemMeta().getDisplayName().replace(" XP", ""));
						if (p != null) {
							Status status = Status.getStatus(p);
							status.addXP(p, xp);
							status.setXp(status.getXp() + xp);
							p.sendMessage("§7Você recebeu: §6" + xp + " XP");
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
						}
						new BukkitRunnable() {

							@Override
							public void run() {
								if (invAberto.contains(p.getUniqueId())) {
									invAberto.remove(p.getUniqueId());
									p.closeInventory();

								}

							}
						}.runTaskLater(Main.plugin, 20 * 3);

					}
				}
			}.runTaskLater(Main.plugin, 20 * 5);
			new BukkitRunnable() {

				@Override
				public void run() {
					if (isOpeningBox.contains(p.getUniqueId())) {
						inv.setItem(13, getRandomXP());
						p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
					} else {
						cancel();
					}

				}
			}.runTaskTimer(Main.plugin, 1L, 3L);
			break;
		case PRATA:
			if (m.PreGameTimer <= 15) {
				p.sendMessage("§cVocê não pode abrir caixas agora");
				return;
			}
			if (invAberto.contains(p.getUniqueId())) {
				p.sendMessage("§cEspere para abrir outra caixa");
				return;
			}
			if (!prataBoxes.containsKey(p.getUniqueId())) {
				p.sendMessage("§cErro ao carregar suas caixas");
				return;
			}
			if (!(prataBoxes.get(p.getUniqueId()).intValue() > 0)) {
				p.sendMessage("§cVocê não tem caixas de Prata");
				return;
			}
			try {
				m.sqlcmd.removePlayerBoxes(p.getUniqueId(), BoxType.PRATA, 1);
			} catch (SQLException e) {
				p.sendMessage("§cErro ao setar suas caixas, tente novamente mais tarde");
				return;
			}
			prataBoxes.put(p.getUniqueId(), prataBoxes.get(p.getUniqueId()) - 1);
			inv = Bukkit.createInventory(p, 27, "Caixa - Prata");
			inv.setItem(0, ouro);
			inv.setItem(1, ouro);
			inv.setItem(2, ouro);
			inv.setItem(3, ouro);
			inv.setItem(4, ouro);
			inv.setItem(5, ouro);
			inv.setItem(6, ouro);
			inv.setItem(7, ouro);
			inv.setItem(8, ouro);
			inv.setItem(9, ouro);
			inv.setItem(17, ouro);
			inv.setItem(18, ouro);
			inv.setItem(19, ouro);
			inv.setItem(20, ouro);
			inv.setItem(21, ouro);
			inv.setItem(22, ouro);
			inv.setItem(23, ouro);
			inv.setItem(24, ouro);
			inv.setItem(25, ouro);
			inv.setItem(26, ouro);
			setVidro(inv, vidro);

			p.openInventory(inv);
			isOpeningBox.add(p.getUniqueId());
			invAberto.add(p.getUniqueId());
			new BukkitRunnable() {

				@Override
				public void run() {
					if (isOpeningBox.contains(p.getUniqueId())) {
						isOpeningBox.remove(p.getUniqueId());
						ItemStack item = inv.getItem(13);
						// m.kit.setSecondKit(p,
						// item.getItemMeta().getDisplayName());
						List<ItemStack> itemtowin = new ArrayList<>();
						itemtowin.add(item);
						if (itemtoGive.containsKey(p.getUniqueId())) {
							for (ItemStack i : itemtoGive.get(p.getUniqueId())) {
								if (i != null) {
									itemtowin.add(i);
								}
							}
							itemtoGive.put(p.getUniqueId(), itemtowin);
						} else {
							itemtoGive.put(p.getUniqueId(), itemtowin);
						}
						if (p != null) {
							p.sendMessage("§7Você recebeu: §6" + item.toString());
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
						}
						new BukkitRunnable() {

							@Override
							public void run() {
								if (invAberto.contains(p.getUniqueId())) {
									invAberto.remove(p.getUniqueId());
									p.closeInventory();

								}

							}
						}.runTaskLater(Main.plugin, 20 * 3);

					}
				}
			}.runTaskLater(Main.plugin, 20 * 5);
			new BukkitRunnable() {

				@Override
				public void run() {
					if (isOpeningBox.contains(p.getUniqueId())) {
						inv.setItem(13, getRandomItem());
						p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
					} else {
						cancel();
					}

				}
			}.runTaskTimer(Main.plugin, 1L, 3L);
			break;
		case OURO:
			if (m.PreGameTimer <= 15) {
				p.sendMessage("§cVocê não pode abrir caixas agora");
				return;
			}
			if (invAberto.contains(p.getUniqueId())) {
				p.sendMessage("§cEspere para abrir outra caixa");
				return;
			}
			if (!goldBoxes.containsKey(p.getUniqueId())) {
				p.sendMessage("§cErro ao carregar suas caixas");
				return;
			}
			if (!(goldBoxes.get(p.getUniqueId()).intValue() > 0)) {
				p.sendMessage("§cVocê não tem caixas de Ouro");
				return;
			}
			try {
				m.sqlcmd.removePlayerBoxes(p.getUniqueId(), BoxType.OURO, 1);
			} catch (SQLException e) {
				p.sendMessage("§cErro ao setar suas caixas, tente novamente mais tarde");
				return;
			}
			goldBoxes.put(p.getUniqueId(), goldBoxes.get(p.getUniqueId()) - 1);
			inv = Bukkit.createInventory(p, 27, "Caixa - Ouro");
			inv.setItem(0, ouro);
			inv.setItem(1, ouro);
			inv.setItem(2, ouro);
			inv.setItem(3, ouro);
			inv.setItem(4, ouro);
			inv.setItem(5, ouro);
			inv.setItem(6, ouro);
			inv.setItem(7, ouro);
			inv.setItem(8, ouro);
			inv.setItem(9, ouro);
			inv.setItem(17, ouro);
			inv.setItem(18, ouro);
			inv.setItem(19, ouro);
			inv.setItem(20, ouro);
			inv.setItem(21, ouro);
			inv.setItem(22, ouro);
			inv.setItem(23, ouro);
			inv.setItem(24, ouro);
			inv.setItem(25, ouro);
			inv.setItem(26, ouro);
			setVidro(inv, vidro);

			p.openInventory(inv);
			isOpeningBox.add(p.getUniqueId());
			invAberto.add(p.getUniqueId());
			new BukkitRunnable() {

				@Override
				public void run() {
					if (isOpeningBox.contains(p.getUniqueId())) {
						isOpeningBox.remove(p.getUniqueId());
						if (m.kitToSecond.containsKey(p.getUniqueId())) {
							ItemStack item = new ItemStack(
									m.kit.items.get(m.kitToSecond.get(p.getUniqueId()).toLowerCase()).icon);
							ItemMeta itemim = item.getItemMeta();
							itemim.setDisplayName(m.kit.getKitName(m.kitToSecond.get(p.getUniqueId()).toLowerCase()));
							item.setItemMeta(itemim);
							inv.setItem(13, item);
						}
						ItemStack item = inv.getItem(13);
						m.kit.setSecondKit(p, item.getItemMeta().getDisplayName());
						if (p != null) {
							p.sendMessage("§7Você recebeu o kit secundário: §6" + item.getItemMeta().getDisplayName());
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
						}
						new BukkitRunnable() {

							@Override
							public void run() {
								if (invAberto.contains(p.getUniqueId())) {
									invAberto.remove(p.getUniqueId());
									p.closeInventory();

								}

							}
						}.runTaskLater(Main.plugin, 20 * 3);

					}
				}
			}.runTaskLater(Main.plugin, 20 * 5);
			new BukkitRunnable() {

				@Override
				public void run() {
					if (isOpeningBox.contains(p.getUniqueId())) {
						inv.setItem(13, getRandomSecondKit(p));
						p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
					} else {
						cancel();
					}

				}
			}.runTaskTimer(Main.plugin, 1L, 3L);
			break;
		case DIAMANTE:
			if (m.PreGameTimer <= 15) {
				p.sendMessage("§cVocê não pode abrir caixas agora");
				return;
			}
			if (invAberto.contains(p.getUniqueId())) {
				p.sendMessage("§cEspere para abrir outra caixa");
				return;
			}
			if (!diamondBoxes.containsKey(p.getUniqueId())) {
				p.sendMessage("§cErro ao carregar suas caixas");
				return;
			}
			if (!(diamondBoxes.get(p.getUniqueId()).intValue() > 0)) {
				p.sendMessage("§cVocê não tem caixas de Diamante");
				return;
			}
			try {
				m.sqlcmd.removePlayerBoxes(p.getUniqueId(), BoxType.DIAMANTE, 1);
			} catch (SQLException e) {
				p.sendMessage("§cErro ao setar suas caixas, tente novamente mais tarde");
				return;
			}
			diamondBoxes.put(p.getUniqueId(), diamondBoxes.get(p.getUniqueId()) - 1);
			inv = Bukkit.createInventory(p, 27, "Caixa - Diamante");
			inv.setItem(0, diamante);
			inv.setItem(1, diamante);
			inv.setItem(2, diamante);
			inv.setItem(3, diamante);
			inv.setItem(4, diamante);
			inv.setItem(5, diamante);
			inv.setItem(6, diamante);
			inv.setItem(7, diamante);
			inv.setItem(8, diamante);
			inv.setItem(9, diamante);
			inv.setItem(17, diamante);
			inv.setItem(18, diamante);
			inv.setItem(19, diamante);
			inv.setItem(20, diamante);
			inv.setItem(21, diamante);
			inv.setItem(22, diamante);
			inv.setItem(23, diamante);
			inv.setItem(24, diamante);
			inv.setItem(25, diamante);
			inv.setItem(26, diamante);
			setVidro(inv, vidro);

			p.openInventory(inv);
			isOpeningBox.add(p.getUniqueId());
			invAberto.add(p.getUniqueId());
			new BukkitRunnable() {

				@Override
				public void run() {
					if (isOpeningBox.contains(p.getUniqueId())) {
						isOpeningBox.remove(p.getUniqueId());
						ItemStack item = inv.getItem(13);
						m.kit.setAntiKit(p, item.getItemMeta().getDisplayName());
						if (p != null) {
							p.sendMessage("§7Você recebeu o Antikit: §6" + item.getItemMeta().getDisplayName());
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
						}
						new BukkitRunnable() {

							@Override
							public void run() {
								if (invAberto.contains(p.getUniqueId())) {
									invAberto.remove(p.getUniqueId());
									p.closeInventory();

								}

							}
						}.runTaskLater(Main.plugin, 20 * 3);

					}
				}
			}.runTaskLater(Main.plugin, 20 * 5);
			new BukkitRunnable() {

				@Override
				public void run() {
					if (isOpeningBox.contains(p.getUniqueId())) {
						inv.setItem(13, getRandom());
						p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
					} else {
						cancel();
					}

				}
			}.runTaskTimer(Main.plugin, 1L, 3L);
			break;
		default:
			break;

		}
	}

	public void openBox(Player p) {
		Inventory inv = Bukkit.createInventory(p, 9, "Caixas");
		ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		// Caixa de Ouro
		ItemStack ouro = new ItemStack(Material.CHEST);
		ItemMeta ourom = ouro.getItemMeta();
		ourom.setDisplayName("§fCaixa de §6Ouro");
		List<String> descricaoouro = new ArrayList<String>();
		descricaoouro.add("Você possui: ");
		descricaoouro.add(goldBoxes.get(p.getUniqueId()) + " caixas de Ouro");
		descricaoouro.add("   ");
		descricaoouro.add("§fEssa caixa te sorteia");
		descricaoouro.add("§falgum Kit secundário para");
		descricaoouro.add("§fusar durante a partida");
		ourom.setLore(descricaoouro);
		ouro.setItemMeta(ourom);
		// Caixa de Diamante
		ItemStack diamante = new ItemStack(Material.CHEST);
		ItemMeta diamantem = diamante.getItemMeta();
		diamantem.setDisplayName("§fCaixa de §bDiamante");
		List<String> descricaodima = new ArrayList<String>();
		descricaodima.add("Você possui: ");
		descricaodima.add(diamondBoxes.get(p.getUniqueId()) + " caixas de Diamante");
		descricaodima.add("   ");
		descricaodima.add("§fEssa caixa te sorteia");
		descricaodima.add("§falgum AntiKit para");
		descricaodima.add("§fusar durante a partida");
		diamantem.setLore(descricaodima);
		diamante.setItemMeta(diamantem);

		// Caixa de Prata
		ItemStack prata = new ItemStack(Material.CHEST);
		ItemMeta pratam = prata.getItemMeta();
		pratam.setDisplayName("§fCaixa de §7Prata");
		List<String> descprata = new ArrayList<String>();
		descprata.add("Você possui: ");
		descprata.add(prataBoxes.get(p.getUniqueId()) + " caixas de Prata");
		descprata.add("   ");
		descprata.add("§fEssa caixa te sorteia");
		descprata.add("§falguns itens para");
		descprata.add("§fusar durante a partida");
		pratam.setLore(descprata);
		prata.setItemMeta(pratam);

		// Caixa de Bronze
		ItemStack bronze = new ItemStack(Material.CHEST);
		ItemMeta bronzem = bronze.getItemMeta();
		bronzem.setDisplayName("§fCaixa de §eBronze");
		List<String> descbronze = new ArrayList<String>();
		descbronze.add("Você possui: ");
		descbronze.add(bronzeBoxes.get(p.getUniqueId()) + " caixas de Bronze");
		descbronze.add("   ");
		descbronze.add("§fEssa caixa te sorteia");
		descbronze.add("§fuma quantidade de xp");
		descbronze.add("§fpara você ganhar");
		bronzem.setLore(descbronze);
		bronze.setItemMeta(bronzem);
		inv.setItem(0, bronze);
		inv.setItem(1, prata);
		inv.setItem(2, ouro);
		inv.setItem(3, diamante);
		setVidro(inv, vidro);
		p.openInventory(inv);

	}

	public void setVidro(Inventory inv, ItemStack vidro) {
		ItemStack[] arrayOfItemStack;
		int vidros = (arrayOfItemStack = inv.getContents()).length;
		for (int metavidros = 0; metavidros < vidros; metavidros++) {
			ItemStack item2 = arrayOfItemStack[metavidros];
			if (item2 == null) {
				inv.setItem(inv.firstEmpty(), vidro);
			}
		}
	}

	public ItemStack getRandom() {
		String kit = m.kit.getViableAntikit();
		char[] stringArray = kit.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		kit = new String(stringArray);
		ItemStack item = new ItemStack(m.kit.items.get(kit.toLowerCase()).icon);
		ItemMeta itemim = item.getItemMeta();
		itemim.setDisplayName(kit);
		item.setItemMeta(itemim);
		return item;

	}

	public ItemStack getRandomItem() {
		WonItem won = getRandomWon();
		if (won.isItem()) {
			return won.getItem();
		}
		return new ItemStack(Material.STAINED_GLASS);
	}

	public ItemStack getRandomXP() {
		Random r = new Random();
		String xps = "100 XP";
		ItemStack xp = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta xpm = xp.getItemMeta();
		if (r.nextInt(9) == 0) {
			xps = "100 XP";
		}
		if (r.nextInt(10) == 1) {
			xps = "200 XP";
		}
		if (r.nextInt(10) == 2) {
			xps = "300 XP";
		}
		if (r.nextInt(10) == 3) {
			xps = "400 XP";
		}
		if (r.nextInt(10) == 4) {
			xps = "500 XP";
		}
		if (r.nextInt(10) == 5) {
			xps = "600 XP";
		}
		if (r.nextInt(10) == 6) {
			xps = "700 XP";
		}
		if (r.nextInt(10) == 7) {
			xps = "800 XP";
		}
		if (r.nextInt(10) == 8) {
			xps = "900 XP";
		}
		if (r.nextInt(10) == 9) {
			xps = "1000 XP";
		}
		xpm.setDisplayName(xps);
		xp.setItemMeta(xpm);
		return xp;
	}

	public WonItem getRandomWon() {
		Random r = new Random();
		Collections.shuffle(itemsToWin, new Random());
		if (itemsToWin.size() == 0)
			return null;
		while (true) {
			Iterator<WonItem> itel = itemsToWin.iterator();
			while (itel.hasNext()) {
				WonItem item = itel.next();
				if (item.getChance() != 0)
					if (r.nextInt(item.getChance()) == 0)
						return item;
			}
		}
	}

	public ItemStack getRandomSecondKit(Player p) {
		String kit = m.kit.getViableSecondKit(p);
		char[] stringArray = kit.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		kit = new String(stringArray);
		ItemStack item = new ItemStack(m.kit.items.get(kit.toLowerCase()).icon);
		ItemMeta itemim = item.getItemMeta();
		itemim.setDisplayName(kit);
		item.setItemMeta(itemim);
		return item;

	}

}
