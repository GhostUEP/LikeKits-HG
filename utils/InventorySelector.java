package me.ghost.hg.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.KitCategory;
import me.ghost.hg.manager.KitManager;

public class InventorySelector {

	private Player player;
	private KitManager manager;
	private Inventory inventory;
	private KitCategory category;
	private Listener listener;
	private int pagina;
	private int paginaNumbers;
	private SelectorType selectorType;

	private Map<KitCategory, List<Kit>> kits;

	public InventorySelector(Player player, KitManager manager, SelectorType type) {
		this.player = player;
		this.manager = manager;
		this.category = KitCategory.OWNED;
		this.selectorType = type;
		inventory = createInventory();
		pagina = 1;
		loadCategories();
		updatePage();
	}

	public void open() {
		player.openInventory(inventory);
		createListener();
	}

	public void setCategory(KitCategory category) {
		this.category = category;
		pagina = 1;
		setGlass(inventory, category.getId());
		updatePage();
	}

	public void nextPage() {
		if (paginaNumbers >= pagina + 1) {
			pagina += 1;
			updatePage();
		}
	}

	public void previusPage() {
		if (pagina - 1 > 0) {
			pagina -= 1;
			updatePage();
		}
	}

	private void updatePage() {
		for (int i = 18; i < 54; i++) {
			inventory.setItem(i, null);
		}
		paginaNumbers = (kits.get(category).size() / 36) + 1;
		setPages(inventory);
		int i = 18;
		int page = 1;
		if (category == KitCategory.ALL) {
			for (Kit kit : kits.get(category)) {
				if (page < pagina) {
					if (i == 53) {
						i = 17;
						page += 1;
					}
					i++;
					continue;
				}
				if (i > 53)
					break;
				if (kit.getIcon() == null)
					continue;
				ItemStack item = new ItemStack(kit.getIcon());
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§5" + manager.getKitName(kit.getName()));
				String description = "";
				if (manager.hasKit(player, kit.getName(), selectorType)) {
					description = "§6Disponivel. ";
				} else {
					description = "§4Indisponivel. ";
				}
				meta.setLore(wrap(description + kit.getKitInfo().toString().replace("[", "").replace("]", "")));
				item.setItemMeta(meta);
				inventory.setItem(i, item);
				i++;
			}
			return;
		}
		for (Kit kit : kits.get(category)) {
			if (page < pagina) {
				if (i == 53) {
					i = 17;
					page += 1;
				}
				i++;
				continue;
			}
			if (i > 53)
				break;
			if (kit.getIcon() == null)
				continue;
			ItemStack item = new ItemStack(kit.getIcon());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§5" + manager.getKitName(kit.getName()));
			String description = "";
			if (manager.hasKit(player, kit.getName(), selectorType)) {
				description = "§6Disponivel. ";
			} else {
				description = "§4Indisponivel. ";
			}
			meta.setLore(wrap(description + kit.getKitInfo().toString().replace("[", "").replace("]", "")));
			item.setItemMeta(meta);
			inventory.setItem(i, item);
			i++;
		}
	}

	private List<String> wrap(String string) {
		String[] split = string.split(" ");
		string = "";
		String color = "§f";
		ArrayList<String> newString = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			if (string.length() > 20 || string.endsWith(".") || string.endsWith("!")) {
				newString.add(color + string);
				if (string.endsWith(".") || string.endsWith("!"))
					newString.add("");
				string = "";
			}
			string += (string.length() == 0 ? "" : " ") + split[i];
		}
		newString.add(color + string);
		return newString;
	}

	private void loadCategories() {
		kits = new HashMap<>();
		List<Kit> list = kits.get(KitCategory.OWNED);
		if (list == null) {
			list = new ArrayList<Kit>();
		}
		for (Kit kit : manager.getKits().values()) {
			if (manager.hasKit(player, kit.getName(), selectorType))
				list.add(kit);
		}
		Collections.sort(list, new Comparator<Kit>() {
			public int compare(Kit kit1, Kit kit2) {
				return kit1.getName().compareTo(kit2.getName());
			}
		});
		kits.put(KitCategory.OWNED, list);
		list = kits.get(KitCategory.ALL);
		if (list == null) {
			list = new ArrayList<Kit>();
		}
		for (Kit kit : manager.getKits().values()) {
			list.add(kit);
		}
		Collections.sort(list, new Comparator<Kit>() {
			public int compare(Kit kit1, Kit kit2) {
				return kit1.getName().compareTo(kit2.getName());
			}
		});
		kits.put(KitCategory.ALL, list);
	}

	private Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null, 54, "§6Kits - LikeHG");
		setGlass(inventory, category.getId());
		setPages(inventory);
		ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		inventory.setItem(1, vidro);
		inventory.setItem(2, vidro);
		inventory.setItem(6, vidro);
		inventory.setItem(7, vidro);
		ItemStack seuskits = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta seuskitsmeta = seuskits.getItemMeta();
		seuskitsmeta.setDisplayName("§aSeus Kits");
		seuskits.setItemMeta(seuskitsmeta);
		inventory.setItem(3, seuskits);
		ItemStack lojakits = new ItemStack(Material.CHEST);
		ItemMeta lojakitsmeta = lojakits.getItemMeta();
		lojakitsmeta.setDisplayName("§bEscolha seu kit");
		lojakitsmeta.setLore(Arrays.asList("Clique e escolha seu kit!"));
		lojakits.setItemMeta(lojakitsmeta);
		inventory.setItem(4, lojakits);
		ItemStack originais = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta originaismeta = originais.getItemMeta();
		originaismeta.setDisplayName("§cTodos os kits");
		originais.setItemMeta(originaismeta);
		inventory.setItem(5, originais);
		return inventory;
	}

	private void setGlass(Inventory inv, short s) {
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, s);
		ItemMeta glassmeta = glass.getItemMeta();
		glassmeta.setDisplayName(" ");
		glass.setItemMeta(glassmeta);
		for (int i = 9; i < 18; i++) {
			inv.setItem(i, glass);
		}
	}

	private void setPages(Inventory inv) {
		if (pagina <= 1)
			inv.setItem(0, getGray("§cNao possui pagina anterior"));
		else
			inv.setItem(0, getGreen("§aPagina Anterior"));
		if (pagina == paginaNumbers)
			inv.setItem(8, getGray("§cNao possui proxima pagina"));
		else
			inv.setItem(8, getGreen("§aProxima Pagina"));
	}

	private ItemStack getGreen(String name) {
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 10);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(name);
		item.setItemMeta(itemmeta);
		return item;
	}

	private ItemStack getGray(String name) {
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 8);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(name);
		item.setItemMeta(itemmeta);
		return item;
	}

	private void createListener() {
		listener = new Listener() {

			@EventHandler
			public void onClose(InventoryCloseEvent event) {
				if (event.getPlayer() != player)
					return;
				destroy();
			}

			@EventHandler
			public void onInteract(InventoryClickEvent event) {
				if (event.getWhoClicked() != player)
					return;
				if (!event.getInventory().getName().equalsIgnoreCase("§6Kits - LikeHG"))
					return;
				event.setCancelled(true);
				player.updateInventory();
				player.setItemOnCursor(null);
				if (event.getClick().equals(ClickType.RIGHT))
					return;
				ItemStack item = event.getCurrentItem();
				if (item == null)
					return;
				if (!item.hasItemMeta())
					return;
				if (!item.getItemMeta().hasDisplayName())
					return;
				if (item.getItemMeta().getDisplayName().contains("Proxima")) {
					nextPage();
					player.playSound(player.getLocation(), Sound.CLICK, 1.0f, 1.0f);
				}
				if (item.getItemMeta().getDisplayName().contains("Anterior")) {
					previusPage();
					player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
				}
				if (item.getItemMeta().getDisplayName().contains("Todos os kits")) {
					setCategory(KitCategory.ALL);
				}
				if (item.getItemMeta().getDisplayName().contains("Seus Kits")) {
					setCategory(KitCategory.OWNED);
				}
				player.setItemOnCursor(null);
				if (event.getRawSlot() >= 18) {
					manager.setKit(player, ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase(),
							selectorType);
					player.closeInventory();
					destroy();
				}
			}
		};
		Main.plugin.getServer().getPluginManager().registerEvents(listener, Main.plugin);
	}

	private void destroy() {
		HandlerList.unregisterAll(listener);
	}

	public static enum SelectorType {
		FIRSTKIT, SECONDKIT;
	}
}