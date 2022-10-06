package me.ghost.hg.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.constructors.KitDiario;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.events.PlayerSelectKitEvent;
import me.ghost.hg.utils.InventorySelector.SelectorType;
import me.ghost.utils.DateUtils;

public class KitManager {
	public HashMap<UUID, String> FIRSTKITS = new HashMap<>();
	public HashMap<UUID, String> SECONDKITS = new HashMap<>();
	public HashMap<UUID, String> ANTIKIT = new HashMap<>();
	public ArrayList<String> kits = new ArrayList<>();
	public ArrayList<String> antikits = new ArrayList<>();
	public HashMap<String, Kit> items = new HashMap<>();
	public HashMap<UUID, ArrayList<String>> playerKit = new HashMap<>();
	public HashMap<UUID, String> freeKit = new HashMap<>();
	public HashMap<UUID, ArrayList<String>> favoriteKits = new HashMap<>();
	public HashMap<String, ArrayList<String>> freeKits = new HashMap<>();
	private ArrayList<UUID> firstSurprises = new ArrayList<>();
	private ArrayList<UUID> secondSurprises = new ArrayList<>();
	private ArrayList<String> kitsDisabled = new ArrayList<>();
	public boolean kitsHabilitados = true;
	public Main m;

	public KitManager(Main m) {
		this.m = m;
	}

	/*
	 * public void loadFreeKits() { ArrayList<String> ranks = new ArrayList<>();
	 * ranks.add("normal"); ranks.add("vip"); ranks.add("mvp"); try {
	 * Connect.lock.lock(); PreparedStatement stmt = null; ResultSet result =
	 * null; for (String rank : ranks) { stmt =
	 * Main.con.prepareStatement("SELECT * FROM KitRotation WHERE Rank='" + rank
	 * + "';"); result = stmt.executeQuery(); ArrayList<String> kits = new
	 * ArrayList<>(); while (result.next()) {
	 * kits.add(result.getString("Kits").toLowerCase()); } freeKits.put(rank,
	 * kits); } result.close(); stmt.close(); } catch (Exception e) {
	 * e.printStackTrace(); System.out.println("Erro ao carregar kits free"); }
	 * finally { Connect.lock.unlock(); } }
	 */
	public boolean hasAntikit(Player p, String kit) {
		if (ANTIKIT.containsKey(p.getUniqueId())) {
			if (ANTIKIT.get(p.getUniqueId()).equalsIgnoreCase(kit)) {
				return true;
			}
		}
		return false;
	}

	public void addAntikit(String kit) {
		antikits.add(kit.toLowerCase());
	}

	public void setAntiKit(Player p, String kit) {
		ANTIKIT.put(p.getUniqueId(), kit.toLowerCase());
	}

	public void setSecondKit(Player p, String kit) {
		SECONDKITS.put(p.getUniqueId(), kit.toLowerCase());
	}

	public void giveItem(Player p) {
		p.getInventory().clear();
		if (hasFirstKitAbility(p, "surprise")) {
			setSurprise(p, SelectorType.FIRSTKIT);
		}
		if (hasSecondKitAbility(p, "surprise")) {
			setSurprise(p, SelectorType.SECONDKIT);
		}
		for (ItemStack item : getPlayerKitItems(p)) {
			p.getInventory().addItem(item);
		}
		for (ItemStack item : getPlayerKitItemsToDrop(p)) {
			p.getInventory().addItem(item);
		}
		if (p.getInventory().contains(Material.COMPASS))
			return;
		p.getInventory().addItem(new ItemStack(Material.COMPASS));
	}

	public void giveItemBox(Player p) {
		if (m.box.itemtoGive.containsKey(p.getUniqueId())) {
			for (ItemStack i : m.box.itemtoGive.get(p.getUniqueId())) {
				if (i == null)
					continue;
				p.getInventory().addItem(i);
			}
			m.box.itemtoGive.remove(p.getUniqueId());
		}
	}

	public void giveMini(Player p) {
		if (hasFirstKitAbility(p, "surprise")) {
			setSurprise(p, SelectorType.FIRSTKIT);
		}
		if (hasSecondKitAbility(p, "surprise")) {
			setSurprise(p, SelectorType.SECONDKIT);
		}
		for (ItemStack item : getPlayerKitItems(p)) {
			p.getInventory().addItem(item);
		}
		for (ItemStack item : getPlayerKitItemsToDrop(p)) {
			p.getInventory().addItem(item);
		}
	}

	public void disableKitAll() {
		for (String str : kits) {
			kitsDisabled.add(str);
		}
		FIRSTKITS.clear();
		SECONDKITS.clear();
	}

	public void enableKitAll() {
		kitsDisabled.clear();
	}

	public void disableKit(String kitName) {
		kitsDisabled.add(kitName);
		List<UUID> playerToRemove = new ArrayList<>();
		for (Entry<UUID, String> kitEntry : FIRSTKITS.entrySet()) {
			if (kitEntry.getValue().equalsIgnoreCase(kitName))
				playerToRemove.add(kitEntry.getKey());
		}
		for (UUID uuid : playerToRemove) {
			FIRSTKITS.remove(uuid);
		}
		playerToRemove = new ArrayList<>();
		for (Entry<UUID, String> kitEntry : SECONDKITS.entrySet()) {
			if (kitEntry.getValue().equalsIgnoreCase(kitName))
				playerToRemove.add(kitEntry.getKey());
		}
		for (UUID uuid : playerToRemove) {
			SECONDKITS.remove(uuid);
		}
	}

	public void enableKit(String kitName) {
		kitsDisabled.remove(kitName);
	}

	public HashMap<String, Kit> getKits() {
		HashMap<String, Kit> map = new HashMap<>();
		for (Entry<String, Kit> kitEntry : items.entrySet()) {
			if (kitsDisabled.contains(kitEntry.getKey()))
				continue;
			map.put(kitEntry.getKey(), kitEntry.getValue());
		}
		return map;
	}

	public void setKit(Player p, String kit) {
		/*
		 * if (hasFirstKit(p)) { if (hasSecondKit(p)) {
		 * p.sendMessage(ChatColor.RED + "Você já escolheu dois kits."); return;
		 * } else { setKit(p, kit, SelectorType.SECONDKIT); return; } }
		 */
		setKit(p, kit, SelectorType.FIRSTKIT);
	}

	public void setKit(Player p, String kit, SelectorType type) {
		if (!isKit(kit)) {
			p.sendMessage("§c" + getKitName(kit) + " não é um kit!");
			return;
		}
		if (m.box.invAberto.contains(p.getUniqueId())) {
			p.sendMessage("§cVocê não pode escolher kit enquanto abre uma caixa");
			return;
		}
		if ((m.stage != Estagio.PREGAME && !m.perm.isMvp(p)) || m.GameTimer > 300) {
			p.sendMessage("§cO torneio já começou");
			return;
		}
		if (kitsDisabled.contains(kit)) {
			p.sendMessage("§cO kit " + getKitName(kit) + " esta desativado nesse jogo");
			return;
		}
		if (!hasKit(p, kit, type)) {
			p.sendMessage("§cVocê não pussui o kit " + getKitName(kit));
			String info = "";
			if (items.get(kit) != null) {
				if (!items.get(kit).kitInfo.isEmpty()) {
					String str = "";
					for (String s : items.get(kit).kitInfo) {
						str = str + " " + s;
					}
					info = "Descrição do kit:" + str;
				} else {
					info = "§7Use /kit para escolher outros kits";
				}
			}
			if (!info.isEmpty())
				p.sendMessage("§c" + info);
			p.sendMessage("§fCompre esse kit em: §6" + m.site);
			return;
		}
		if (m.stage != Estagio.PREGAME && (type == SelectorType.FIRSTKIT && FIRSTKITS.containsKey(p.getUniqueId()))) {
			p.sendMessage("§cO torneio já começou.");
			return;
		}
		if (FIRSTKITS.containsKey(p.getUniqueId()) && FIRSTKITS.get(p.getUniqueId()).equalsIgnoreCase(kit)) {
			p.sendMessage("§cVocê já esta com esse kit.");
			return;
		}
		if (SECONDKITS.containsKey(p.getUniqueId()) && SECONDKITS.get(p.getUniqueId()).equalsIgnoreCase(kit)) {
			p.sendMessage("§cVocê já esta com esse kit.");
			return;
		}
		if (m.isNotPlaying(p) && m.stage != Estagio.PREGAME) {
			p.sendMessage("§cO torneio já começou.");
			return;
		}

		p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
		p.sendMessage("§6Você escolheu o kit " + getKitName(kit) + " com sucesso");
		String info = "";
		if (items.get(kit) != null) {
			if (!items.get(kit).kitInfo.isEmpty()) {
				String str = "";
				for (String s : items.get(kit).kitInfo) {
					str = str + " " + s;
				}
				info = "Descrição do kit:§7" + str + "\n§6Use /kit para escolher outro kit.";
			} else {
				info = "§7Use /kit para escolher outros kits";
			}
		}
		if (!info.isEmpty())
			p.sendMessage("§c" + info);
		if (type == SelectorType.FIRSTKIT) {
			FIRSTKITS.put(p.getUniqueId(), kit);
		} else {
			SECONDKITS.put(p.getUniqueId(), kit);
		}
		Bukkit.getPluginManager().callEvent(new PlayerSelectKitEvent(p, kit));
		if (m.stage != Estagio.PREGAME) {
			giveMini(p);
		}
	}

	public boolean hasAbility(Player p, String kitName) {
		return kitsHabilitados && (hasFirstKitAbility(p, kitName) || hasSecondKitAbility(p, kitName));
	}

	public boolean hasFirstKitAbility(Player p, String kitName) {
		return FIRSTKITS.containsKey(p.getUniqueId()) && FIRSTKITS.get(p.getUniqueId()).equalsIgnoreCase(kitName);
	}

	public boolean hasFirstKit(Player p) {
		return FIRSTKITS.containsKey(p.getUniqueId());
	}

	public boolean hasSecondKitAbility(Player p, String kitName) {
		return SECONDKITS.containsKey(p.getUniqueId()) && SECONDKITS.get(p.getUniqueId()).equalsIgnoreCase(kitName);
	}

	public boolean hasSecondKit(Player p) {
		return SECONDKITS.containsKey(p.getUniqueId());
	}

	public boolean hasKit(Player p, String kit, SelectorType type) {
		if (m.perm.isPro(p))
			return true;
		if (type == SelectorType.FIRSTKIT) {
			if (getKitDiário(p).toLowerCase().equalsIgnoreCase(kit.toLowerCase())) {
				return true;
			}
			if (m.perm.playerKits.containsKey(p.getUniqueId())) {
				for (String i : m.perm.playerKits.get(p.getUniqueId())) {
					if (i.toLowerCase().replace("kit.", "").equals(kit.toLowerCase())
							|| i.toLowerCase().equalsIgnoreCase("kit.*")) {
						return true;
					}
				}
			}
		} else if (type == SelectorType.SECONDKIT) {

		}
		return false;
	}

	public void addKit(String kit, Kit k) {
		kits.add(kit.toLowerCase());
		items.put(kit.toLowerCase(), k);
	}

	public void setKitDiário(Player p) {
		if (!getKitDiário(p).toLowerCase().equalsIgnoreCase("nenhum")) {
			p.sendMessage("§cVocê já está com um kit diário, espere pelo menos um dia para pegar outro");
			return;
		}
		long expiresCheck;
		try {
			expiresCheck = DateUtils.parseDateDiff("1d", true);
		} catch (Exception e1) {
			p.sendMessage("§cFormato invalido");
			return;
		}
		String kit = getViableKitDiario(p);
		if (kit == null) {
			p.sendMessage("§cVocê já tem todos os kits");
			return;
		}
		try {
			m.sqlcmd.addExpire(p.getUniqueId(), kit.toLowerCase(), expiresCheck);
		} catch (Exception e) {
			e.printStackTrace();
			p.sendMessage("§cErro ao abrir seu kit Diario");
			return;
		}
		p.sendMessage("§aVocê ganhou o kit: " + getKitName(kit) + " por 1 dia.");
		ItemStack item = new ItemStack(Material.ENDER_CHEST);
		ItemMeta itemm = item.getItemMeta();
		itemm.setDisplayName("§6Kit Diario : " + getKitName(kit));
		item.setItemMeta(itemm);
		p.getInventory().setItem(8, item);
	}

	public String getKitDiário(Player p) {
		KitDiario kit = m.sqlcmd.getKitDiario(p.getUniqueId());
		if (kit == null) {
			return "Nenhum";
		}
		return kit.getKit();
	}

	public void printKitChat(Player player) {

		List<String> yourkits = new ArrayList<String>();
		List<String> otherkits = new ArrayList<String>();
		for (String name : kits) {
			if (kitsDisabled.contains(name))
				continue;
			char[] stringArray = name.toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			name = new String(stringArray);
			if (hasKit(player, name, SelectorType.FIRSTKIT)) {
				yourkits.add(name);
			} else {
				otherkits.add(name);
			}
		}

		Collections.sort(yourkits, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(otherkits, String.CASE_INSENSITIVE_ORDER);
		String list = StringUtils.join(yourkits, ", ");
		String list2 = StringUtils.join(otherkits, ", ");
		if (kitsHabilitados == true) {
			player.sendMessage("§6§lPara escolher um kit use /kit [Kit]");
			if (list.isEmpty()) {
				player.sendMessage("§fSeus kits: §cErro ao carregar seus kits!");
			} else {
				player.sendMessage("§fSeus kits: §7" + list);
			}
			if (list2.isEmpty()) {
				player.sendMessage("§fOutros Kits: §cVocê tem todos os kits!");
			} else {
				player.sendMessage("§fOutros Kits: §c" + list2);
			}
			player.sendMessage("§fPara comprar kits acesse: §6" + m.site);
		} else {
			player.sendMessage("§6§lPara escolher um kit use /kit [Kit]");
			if (list.isEmpty()) {
				player.sendMessage("§fSeus kits: §cDesativados");
			} else {
				player.sendMessage("§fSeus kits: §cDesativados");
			}
			if (list2.isEmpty()) {
				player.sendMessage("§fOutros Kits: §aVocê tem todos os kits!");
			} else {
				player.sendMessage("§fOutros Kits: §c" + list2);
			}
			player.sendMessage("§fPara comprar kits acesse: §6" + m.site);
		}
	}

	public boolean isKit(String kit) {
		return kits.contains(kit.toLowerCase());
	}

	public boolean isAntikit(String kit) {
		return antikits.contains(kit.toLowerCase());
	}

	public String getKitName(String kit) {
		char[] stringArray = kit.toLowerCase().toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		return new String(stringArray);
	}

	public void setSurprise(Player player, SelectorType type) {
		String kit = getViableKit();
		if (type == SelectorType.FIRSTKIT) {
			firstSurprises.add(player.getUniqueId());
			FIRSTKITS.put(player.getUniqueId(), kit);
		} else {
			secondSurprises.add(player.getUniqueId());
			SECONDKITS.put(player.getUniqueId(), kit);
		}
		player.sendMessage("§6Seu surprise é: §7" + getKitName(kit) + "§6.");
		String info = "";
		if (items.get(kit) != null) {
			if (!items.get(kit).kitInfo.isEmpty()) {
				String str = "";
				for (String s : items.get(kit).kitInfo) {
					str = str + " " + s;
				}
				info = "Descrição do kit:§7" + str;
			} else {
				info = "§7Use /kit para escolher outros kits";
			}
		}
		if (!info.isEmpty())
			player.sendMessage("§c" + info);
	}

	public String getViableKit() {
		List<String> kits = new ArrayList<>();
		for (String kit : this.kits) {
			if (kitsDisabled.contains(kit))
				continue;
			if (kit.equals("surprise"))
				continue;
			kits.add(kit);
		}
		if (kits.size() > 0)
			return (String) kits.get(new Random().nextInt(kits.size()));
		return null;
	}

	public String getViableKitDiario(Player p) {
		List<String> kits = new ArrayList<>();
		for (String kit : this.kits) {
			if (hasKit(p, kit.toLowerCase(), SelectorType.FIRSTKIT))
				continue;
			kits.add(kit);
		}
		if (kits.size() > 0)
			return (String) kits.get(new Random().nextInt(kits.size()));
		return null;
	}

	public String getViableSecondKit() {
		List<String> kits = new ArrayList<>();
		for (String kit : this.kits) {
			if (kitsDisabled.contains(kit))
				continue;
			kits.add(kit);
		}
		if (kits.size() > 0)
			return (String) kits.get(new Random().nextInt(kits.size()));
		return null;
	}

	public String getViableSecondKit(Player p) {
		List<String> kits = new ArrayList<>();
		for (String kit : this.kits) {
			if (kitsDisabled.contains(kit))
				continue;
			if (FIRSTKITS.containsKey(p.getUniqueId())) {
				if (kit.equalsIgnoreCase(FIRSTKITS.get(p.getUniqueId()))) {
					continue;
				}
			}
			kits.add(kit);
		}
		if (kits.size() > 0)
			return (String) kits.get(new Random().nextInt(kits.size()));
		return null;
	}

	public String getViableAntikit() {
		List<String> kits = new ArrayList<>();
		for (String kit : this.antikits) {
			if (kitsDisabled.contains(kit))
				continue;
			kits.add(kit);
		}
		if (kits.size() > 0)
			return (String) kits.get(new Random().nextInt(kits.size()));
		return null;
	}

	public String getPlayerKit(Player p) {
		String firstKit = FIRSTKITS.get(p.getUniqueId());
		String secondKit = SECONDKITS.get(p.getUniqueId());
		String kitName = "";
		if (firstKit != null) {
			if (firstSurprises.contains(p.getUniqueId())) {
				kitName = kitName + "Surprise ";
			}
			char[] stringArray = firstKit.toLowerCase().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			if (secondKit != null) {
				kitName = kitName + new String(stringArray) + " e ";
			} else {
				kitName = kitName + new String(stringArray);
			}
		}
		if (secondKit != null) {
			if (secondSurprises.contains(p.getUniqueId())) {
				kitName = kitName + "Surprise ";
			}
			char[] stringArray = secondKit.toLowerCase().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitName = kitName + new String(stringArray);
		}
		if (kitName.isEmpty()) {
			return "Nenhum";
		}
		return kitName;
	}

	public String getPlayerKit1(Player p) {
		String firstKit = FIRSTKITS.get(p.getUniqueId());
		String kitName = "";
		if (firstKit != null) {
			if (firstSurprises.contains(p.getUniqueId())) {
				kitName = kitName + "Surprise ";
			}
			char[] stringArray = firstKit.toLowerCase().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitName = kitName + new String(stringArray);
		}
		if (kitName.isEmpty()) {
			return "Nenhum";
		}
		return kitName;
	}

	public String getPlayerKit2(Player p) {
		String secondKit = SECONDKITS.get(p.getUniqueId());
		String kitName = "";
		if (secondKit != null) {
			if (secondSurprises.contains(p.getUniqueId())) {
				kitName = kitName + "Surprise ";
			}
			char[] stringArray = secondKit.toLowerCase().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitName = kitName + new String(stringArray);
		}
		if (kitName.isEmpty()) {
			return "Nenhum";
		}
		return kitName;
	}

	public String getPlayerKit1noSurprise(Player p) {
		String firstKit = FIRSTKITS.get(p.getUniqueId());
		String kitName = "";
		if (firstKit != null) {
			char[] stringArray = firstKit.toLowerCase().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitName = kitName + new String(stringArray);
		}
		if (kitName.isEmpty()) {
			return "Nenhum";
		}
		return kitName;
	}

	public String getPlayerKit2NoSurprise(Player p) {
		String secondKit = SECONDKITS.get(p.getUniqueId());
		String kitName = "";
		if (secondKit != null) {
			char[] stringArray = secondKit.toLowerCase().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitName = kitName + new String(stringArray);
		}
		if (kitName.isEmpty()) {
			return "Nenhum";
		}
		return kitName;
	}

	@SuppressWarnings("deprecation")
	public void forceKit(String player, String kit) {
		if (player.equalsIgnoreCase("all")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (m.isNotPlaying(p) && m.stage != Estagio.PREGAME)
					continue;
				if (p != null) {
					if (m.stage != Estagio.PREGAME) {
						for (ItemStack i : p.getInventory().getContents()) {
							if (i != null) {
								if (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
									i.setType(Material.AIR);
								}
							}
						}
					}
					FIRSTKITS.put(p.getUniqueId(), kit);
					if (m.stage != Estagio.PREGAME) {
						giveMini(p);
					}
				}
			}
		} else {
			Player p = Bukkit.getPlayer(player);
			if (p != null) {
				if (m.stage != Estagio.PREGAME) {
					for (ItemStack i : p.getInventory().getContents()) {
						if (i != null) {
							if (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
								i.setType(Material.AIR);
							}
						}
					}
				}
				p.updateInventory();
				FIRSTKITS.put(p.getUniqueId(), kit);
				if (m.stage != Estagio.PREGAME) {
					giveMini(p);
				}
			}
		}
	}

	public String getPlayerAntiKit(Player p) {
		String secondKit = ANTIKIT.get(p.getUniqueId());
		String kitName = "";
		if (secondKit != null) {
			char[] stringArray = secondKit.toLowerCase().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitName = kitName + new String(stringArray);
		}
		if (kitName.isEmpty()) {
			return "Nenhum";
		}
		return kitName;
	}

	public ArrayList<ItemStack> getPlayerKitItems(Player p) {
		ArrayList<ItemStack> kitItems = new ArrayList<>();
		if (FIRSTKITS.containsKey(p.getUniqueId())) {
			String name = FIRSTKITS.get(p.getUniqueId()).toLowerCase();
			if (items.get(name) != null) {
				if (items.get(name).items != null) {
					for (ItemStack i : items.get(name).items) {
						kitItems.add(i);
					}
				}
			}
		}
		if (SECONDKITS.containsKey(p.getUniqueId())) {
			String name = SECONDKITS.get(p.getUniqueId()).toLowerCase();
			if (items.get(name) != null) {
				if (items.get(name).items != null) {
					for (ItemStack i : items.get(name).items) {
						kitItems.add(i);
					}
				}
			}
		}
		return kitItems;
	}

	public ArrayList<ItemStack> getPlayerKitItemsToDrop(Player p) {
		ArrayList<ItemStack> kitItems = new ArrayList<>();
		if (FIRSTKITS.containsKey(p.getUniqueId())) {
			String name = FIRSTKITS.get(p.getUniqueId()).toLowerCase();
			if (items.get(name) != null) {
				if (items.get(name).itemstodrop != null) {
					for (ItemStack i : items.get(name).itemstodrop) {
						kitItems.add(i);
					}
				}
			}
		}
		if (SECONDKITS.containsKey(p.getUniqueId())) {
			String name = SECONDKITS.get(p.getUniqueId()).toLowerCase();
			if (items.get(name) != null) {
				if (items.get(name).itemstodrop != null) {
					for (ItemStack i : items.get(name).itemstodrop) {
						kitItems.add(i);
					}
				}
			}
		}
		return kitItems;
	}
}