package me.ghost.hg.utils;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SimpleKit {
	public static HashMap<String, SimpleKit> kits = new HashMap<>();
	private ItemStack[] armor;
	private ItemStack[] inv;
	private Collection<PotionEffect> effects;

	public SimpleKit(Player p) {
		ItemStack[] copyInventory = new ItemStack[p.getInventory().getContents().length];
		for (int i = 0; i < p.getInventory().getContents().length; i++) {
			ItemStack item = p.getInventory().getContents()[i];
			if (item == null)
				continue;
			copyInventory[i] = item;
		}
		this.inv = copyInventory;
		ItemStack[] copyArmor = new ItemStack[p.getInventory().getArmorContents().length];
		for (int i = 0; i < p.getInventory().getArmorContents().length; i++) {
			ItemStack item = p.getInventory().getArmorContents()[i];
			if (item == null)
				continue;
			copyArmor[i] = item;
		}
		this.armor = copyArmor;
		this.effects = p.getActivePotionEffects();
	}

	public static void addKit(Player sender, String s, SimpleKit sk) {
		if (kits.containsKey(s)) {
			sender.sendMessage("§cO kit '" + s + "' ja existe");
			return;
		}
		kits.put(s, sk);
		sender.sendMessage("§6Kit '" + s + "' criado com sucesso");
	}

	public static void removeKit(Player sender, String s) {
		if (!kits.containsKey(s)) {
			sender.sendMessage("§cO kit '" + s + "' nao existe");
			return;
		}
		kits.remove(s);
		sender.sendMessage("§6Kit '" + s + "' removido com sucesso");
	}

	@SuppressWarnings("deprecation")
	public static void applyKit(Player sender, String s, Player target) {
		if (!kits.containsKey(s)) {
			sender.sendMessage("§cO kit '" + s + "' nao existe");
			return;
		}
		SimpleKit sk = kits.get(s);
		if (target == null) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p != sender) {
					p.sendMessage("§6Kit '" + s + "' aplicado com sucesso");
				}
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				for (PotionEffect effect : p.getActivePotionEffects()) {
					p.removePotionEffect(effect.getType());
				}
				p.getInventory().setHelmet(sk.getArmor()[0]);
				p.getInventory().setChestplate(sk.getArmor()[1]);
				p.getInventory().setLeggings(sk.getArmor()[2]);
				p.getInventory().setBoots(sk.getArmor()[3]);
				p.getInventory().setArmorContents(sk.getArmor());
				p.getInventory().setContents(sk.getInventory());

				for (PotionEffect effect : sk.getEffects()) {
					p.addPotionEffect(effect);
				}
			}
			sender.sendMessage("§6Kit '" + s + "' aplicado com sucesso para todos os jogadores");
			return;
		}
		target.getInventory().clear();
		target.getInventory().setArmorContents(null);
		for (PotionEffect effect : target.getActivePotionEffects()) {
			target.removePotionEffect(effect.getType());
		}
		target.getInventory().setArmorContents(sk.getArmor());
		target.getInventory().setContents(sk.getInventory());
		for (PotionEffect effect : sk.getEffects()) {
			target.addPotionEffect(effect);
		}
		sender.sendMessage("§6Kit '" + s + "' aplicado com sucesso para " + target.getName());

	}

	public ItemStack[] getInventory() {
		return this.inv;
	}

	public ItemStack[] getArmor() {
		return this.armor;
	}

	public Collection<PotionEffect> getEffects() {
		return this.effects;
	}
}
