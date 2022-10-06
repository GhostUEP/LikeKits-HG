package me.ghost.hg.manager;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;

public abstract class KitInterface implements Listener {
	private String kitName;
	private KitManager kitManager;

	private Main m;

	private static KitInterface ki;

	public KitInterface(Main main) {
		this.m = main;
		kitManager = m.kit;
		Kit kit = getKit();
		kitName = kit.name;
		m.kit.addKit(kitName, kit);
	}

	public KitManager getKitManager() {
		return kitManager;
	}

	public String getKitName() {
		return kitName;
	}

	public Main getMain() {
		return m;
	}

	public static KitInterface getKi() {
		return ki;
	}

	public abstract Kit getKit();

	public boolean hasAbility(Player player) {
		return kitManager.hasAbility(player, kitName);
	}

	public Server getServer() {
		return getMain().getServer();
	}

	public void addAntikit() {
		kitManager.addAntikit(kitName);
	}

	public boolean hasAbility(Player player, String kitName) {
		return kitManager.hasAbility(player, kitName);
	}

	public boolean hasAntikit(Player player) {
		return kitManager.hasAntikit(player, kitName);
	}

	public boolean hasAntikit(Player player, String kitName) {
		return kitManager.hasAntikit(player, kitName);
	}

	public boolean isKitItem(ItemStack i, Material material, String name) {
		return ((i != null) && (i.getType() == material) && (i.hasItemMeta()) && (i.getItemMeta().hasDisplayName())
				&& (i.getItemMeta().getDisplayName().equals(name)));
	}

	public boolean isKitItem(ItemStack i, String name) {
		return (i != null && i.getType() != Material.AIR && i.hasItemMeta() && i.getItemMeta().hasDisplayName()
				&& i.getItemMeta().getDisplayName().equals(name));
	}

	public static ItemStack createItem(Material mat, String name) {
		return createItem(mat, 1, (short) 0, name, "");
	}

	public static ItemStack createItem(Material mat, String name, String... description) {
		return createItem(mat, 1, (short) 0, name, description);
	}

	public static ItemStack createItem(Material mat, int quantidade, String name, String... description) {
		return createItem(mat, quantidade, (short) 0, name, description);
	}

	public static ItemStack createItemstack(Material material, int amount, int rawdata, String display) {
		ItemStack itemstack = new ItemStack(material, amount, (short) rawdata);
		ItemMeta meta = itemstack.getItemMeta();
		meta.setDisplayName(display);
		itemstack.setItemMeta(meta);
		return itemstack;
	}

	public static ItemStack createItemstack(Material material, int amount, String display) {
		return createItemstack(material, amount, 0, display);
	}

	public static boolean checkItem(ItemStack item, String display) {
		return (item != null && item.getType() != Material.AIR && item.hasItemMeta()
				&& item.getItemMeta().hasDisplayName()
				&& item.getItemMeta().getDisplayName().equalsIgnoreCase(display));
	}

	public static void setLore(ItemStack item, String... string) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(string));
		item.setItemMeta(meta);
	}

	public static ItemStack createItemStack(String string, Material mat, int i) {
		return createItemstack(mat, i, string);
	}

	public static ItemStack createItemStack(String string, Material stick, int i, Enchantment knockback, int j) {
		ItemStack item = createItemStack(string, stick, i);
		item.addUnsafeEnchantment(knockback, j);
		return item;
	}

	public static ItemStack createItemStack(String string, List<String> asList, Material material, int quantia) {
		ItemStack item = createItemStack(string, material, quantia);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(asList);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItemStack(String string, Material material, int quantia, int data) {
		return createItemstack(material, quantia, data, string);
	}

	public static ItemStack createItem(Material mat, int quantidade, short data, String name, String... description) {
		ItemStack item = new ItemStack(mat, quantidade, data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		if (description.length > 0 && !description[0].isEmpty())
			im.setLore(Arrays.asList(description));
		item.setItemMeta(im);
		return item;
	}
}
