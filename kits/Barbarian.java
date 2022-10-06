package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Barbarian extends KitInterface {

	public Barbarian(Main main) {
		super(main);
	}

	private HashMap<UUID, Integer> kills = new HashMap<UUID, Integer>();

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if ((e.getEntity().getKiller() != null) && (e.getEntity().getKiller() instanceof Player)
				&& (hasAbility(e.getEntity().getKiller()))) {
			Player p = e.getEntity().getKiller();
			if (this.kills.containsKey(p.getUniqueId())) {
				this.kills.put(p.getUniqueId(), this.kills.get(p.getUniqueId()) + 1);
			} else {
				this.kills.put(p.getUniqueId(), 1);
			}
			if (p.getItemInHand() == null)
				return;
			if (p.getItemInHand().getType() == Material.AIR)
				return;
			if (p.getItemInHand().getItemMeta() == null)
				return;
			if (!p.getItemInHand().getItemMeta().getDisplayName().contains("Blody Bane"))
				return;
			switch (this.kills.get(p.getUniqueId())) {
			case 1:
				p.getItemInHand().setType(Material.STONE_SWORD);
				p.getItemInHand().setDurability((short) 0);
				break;
			case 5:
				p.getItemInHand().setType(Material.IRON_SWORD);
				p.getItemInHand().setDurability((short) 0);
				break;
			case 8:
				p.getItemInHand().setType(Material.DIAMOND_SWORD);
				p.getItemInHand().setDurability((short) 0);
				break;
			case 10:
				p.getItemInHand().addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
				p.getItemInHand().setDurability((short) 0);
				break;
			case 12:
				p.getItemInHand().addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
				p.getItemInHand().setDurability((short) 0);
				break;
			}
		}
	}

	@EventHandler
	public void onHit(final EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		Player p = (Player) event.getDamager();
		if (!hasAbility(p))
			return;
		if (p.getItemInHand() != null && p.getItemInHand().hasItemMeta()
				&& p.getItemInHand().getItemMeta().getDisplayName().contains("Blody Bane")) {
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
				public void run() {
					p.getItemInHand().setDurability((short) 0);
				}
			}, 1L);
		}
	}

	@EventHandler
	public void ondrop(PlayerDropItemEvent e) {
		Player p = (Player) e.getPlayer();
		if (!hasAbility(p))
			return;
		if (e.getItemDrop().getItemStack() != null && e.getItemDrop().getItemStack().hasItemMeta()
				&& e.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("Blody Bane")) {
			e.setCancelled(true);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.WOOD_SWORD, 1, "§r§cBlody Bane"));
		ItemStack barbarian = new ItemStack(Material.WOOD_SWORD);
		barbarian.setDurability((short) 45);
		barbarian.addEnchantment(Enchantment.DURABILITY, 1);
		return new Kit("barbarian",
				Arrays.asList(new String[] {
						"Inicie a partida com uma espada que a cada um certo numero de kills ela fica mais forte" }),
				kitItems, null, barbarian);
	}

}
