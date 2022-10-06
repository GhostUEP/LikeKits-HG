package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Checkpoint extends KitInterface {

	public Checkpoint(Main main) {
		super(main);
	}

	private HashMap<UUID, Location> checkpoints = new HashMap<UUID, Location>();
	private HashMap<UUID, Long> hit = new HashMap<UUID, Long>();

	@EventHandler
	public void colocar(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (!hasAbility(p))
			return;
		if (e.getItemInHand().getType() == Material.NETHER_FENCE && e.getItemInHand().hasItemMeta()
				&& e.getItemInHand().getItemMeta().getDisplayName().contains("Checkpoint")) {
			if (CooldownManager.isInCooldown(p.getUniqueId(), "checkpoint2")) {
				e.setBuild(false);
				e.setCancelled(true);
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "checkpoint2");
				p.sendMessage("§cCheckpoint em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			if ((this.hit.containsKey(p.getUniqueId()))
					&& (this.hit.get(p.getUniqueId()) > System.currentTimeMillis())) {
				p.sendMessage("§cVocê levou um hit a pouco tempo, Espere para usar o checkpoint");
				e.setCancelled(true);
				e.setBuild(false);
				p.updateInventory();
				return;
			}
			CooldownManager cd = new CooldownManager(p.getUniqueId(), "checkpoint2", 5);
			cd.start();
			p.setItemInHand(e.getItemInHand());
			p.updateInventory();
			if (this.checkpoints.containsKey(p.getUniqueId())) {
				Block b = this.checkpoints.get(p.getUniqueId()).getBlock();
				b.setType(Material.AIR);
			}
			this.checkpoints.put(p.getUniqueId(), e.getBlock().getLocation());
		}
	}

	@EventHandler
	public void rightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		if (!hasAbility(p))
			return;
		if (!e.getAction().name().contains("RIGHT"))
			return;
		if (i.getType() == Material.FLOWER_POT_ITEM && i.hasItemMeta()
				&& i.getItemMeta().getDisplayName().contains("Checkpoint Teleport")) {
			e.setCancelled(true);
			if (e.getPlayer().getLocation().getY() > 140) {
				return;
			}
			if (!this.checkpoints.containsKey(p.getUniqueId())) {
				p.sendMessage("§cVocê não tem nenhum checkpoint salvo");
			} else if (this.hit.containsKey(p.getUniqueId()) && hit.get(p.getUniqueId()) > System.currentTimeMillis()) {
				p.sendMessage("§cVocê levou um hit recentemente, aguarde para usar");
			} else if (CooldownManager.isInCooldown(p.getUniqueId(), "checkpoint")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "checkpoint");
				p.sendMessage("§cCheckpoint em cooldown, faltando: " + timeleft + " segundos");
			} else {
				p.teleport(this.checkpoints.get(p.getUniqueId()));
				p.sendMessage("§bTeleported");
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				CooldownManager cd = new CooldownManager(p.getUniqueId(), "checkpoint", 30);
				cd.start();
			}
		}
	}

	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		if ((e.isCancelled()) || (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player))) {
			return;
		}
		if (!hasAbility((Player) e.getEntity())) {
			return;
		}
		this.hit.put(e.getEntity().getUniqueId(), System.currentTimeMillis() + 6000L);
	}

	@EventHandler
	public void blockDamage(BlockDamageEvent e) {
		if (this.checkpoints.containsValue(e.getBlock().getLocation())
				&& e.getBlock().getType() == Material.NETHER_FENCE) {
			e.setCancelled(true);
			for (UUID p : this.checkpoints.keySet()) {
				if (this.checkpoints.get(p) == e.getBlock().getLocation()) {
					if (Bukkit.getPlayer(p) != null) {
						Bukkit.getPlayer(p).sendMessage("§cSeu checkpoint foi removido");
					}
					this.checkpoints.remove(p);
				}
			}
			e.getBlock().setType(Material.AIR);
			e.getPlayer().sendMessage("§cVocê destruiu um checkpoint");
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.NETHER_FENCE, 1, "§r§5Checkpoint"));
		kitItems.add(createItem(Material.FLOWER_POT_ITEM, 1, "§r§5Checkpoint Teleport"));
		return new Kit("checkpoint",
				Arrays.asList(new String[] {
						"Ao colocar sua cerca do nether em um local, esta localizacao será salva, quando você clicar no pote de flor, voce será teleportado para a ultima localizacao que voce salvou (Se ela não for destruida por um jogador)" }),
				kitItems, null, new ItemStack(Material.NETHER_FENCE));
	}

}
