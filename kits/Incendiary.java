package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;
import me.ghost.hg.utils.Particulas;

public class Incendiary extends KitInterface {

	public Incendiary(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (isKitItem(event.getItem(), "§6§lIncendiary") && hasAbility(event.getPlayer())
				&& event.getAction().name().contains("RIGHT")) {
			event.setCancelled(true);
			final Player p = event.getPlayer();
			if (Main.plugin.stage != Estagio.GAMETIME) {
				p.sendMessage("§cVocê não pode usar isto agora!");
				return;
			}
			if (CooldownManager.isInCooldown(p.getUniqueId(), "incendiary")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "incendiary");
				p.sendMessage("§cIncendiary em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			CooldownManager cd = new CooldownManager(p.getUniqueId(), "incendiary", 90);
			cd.start();
			final BlockIterator iterator = new BlockIterator(p.getEyeLocation(), 0, 64);
			new BukkitRunnable() {
				List<Entity> atingidas = new ArrayList<Entity>();

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if (iterator.hasNext()) {
						Block b = iterator.next();
						if (b.getType() != Material.AIR) {
							cancel();
						} else {
							Particulas.FLAME.part(b.getLocation(), 0.25F, 0.25F, 0.25F, 0, 10);
							for (Player o : Bukkit.getOnlinePlayers()) {
								if (o.equals(p) || atingidas.contains(o) || Main.plugin.adm.isSpectating(o)) {
									continue;
								}
								if (o.getLocation().distance(b.getLocation()) > 3.0D) {
									continue;
								}
								if (hasAntikit(o)) {
									p.sendMessage(Main.plugin.antikit_message);
									continue;
								}
								o.setNoDamageTicks(0);
								Main.plugin.darDano(o, p, 7.0D, false);
								o.setFireTicks(120);
								o.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 0), true);
								atingidas.add(o);
							}
						}
					} else {
						cancel();
					}
				}
			}.runTaskTimer(Main.plugin, 0L, 1L);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.BLAZE_ROD, "§6§lIncendiary"));
		return new Kit("incendiary",
				Arrays.asList(new String[] {
						"Você inicia a partida com uma blaze rod ao clicar nesta blaze rod, voce lançara uma linha de energia que por onde passar deixará quem foi atingido queimando sofrendo envenenamento além de 5 coracoes de dano" }),
				kitItems, null, new ItemStack(Material.BLAZE_ROD));
	}

}
