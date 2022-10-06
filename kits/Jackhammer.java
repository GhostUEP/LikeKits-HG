package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Jackhammer extends KitInterface {
	private HashMap<UUID, Integer> uses;

	public Jackhammer(Main main) {
		super(main);
		uses = new HashMap<>();
	}

	@EventHandler
	public void onBreak(final BlockBreakEvent e) {
		final Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (!hasAbility(p))
			return;
		if (item.getType() != Material.STONE_AXE)
			return;
		if (Main.plugin.stage != Estagio.GAMETIME)
			return;
		if (CooldownManager.isInCooldown(p.getUniqueId(), "jackhammer")) {
			e.setCancelled(true);
			int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "jackhammer");
			p.sendMessage("§cJackhammer em cooldown, faltando: " + timeleft + " segundos");
			return;
		}
		if (!this.uses.containsKey(p.getUniqueId())) {
			this.uses.put(p.getUniqueId(), Integer.valueOf(1));
		}
		if (this.uses.get(p.getUniqueId()).intValue() <= 6) {
			this.uses.put(p.getUniqueId(), this.uses.get(p.getUniqueId()).intValue() + 1);
		}
		if (this.uses.get(p.getUniqueId()).intValue() > 6) {
			CooldownManager c = new CooldownManager(p.getUniqueId(), "jackhammer", 30);
			c.start();
			this.uses.remove(p.getUniqueId());
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				public void run() {
					p.sendMessage("§aKit pronto");
				}
			}, 600L);
		}
		final double pY = p.getLocation().getDirection().getY();
		new BukkitRunnable() {
			final Block bup = e.getBlock().getRelative(BlockFace.UP);
			final Block bdown = e.getBlock().getRelative(BlockFace.DOWN);
			Block blocku = bup;
			Block blockd = bdown;

			public void run() {
				if (!blockd.getType().equals(Material.BEDROCK)) {
					if (pY < 0.14) {
						if (blockd.getType() != Material.BEDROCK) {
							blockd.setType(Material.AIR);
							blockd = blockd.getRelative(BlockFace.DOWN);
						}
					} else {
						if (blocku.getType() != Material.BEDROCK) {
							blocku.setType(Material.AIR);
							blocku = blocku.getRelative(BlockFace.UP);
						}
					}
				} else
					cancel();
			}
		}.runTaskTimer(Main.plugin, 1, 1);
	}

	@Override
	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<>();
		kititems.add(new ItemStack(Material.STONE_AXE));
		return new Kit("jackhammer",
				Arrays.asList(new String[] { "Quebre uma fileira de blocos para baixo ou para cima" }), null, kititems,
				new ItemStack(Material.STONE_AXE));
	}
}