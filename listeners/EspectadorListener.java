package me.ghost.hg.listeners;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

import me.ghost.hg.Main;
import me.ghost.hg.events.TimeSecondEvent;

public class EspectadorListener implements Listener {
	public Main main;

	public EspectadorListener(Main m) {
		main = m;
	}

	private ArrayList<UUID> noScoreboard = new ArrayList<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSecond(TimeSecondEvent event) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (noScoreboard.contains(p.getUniqueId())) {
				continue;
			}

			// p.setScoreboard(main.getScoreboard(p));
		}
	}

	/*
	 * @EventHandler public void onCommand(PlayerCommandPreprocessEvent event) {
	 * if (event.getMessage().toLowerCase().startsWith("/score")) {
	 * event.setCancelled(true); if
	 * (noScoreboard.contains(event.getPlayer().getUniqueId())) {
	 * noScoreboard.remove(event.getPlayer().getUniqueId()); event.getPlayer().
	 * sendMessage("§a§lAVISO > §aAgora scoreboard será exibida!"); } else {
	 * noScoreboard.add(event.getPlayer().getUniqueId());
	 * event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().
	 * getNewScoreboard()); event.getPlayer().
	 * sendMessage("§a§lAVISO > §aAgora a scoreboard irá se esconder!"); } } }
	 */

	@EventHandler
	public void noFollow(EntityTargetEvent event) {
		if (!(event.getTarget() instanceof Player))
			return;
		if (!main.adm.isSpectating((Player) event.getTarget()))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void Item(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (main.adm.isSpectating(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Comida(FoodLevelChangeEvent e) {
		Player p = (Player) e.getEntity();
		if (main.adm.isSpectating(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEntityEvent event) {
		if (main.adm.isAdmin(event.getPlayer())) {
			if (event.getRightClicked() instanceof Player) {
				event.getPlayer().openInventory(((Player) event.getRightClicked()).getInventory());
			}
		}
	}

	@EventHandler
	public void Collision(VehicleEntityCollisionEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();
		if (main.adm.isSpectating(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onVehicleDestroy(VehicleDestroyEvent e) {
		Entity entity = e.getAttacker();
		if (!(entity instanceof Player))
			return;
		Player p = (Player) e.getAttacker();
		if (main.adm.isSpectating(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onVehicleEnter(VehicleEnterEvent e) {
		if (!(e instanceof Player))
			return;
		Player p = (Player) e.getEntered();
		if (main.adm.isSpectating(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onVehicleDamage(VehicleDamageEvent e) {
		Entity entity = e.getAttacker();
		if ((entity instanceof Player))
			return;
		Player p = (Player) e.getAttacker();
		if (main.adm.isSpectating(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Quebrar(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (main.adm.isYTUT(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Colocar(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (main.adm.isYTUT(p)) {
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void Interagir(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (main.adm.isYTUT(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Dano(EntityDamageEvent event) {
		if ((event.getEntity() instanceof Player)) {
			Player p = (Player) event.getEntity();
			if (main.adm.isSpectating(p)) {
				event.setCancelled(true);
			}

		}

	}

	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent e) {
		Player p = e.getPlayer();
		if (main.adm.isYTUT(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent e) {
		Player p = e.getPlayer();
		if (main.adm.isYTUT(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityShootArrow(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();
		if (main.adm.isSpectating(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (main.adm.isYTUT(p)) {
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onPlayerEntityShear(PlayerShearEntityEvent e) {
		Player p = e.getPlayer();
		if (main.adm.isSpectating(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void DamagePlayers2(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getDamager();
		if (main.adm.isYTUT(p)) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onYoutuber(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (main.adm.isYTUT(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onYoutuber2(InventoryInteractEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (main.adm.isYTUT(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onYoutuber3(PlayerInteractEntityEvent event) {
		if (main.adm.isYTUT(event.getPlayer())) {
			if (event.getRightClicked() instanceof Player) {
				event.getPlayer().openInventory(((Player) event.getRightClicked()).getInventory());
			}
		}
	}
}
