package me.ghost.hg.listeners;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;

public class PregameListener implements Listener {

	public Main main;

	public PregameListener(Main m) {
		main = m;
	}

	@EventHandler
	public void BreakBlock(BlockBreakEvent e) {

		if (main.stage == Estagio.PREGAME) {
			e.setCancelled(true);
		}
		if (!main.build && !main.perm.isTrial(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void PlaceBlock(BlockPlaceEvent e) {

		if (main.stage == Estagio.PREGAME) {
			e.setCancelled(true);
		}
		if (!main.build && !main.perm.isTrial(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void PlayerInteract(PlayerInteractEvent e) {
		if (main.stage == Estagio.PREGAME) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Dano(EntityDamageEvent e) {
		if (main.stage == Estagio.PREGAME) {
			e.setCancelled(true);
		}
		if (main.stage == Estagio.INVENCIBILITY) {
			if (e.getEntity() instanceof Player) {
				e.setCancelled(true);
			}
		}
		if (main.stage == Estagio.WINNER) {
			if (e.getEntity() instanceof Player) {
				e.setCancelled(true);
			}
		}
		if (!main.dano) {
			if (e.getEntity() instanceof Player) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_AIR && main.clicks.containsKey(e.getPlayer().getUniqueId())) {
			int click = main.clicks.get(e.getPlayer().getUniqueId());
			click++;
			main.clicks.put(e.getPlayer().getUniqueId(), click);
		}
	}

	@EventHandler
	public void onPvp(EntityDamageByEntityEvent e) {
		if (!main.pvp && e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		if (event.getEntityType() == EntityType.GHAST || event.getEntityType() == EntityType.PIG_ZOMBIE) {
			event.setCancelled(true);
			return;
		}
		if (main.stage == Estagio.PREGAME) {
			event.setCancelled(true);
			return;
		}
		if (event.getSpawnReason() != SpawnReason.NATURAL)
			return;
		if (new Random().nextInt(5) != 0) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onTab(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/me ") || event.getMessage().toLowerCase().startsWith("/kill")
				|| event.getMessage().toLowerCase().startsWith("/suicide")
				|| event.getMessage().toLowerCase().startsWith("/bukkit:me")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cVoce nao pode digitar esse comando");
			return;
		}
	}

	@EventHandler
	public void Comida(FoodLevelChangeEvent e) {
		// Player p = (Player) e.getEntity();

		if (main.stage != Estagio.GAMETIME) {
			e.setCancelled(true);
		}

		if ((e.getEntity() instanceof Player)) {
			((Player) e.getEntity()).setSaturation(4.0F);
		}

		// if (Main.plugin.admin.isAdmin(p) ||
		// Main.plugin.admin.isSpectating(p)) {
		// e.setCancelled(true);
		// }
	}

	@EventHandler
	public void Spawnar(EntitySpawnEvent e) {
		if (main.stage == Estagio.PREGAME) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Baude(PlayerBucketFillEvent e) {
		// Player p = e.getPlayer();

		if (main.stage == Estagio.PREGAME) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Baude2(PlayerBucketEmptyEvent e) {
		// Player p = e.getPlayer();
		if (main.stage == Estagio.PREGAME) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(InventoryClickEvent event) {
		if (!event.getInventory().getName().contains("Caixa"))
			return;
		event.setCancelled(true);
		event.getWhoClicked().setItemOnCursor(null);
	}

	@EventHandler
	public void Arco(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		// Player p = (Player) e.getEntity();

		if (((e.getEntity() instanceof Player)) && (main.stage == Estagio.PREGAME)) {
			e.getBow().setDurability((short) 0);
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Dropar(PlayerDropItemEvent e) {
		// Player p = e.getPlayer();
		if (main.stage == Estagio.PREGAME) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(EntityExplodeEvent event) {
		if (event.getEntity() instanceof EnderDragon) {
			event.setCancelled(true);
		}
		if (main.stage == Estagio.PREGAME) {
			event.setCancelled(true);
		}
		Iterator<Block> blocks = event.blockList().iterator();
		Block b = blocks.next();
		if (GameListener.feast != null && GameListener.feast.isFeastBlock(b)) {
			blocks.remove();
		}
	}

	@EventHandler
	public void Pegar(PlayerPickupItemEvent e) {
		// Player p = e.getPlayer();

		if (main.stage == Estagio.PREGAME) {
			e.setCancelled(true);
		}
	}

}
