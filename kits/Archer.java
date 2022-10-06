package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.KitInterface;

public class Archer extends KitInterface {

	public Archer(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!hasAbility(event.getPlayer())) {
			return;
		}
		if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
			return;
		}
		if (event.getItem().getType() != Material.BOW || !event.getItem().hasItemMeta()
				|| !event.getItem().getItemMeta().hasDisplayName()) {
			return;
		}
		ItemStack i = event.getItem();
		Player p = event.getPlayer();
		String display = i.getItemMeta().getDisplayName();
		if (display.contains("§a")) {
			p.setItemInHand(createItem(Material.BOW, 1, "§9§lArcher - §bLentidao"));
		} else if (display.contains("§b")) {
			p.setItemInHand(createItem(Material.BOW, 1, "§9§lArcher - §cFogo"));
		} else if (display.contains("§c")) {
			p.setItemInHand(createItem(Material.BOW, 1, "§9§lArcher - §4Mais Dano"));
		} else if (display.contains("§4")) {
			p.setItemInHand(createItem(Material.BOW, 1, "§9§lArcher - §aVeneno"));
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void launch(ProjectileLaunchEvent event) {
		if (!(event.getEntity() instanceof Arrow)) {
			return;
		}
		Arrow arrow = (Arrow) event.getEntity();
		if (!(arrow.getShooter() instanceof Player)) {
			return;
		}
		final Player p = (Player) arrow.getShooter();
		if (!hasAbility(p)) {
			return;
		}
		if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
			return;
		}
		if (p.getItemInHand().getType() != Material.BOW || !p.getItemInHand().hasItemMeta()
				|| !p.getItemInHand().getItemMeta().hasDisplayName()) {
			return;
		}
		ItemStack i = p.getItemInHand();
		String display = i.getItemMeta().getDisplayName();
		if (display.contains("§a")) {
			arrow.setMetadata("veneno", new FixedMetadataValue(Main.plugin, 4.0D));
		} else if (display.contains("§b")) {
			arrow.setMetadata("lentidao", new FixedMetadataValue(Main.plugin, 4.0D));
		} else if (display.contains("§c")) {
			arrow.setMetadata("fogo", new FixedMetadataValue(Main.plugin, 4.0D));
		} else if (display.contains("§4")) {
			arrow.setMetadata("dano", new FixedMetadataValue(Main.plugin, 7.0D));
		}
		new BukkitRunnable() {

			@Override
			public void run() {
				p.getItemInHand().setDurability((short) 0);
			}
		}.runTaskLater(Main.plugin, 1L);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (Main.plugin.stage != Estagio.GAMETIME) {
			return;
		}
		if (e.getDamager() instanceof Arrow && e.getEntity() instanceof LivingEntity) {
			Arrow a = (Arrow) e.getDamager();
			if (!(a.getShooter() instanceof Player)) {
				return;
			}
			Player p = (Player) a.getShooter();
			if (!hasAbility(p)) {
				return;
			}
			if (e.getEntity() instanceof Player) {
				Player tomou = (Player) e.getEntity();
				if (hasAntikit(tomou)) {
					p.sendMessage(Main.plugin.antikit_message);
					return;
				}
			}

			if (p.isOnline()) {
				p.getInventory().addItem(new ItemStack(Material.ARROW));
			}
			if (a.hasMetadata("veneno")) {
				e.setDamage((double) a.getMetadata("veneno").get(0).value());
				((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 90, 0));
			} else if (a.hasMetadata("lentidao")) {
				e.setDamage((double) a.getMetadata("lentidao").get(0).value());
				((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90, 0));
			}
			if (a.hasMetadata("fogo")) {
				e.setDamage((double) a.getMetadata("fogo").get(0).value());
				e.getEntity().setFireTicks(e.getEntity().getFireTicks() + 100);
			}
			if (a.hasMetadata("dano")) {
				e.setDamage((double) a.getMetadata("dano").get(0).value());
			}
		}
	}

	@EventHandler
	public void ondrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (!hasAbility(p))
			return;
		if (e.getItemDrop().getItemStack().getType() != Material.BOW)
			return;
		if (e.getItemDrop().getItemStack().getItemMeta() == null)
			return;
		if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("Archer")) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.BOW, 1, "§9§lArcher - §aVeneno"));
		kitItems.add(new ItemStack(Material.ARROW, 20));
		return new Kit("archer",
				Arrays.asList(new String[] {
						"Inicie a partida com um arco que pode ser alternado e 20 flechas, ao acertar um player você ganha uma flecha, para trocar o efeito do arco clique com botao esquerdo" }),
				kitItems, null, new ItemStack(Material.BOW));
	}

}
