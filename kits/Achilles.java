package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Achilles extends KitInterface {

	public Achilles(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamageAchilles(EntityDamageByEntityEvent e) {
		if (((e.getEntity() instanceof Player))) {
			if (e.getDamager() instanceof Player) {
				Player damager = (Player) e.getDamager();
				Player p = (Player) e.getEntity();
				if (hasAbility(p)) {
					if (hasAntikit(damager))
						return;
					if (damager.getItemInHand().getType() == Material.AIR)
						return;
					if (damager.getItemInHand() == null)
						return;
					if (damager.getItemInHand().getType() == Material.GOLD_SWORD) {
						e.setDamage(2);
						damager.sendMessage(
								ChatColor.RED + p.getName() + "Kit Achilles, itens de madeira matam ele mais rápido");
					} else if (damager.getItemInHand().getType() == Material.STONE_SWORD) {
						e.setDamage(2);
						damager.sendMessage(
								ChatColor.RED + p.getName() + "Kit Achilles, itens de madeira matam ele mais rápido");
					} else if (damager.getItemInHand().getType() == Material.IRON_SWORD) {
						e.setDamage(2);
						damager.sendMessage(
								ChatColor.RED + p.getName() + "Kit Achilles, itens de madeira matam ele mais rápido");
					} else if (damager.getItemInHand().getType() == Material.DIAMOND_SWORD) {
						e.setDamage(2);
						damager.sendMessage(
								ChatColor.RED + p.getName() + "Kit Achilles, itens de madeira matam ele mais rápido");
					} else if (damager.getItemInHand().getType().name().contains("WOOD")
							|| damager.getItemInHand().getType() == Material.STICK) {
						e.setDamage(6);
						damager.getItemInHand().setDurability((short) 0);
					}
				}
			}
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("achilles",
				Arrays.asList(new String[] {
						"Quando alguém te bater, você receberá dano mínimo, ao menos que seja por itens de madeira, se for, você receberá dano aumentado" }),
				kitItems, null, new ItemStack(Material.STONE_SWORD));
	}

}
