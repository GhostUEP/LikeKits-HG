package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class Immortal extends KitInterface {

	public Immortal(Main main) {
		super(main);
	}

	private List<UUID> immortal = new ArrayList<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			final Player p = (Player) e.getEntity();
			EntityPlayer p2 = ((CraftPlayer) p).getHandle();
			if (hasAbility(p)) {
				if (Main.plugin.stage != Estagio.GAMETIME) {
					return;
				}
				if (immortal.contains(p.getUniqueId())) {
					e.setCancelled(true);
					p.setHealth(1);
					p.sendMessage("§c§lIMMORTAL > §cVocê não morreu pois está em modo furia.");
					return;
				}
				if (e.getDamage() >= p2.getHealth()) {
					if (CooldownManager.isInCooldown(p.getUniqueId(), "immortal")) {
						return;
					}
					p.setHealth(1);
					immortal.add(p.getUniqueId());
					CooldownManager cd = new CooldownManager(p.getUniqueId(), "immortal", 65);
					cd.start();
					for (Player g : Bukkit.getOnlinePlayers()) {
						if (Main.plugin.isNotPlaying(g))
							continue;
						if (g.getLocation().distance(p.getLocation()) <= 10) {
							g.playSound(g.getLocation(), Sound.GHAST_SCREAM, 1.0F, 1.0F);
						}
					}
					e.setCancelled(true);
					p.sendMessage("§a§lIMMORTAL > §aVocê entrou em modo furia por 5 segundos.");

					new BukkitRunnable() {

						@Override
						public void run() {
							if (p.isOnline() && !Main.plugin.isNotPlaying(p) && immortal.contains(p.getUniqueId())) {
								immortal.remove(p.getUniqueId());
								p.sendMessage("§c§lIMMORTAL > §cO modo furia acabou.");
							}

						}
					}.runTaskLater(Main.plugin, 20L * 5);
					new BukkitRunnable() {

						@Override
						public void run() {
							if (hasAbility(p) && !Main.plugin.isNotPlaying(p)) {
								p.sendMessage("§a§lIMMORTAL --> §aSeu kit já está disponivel novamente.");
							}

						}
					}.runTaskTimer(Main.plugin, 0, 20L * 65);
				} else if (p2.getHealth() <= 6) {
					if (CooldownManager.isInCooldown(p.getUniqueId(), "immortal")) {
						return;
					}
					p.setHealth(1);
					immortal.add(p.getUniqueId());
					CooldownManager cd = new CooldownManager(p.getUniqueId(), "immortal", 65);
					cd.start();
					for (Player g : Bukkit.getOnlinePlayers()) {
						if (Main.plugin.isNotPlaying(g))
							continue;
						if (g.getLocation().distance(p.getLocation()) <= 10) {
							g.playSound(g.getLocation(), Sound.GHAST_SCREAM, 1.0F, 1.0F);
						}
					}
					e.setCancelled(true);
					p.sendMessage("§a§lIMMORTAL > §aVocê entrou em modo furia por 5 segundos.");
					new BukkitRunnable() {

						@Override
						public void run() {
							if (p.isOnline() && !Main.plugin.isNotPlaying(p) && immortal.contains(p.getUniqueId())) {
								immortal.remove(p.getUniqueId());
								p.sendMessage("§c§lIMMORTAL > §cO modo furia acabou.");
							}

						}
					}.runTaskLater(Main.plugin, 20L * 5);
					new BukkitRunnable() {

						@Override
						public void run() {
							if (hasAbility(p) && !Main.plugin.isNotPlaying(p)) {
								p.sendMessage("§a§lIMMORTAL --> §aSeu kit já está disponivel novamente.");
							}

						}
					}.runTaskTimer(Main.plugin, 0, 20L * 65);
				}

			}
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("immortal",
				Arrays.asList(new String[] {
						"Ao ativar o kit você ira ficar com meio coração e não vai receber dano durante 5 segundos depois você vai receber 10 corações de vida" }),
				kitItems, null, new ItemStack(Material.GHAST_TEAR));
	}

}
