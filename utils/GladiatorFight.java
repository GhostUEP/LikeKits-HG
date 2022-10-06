package me.ghost.hg.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.BO3Blocks;
import me.ghost.hg.enums.GladiatorType;
import me.ghost.hg.kits.Gladiator;

public class GladiatorFight {
	private Player gladiator;
	private Player target;
	private Location tpLocGladiator;
	private Location tpLocTarget;
	private BukkitRunnable witherEffect;
	private BukkitRunnable teleportBack;
	private List<Block> blocksToRemove;
	private Listener listener;
	private GladiatorType gladtype;
	public ArrayList<BO3Blocks> glad1;

	public GladiatorFight(final Player gladiator, final Player target, final GladiatorType gladtype) {
		this.gladiator = gladiator;
		this.target = target;
		this.blocksToRemove = new ArrayList<Block>();
		this.glad1 = Main.plugin.gladiator;
		this.gladtype = gladtype;
		send1v1();
		listener = new Listener() {

			@EventHandler
			public void onEntityDamage(EntityDamageByEntityEvent event) {
				if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
					Player recebe = (Player) event.getEntity();
					Player faz = (Player) event.getDamager();
					if (isIn1v1(faz) && isIn1v1(recebe))
						return;
					if (isIn1v1(faz) && !isIn1v1(recebe))
						event.setCancelled(true);
					else if (!isIn1v1(faz) && isIn1v1(recebe))
						event.setCancelled(true);
				}
			}

			@EventHandler(priority = EventPriority.LOWEST)
			public void onDeath(PlayerDeathEvent event) {
				Player p = event.getEntity();
				if (!isIn1v1(p))
					return;
				if (p == gladiator) {
					// target winner
					target.sendMessage("§6Você venceu a batalha contra " + gladiator.getName() + ".");
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
					p.sendMessage("§cVocê perdeu a batalha contra " + target.getName() + ".");
					Main.plugin.dropItems(p, event.getDrops(), tpLocGladiator);
					event.getDrops().clear();
					teleportBack(target, gladiator);
					return;
				}
				// gladiator winner
				gladiator.sendMessage("§6Você venceu a batalha contra " + target.getName() + ".");
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
				p.sendMessage("§cVocê perdeu a batalha contra " + gladiator.getName() + ".");
				Main.plugin.dropItems(p, event.getDrops(), tpLocTarget);
				event.getDrops().clear();
				teleportBack(gladiator, target);
			}

			@EventHandler
			public void onQuit(PlayerQuitEvent event) {
				Player p = event.getPlayer();
				if (!isIn1v1(p))
					return;
				if (event.getPlayer().isDead())
					return;
				if (p == gladiator) {
					// target winner
					target.sendMessage("§7" + gladiator.getName() + "§6 saiu da partida, você venceu.");
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
					Main.plugin.dropItems(p, tpLocGladiator);
					teleportBack(target, gladiator);
					return;
				}
				// gladiator winner
				gladiator.sendMessage("§7" + target.getName() + "§6 saiu da partida, você venceu.");
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
				Main.plugin.dropItems(p, tpLocTarget);
				teleportBack(gladiator, target);
			}

			@EventHandler
			public void onKick(PlayerKickEvent event) {
				Player p = event.getPlayer();
				if (!isIn1v1(p))
					return;
				if (event.getPlayer().isDead())
					return;
				if (p == gladiator) {
					// target winner
					target.sendMessage("§7" + gladiator.getName() + "§6 saiu da partida, você venceu.");
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
					Main.plugin.dropItems(p, tpLocGladiator);
					teleportBack(target, gladiator);
					return;
				}
				// gladiator winner
				gladiator.sendMessage("§7" + target.getName() + "§6 saiu da partida, você venceu.");
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
				Main.plugin.dropItems(p, tpLocTarget);
				teleportBack(gladiator, target);
			}

			@EventHandler
			public void onTeleport(PlayerTeleportEvent event) {
				if (event.isCancelled())
					return;
				Player p = event.getPlayer();
				if (!isIn1v1(p))
					return;
				if (!Gladiator.inGladiator(p))
					return;
				if (blocksToRemove.contains(event.getTo().getBlock()))
					return;
				if (p == gladiator) {
					// target winner
					target.sendMessage("§aAlgum de vocês foi teleportado para fora da arena.");
					gladiator.sendMessage("§aAlgum de vocês foi teleportado para fora da arena.");
					teleportBack(event.getTo(), tpLocTarget);
					return;
				} else if (p == target) {
					// gladiator winner
					gladiator.sendMessage("§aAlgum de vocês foi teleportado para fora da arena.");
					target.sendMessage("§aAlgum de vocês foi teleportado para fora da arena.");
					teleportBack(tpLocGladiator, event.getTo());
				}
			}

			@EventHandler
			public void onsair(PlayerMoveEvent e) {
				if (e.isCancelled())
					return;
				Player p = e.getPlayer();
				if (!isIn1v1(p))
					return;
				if (!Gladiator.inGladiator(p))
					return;
				if (blocksToRemove.contains(e.getTo().getBlock()))
					return;
				if (p == gladiator) {
					// target winner
					teleportBack(gladiator, gladiator);
					teleportBack(target, gladiator);
					return;
				} else if (p == target) {
					// gladiator winner
					teleportBack(target, gladiator);
					teleportBack(gladiator, gladiator);
				}
			}
		};
		Bukkit.getServer().getPluginManager().registerEvents(listener, Main.plugin);
	}

	public boolean isIn1v1(Player player) {
		return player == gladiator || player == target;
	}

	public void destroy() {
		HandlerList.unregisterAll(listener);
	}

	public void send1v1() {
		Location loc = gladiator.getLocation();
		boolean hasGladi = true;
		while (hasGladi) {
			hasGladi = false;
			boolean stop = false;
			for (double x = -8; x <= 8; x++) {
				for (double z = -8; z <= 8; z++) {
					for (double y = 0; y <= 10; y++) {
						Location l = new Location(loc.getWorld(), loc.getX() + x, 120 + y, loc.getZ() + z);
						if (l.getBlock().getType() != Material.AIR) {
							hasGladi = true;
							loc = new Location(loc.getWorld(), loc.getX() + 20, loc.getY(), loc.getZ());
							stop = true;
						}
						if (stop)
							break;
					}
					if (stop)
						break;
				}
				if (stop)
					break;
			}
		}
		Block mainBlock = loc.getBlock();
		generateArena(mainBlock);
		tpLocGladiator = gladiator.getLocation().clone();
		tpLocTarget = target.getLocation().clone();
		gladiator.sendMessage("§6Você está em uma batalha com: §7" + target.getName());
		target.sendMessage("§6Você está em uma batalha com: §7" + gladiator.getName());
		Location l1 = new Location(mainBlock.getWorld(), mainBlock.getX() + 6.5, 122, mainBlock.getZ() + 6.5);
		l1.setYaw((float) (90.0 * 1.5));
		Location l2 = new Location(mainBlock.getWorld(), mainBlock.getX() - 5.5, 122, mainBlock.getZ() - 5.5);
		l2.setYaw((float) (90.0 * 3.5));
		target.teleport(l1);
		gladiator.teleport(l2);
		Gladiator.playersIn1v1.add(gladiator.getUniqueId());
		Gladiator.playersIn1v1.add(target.getUniqueId());
		if (gladtype == GladiatorType.INFERNOR) {
			gladiator.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10, 0));
		}
		gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		witherEffect = new BukkitRunnable() {
			@Override
			public void run() {
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 60, 5));
				target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 60, 5));
			}
		};
		witherEffect.runTaskLater(Main.plugin, 20 * 60 * 2);
		teleportBack = new BukkitRunnable() {
			@Override
			public void run() {
				teleportBack(tpLocGladiator, tpLocTarget);
			}
		};
		teleportBack.runTaskLater(Main.plugin, 20 * 60 * 3);
	}

	public void teleportBack(Location loc, Location loc1) {
		Gladiator.playersIn1v1.remove(gladiator.getUniqueId());
		Gladiator.playersIn1v1.remove(target.getUniqueId());
		gladiator.teleport(loc);
		target.teleport(loc1);
		removeBlocks();
		gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		gladiator.removePotionEffect(PotionEffectType.WITHER);
		target.removePotionEffect(PotionEffectType.WITHER);
		stop();
		destroy();
	}

	public void teleportBack(Player winner, Player loser) {
		Gladiator.playersIn1v1.remove(winner.getUniqueId());
		Gladiator.playersIn1v1.remove(loser.getUniqueId());
		winner.teleport(tpLocGladiator);
		loser.teleport(tpLocTarget);
		removeBlocks();
		winner.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		winner.removePotionEffect(PotionEffectType.WITHER);
		stop();
		destroy();
	}

	public void generateBlocks(Block mainBlock) {
		for (double x = -8; x <= 8; x++) {
			for (double z = -8; z <= 8; z++) {
				for (double y = 0; y <= 9; y++) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 120 + y,
							mainBlock.getZ() + z);
					l.getBlock().setType(Material.GLASS);
					Gladiator.gladiatorBlocks.add(l.getBlock());
					blocksToRemove.add(l.getBlock());
				}
			}
		}
		for (double x = -7; x <= 7; x++) {
			for (double z = -7; z <= 7; z++) {
				for (double y = 1; y <= 8; y++) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 120 + y,
							mainBlock.getZ() + z);
					l.getBlock().setType(Material.AIR);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void generateArena(Block mainBlock) {
		Location l = mainBlock.getLocation();
		l.setY(120);
		for (BO3Blocks bo3 : glad1) {
			Block b = new Location(l.getWorld(), l.getX() + bo3.getX(), l.getY() + bo3.getY(), l.getZ() + bo3.getZ())
					.getBlock();
			b.setType(bo3.getType());
			b.setData(bo3.getData());
			Gladiator.gladiatorBlocks.add(b);
			blocksToRemove.add(b);

		}
		for (double x = -7; x <= 7; x++) {
			for (double z = -7; z <= 7; z++) {
				for (double y = 1; y <= 9; y++) {
					Location loc = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 120 + y,
							mainBlock.getZ() + z);
					blocksToRemove.add(loc.getBlock());
				}
			}
		}
	}

	public void removeBlocks() {
		for (Block b : blocksToRemove) {
			if (b.getType() != null && b.getType() != Material.AIR)
				b.setType(Material.AIR);
			Gladiator.gladiatorBlocks.remove(b);
		}
		blocksToRemove.clear();

	}

	public void stop() {
		witherEffect.cancel();
		teleportBack.cancel();
	}
}
