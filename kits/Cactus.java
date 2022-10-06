package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.events.PlayerSelectKitEvent;
import me.ghost.hg.events.PlayerStartGameEvent;
import me.ghost.hg.manager.KitInterface;

public class Cactus extends KitInterface {

	public Cactus(Main main) {
		super(main);
	}

	@EventHandler
	public void onGamestart(PlayerStartGameEvent e) {
		Player p = e.getPlayer();
		if (hasAbility(p)) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					p.getInventory().setHelmet(getCactusHelmet());
					
				}
			};
		}
	}

	@EventHandler
	public void onGamestart(PlayerSelectKitEvent e) {
		Player p = e.getPlayer();
		String kit = e.getKit();
		if (kit.equalsIgnoreCase("cactus")) {
			if (Main.plugin.stage != Estagio.PREGAME) {
				p.getInventory().setHelmet(getCactusHelmet());
			}
		}
	}

	private ItemStack getCactusHelmet() {
		ItemStack i = new ItemStack(Material.CACTUS);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("§r§2Cactus Head");
		i.setItemMeta(im);
		i.addUnsafeEnchantment(Enchantment.THORNS, 2);
		return i;
	}

	@EventHandler
	public void click(InventoryClickEvent e) {
		if (hasAbility((Player) e.getWhoClicked())) {
			if (isKitItem(e.getCurrentItem(), Material.CACTUS, "§r§2Cactus Head")) {
				e.setCancelled(true);
			}
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("cactus",
				Arrays.asList(new String[] {
						"Sua cabeça é um cacto, sempre que alguem lhe da um hit, tem uma chance deste jogador receber uma parte do dano" }),
				kitItems, null, new ItemStack(Material.CACTUS));
	}

}
