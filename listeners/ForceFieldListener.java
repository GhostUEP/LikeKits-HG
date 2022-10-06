package me.ghost.hg.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.ghost.hg.Main;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.events.TimeSecondEvent;

public class ForceFieldListener implements Listener {
	public Main main;

	public ForceFieldListener(Main m) {
		main = m;

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void coliseuDano(TimeSecondEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Location loc = p.getLocation();
			if (main.stage == Estagio.PREGAME) {
				Location l = new Location(p.getWorld(), 0, p.getWorld().getHighestBlockYAt(0, 0), 0);
				if ((Math.abs(loc.getBlockX() + l.getBlockX()) >= 500)
						|| (Math.abs(loc.getBlockZ() + l.getBlockZ()) >= 500)) {
					int x = new Random().nextInt(5) + 30;
					int y = new Random().nextInt(5) + 90;
					int z = new Random().nextInt(5) + 30;
					Location locs = new Location(p.getWorld(), x, y, z);
					p.teleport(locs);
				}
			}
			if (main.stage == Estagio.PREGAME) {
				Location l = new Location(p.getWorld(), 0, p.getWorld().getHighestBlockYAt(0, 0), 0);
				if (Math.abs(loc.getBlockX() + l.getBlockX()) >= 35
						|| Math.abs(loc.getBlockZ() + l.getBlockZ()) >= 35) {
					if (!main.perm.isTrial(p)) {
						Location l2 = new Location(p.getWorld(), 0, 125, 0);
						p.teleport(l2);
					}
				}
				if (p.getLocation().getY() < 120 && !main.perm.isTrial(p)) {
					Location l2 = new Location(p.getWorld(), 0, 125, 0);
					p.teleport(l2);
				}
			}
			if (main.stage != Estagio.PREGAME) {
				if (main.adm.isSpectating(p)) {
					Location l = new Location(p.getWorld(), 0, p.getWorld().getHighestBlockYAt(0, 0), 0);
					if ((Math.abs(loc.getBlockX() + l.getBlockX()) >= 500)
							|| (Math.abs(loc.getBlockZ() + l.getBlockZ()) >= 500)) {
						int x = new Random().nextInt(5) + 30;
						int y = new Random().nextInt(5) + 90;
						int z = new Random().nextInt(5) + 30;
						Location locs = new Location(p.getWorld(), x, y, z);
						p.teleport(locs);
					}
				}
			}
			if (main.stage != Estagio.PREGAME) {
				Location l = new Location(p.getWorld(), 0, p.getWorld().getHighestBlockYAt(0, 0), 0);
				if ((Math.abs(loc.getBlockX() + l.getBlockX()) >= 500)
						|| (Math.abs(loc.getBlockZ() + l.getBlockZ()) >= 500)) {
					if (!main.isNotPlaying(p)) {
						p.setFireTicks(500);
						p.damage(6.0D);
					}
				}
			}

			if (main.stage != Estagio.PREGAME) {
				if (p.getLocation().getY() > 130) {
					if (!main.isNotPlaying(p) && main.stage != Estagio.WINNER) {
						p.setFireTicks(10);
						p.damage(7.0D);

					}
				}
			}
		}
		if (main.stage == Estagio.GAMETIME) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (main.isNotPlaying(p))
					continue;
				if (main.blocosColiseu.contains(p.getLocation().getBlock())) {
					p.setFireTicks(500);
					p.damage(8.0D);
				}
			}
		}
	}

	@EventHandler
	public void onffbreak(BlockBreakEvent event) {
		final Player p = event.getPlayer();
		Block b = event.getBlock();
		Location loc = b.getLocation();
		Location l = new Location(p.getWorld(), 0, p.getWorld().getHighestBlockYAt(0, 0), 0);
		if ((Math.abs(loc.getBlockX() + l.getBlockX()) >= 490) || (Math.abs(loc.getBlockZ() + l.getBlockZ()) >= 490)) {
			if (b.getType() == Material.RED_MUSHROOM || b.getType() == Material.BROWN_MUSHROOM)
				return;
			p.sendMessage("§cVocê não pode quebrar blocos perto do Forcefield");
			event.setCancelled(true);
		}
		if (GameListener.feast != null && GameListener.feast.isFeastBlock(event.getBlock())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onffplace(BlockPlaceEvent event) {
		final Player p = event.getPlayer();
		Block b = event.getBlockPlaced();
		Location loc = b.getLocation();
		Location l = new Location(p.getWorld(), 0, p.getWorld().getHighestBlockYAt(0, 0), 0);
		if ((Math.abs(loc.getBlockX() + l.getBlockX()) >= 490) || (Math.abs(loc.getBlockZ() + l.getBlockZ()) >= 490)) {
			p.sendMessage("§cVocê não pode colocar blocos perto do Forcefield");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onColocar(BlockPlaceEvent e) {
		if (main.blocosColiseu.contains(e.getBlockAgainst())) {
			e.setCancelled(true);
		}
		if (main.blocosColiseu.contains(e.getBlockPlaced())) {
			e.setCancelled(true);
		}
		if ((GameListener.feast != null && GameListener.feast.isFeastBlock(e.getBlockAgainst()))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onClosei(BlockBreakEvent event) {
		if (main.blocosColiseu.contains(event.getBlock())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void fogoNao(BlockBurnEvent e) {
		if (main.blocosColiseu.contains(e.getBlock())) {
			e.setCancelled(true);
		}
	}
}
