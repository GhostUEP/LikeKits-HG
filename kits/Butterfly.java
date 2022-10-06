package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Butterfly extends KitInterface {

	public Butterfly(Main main) {
		super(main);
	}

	@EventHandler
	public void butterfly(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && hasAbility((Player) event.getEntity())
				&& event.getEntity().isInsideVehicle() && event.getEntity().getVehicle().hasMetadata("butterfly")
				&& event.getCause() == DamageCause.FALL) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void butterfly(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR && hasAbility(event.getPlayer())
				&& isKitItem(event.getItem(), "§rButter§lFly") && event.getItem().getType() == Material.FEATHER) {
			event.setCancelled(true);
			Player p = event.getPlayer();
			if (CooldownManager.isInCooldown(p.getUniqueId(), "butterfly")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "butterfly");
				p.sendMessage("§cButterfly em cooldown, faltando: " + timeleft + " segundos");
				return;
			}

			CooldownManager cd = new CooldownManager(p.getUniqueId(), "butterfly", 60);
			cd.start();
			p.playSound(p.getLocation(), Sound.FIREWORK_BLAST, 10.0F, (byte) 1);
			final Item item = p.getWorld().dropItemNaturally(p.getEyeLocation(),
					createItem(Material.SNOW_BALL, 1, p.getName()));
			item.setVelocity(p.getEyeLocation().getDirection().multiply(3.0F));
			item.setPickupDelay(Integer.MAX_VALUE);
			item.setPassenger(p);
			item.setMetadata("butterfly", new FixedMetadataValue(Main.plugin, p));
			new BukkitRunnable() {
				public void run() {
					if (item.isOnGround()) {
						if (item.getPassenger() != null) {
							item.getPassenger().setFallDistance(0.0F);
						}
						item.remove();
						cancel();
					} else if (item.getPassenger() == null) {
						item.remove();
						cancel();
					} else if (item.isDead()) {
						cancel();
					}
				}
			}.runTaskTimer(Main.plugin, 0L, 20L);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.FEATHER, "§rButter§lFly"));
		return new Kit("butterfly",
				Arrays.asList(new String[] {
						"Inicia com uma pena que ao clicar nela, voce lança um barco e voce ira ficar montado em cima dele" }),
				kitItems, null, new ItemStack(Material.FEATHER));
	}

}
