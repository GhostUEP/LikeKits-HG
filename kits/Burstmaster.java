package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Burstmaster extends KitInterface {

	public Burstmaster(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (hasAbility(e.getPlayer()) && e.getAction().name().contains("RIGHT")
				&& isKitItem(e.getItem(), Material.GOLD_HOE, "§r§6Burstmaster")) {
			e.setCancelled(true);
			final Player p = e.getPlayer();
			if (Main.plugin.stage != Estagio.GAMETIME) {
				p.sendMessage("§cVocê não pode usar isto agora!");
				return;
			}
			if (CooldownManager.isInCooldown(p.getUniqueId(), "burstmaster")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "burstmaster");
				p.sendMessage("§cBurstmaster em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			CooldownManager cd = new CooldownManager(p.getUniqueId(), "burstmaster", 60);
			cd.start();
			final BlockIterator iterator = new BlockIterator(p, 64);
			new BukkitRunnable() {

				private List<Player> hits = new ArrayList<Player>();

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if (iterator.hasNext()) {
						Block b = iterator.next();
						if (b.getType() != Material.AIR) {
							b.getWorld().createExplosion(b.getLocation(), 4.0F);
							cancel();
						}
						b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 46);
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (Main.plugin.isNotPlaying(player))
								continue;
							if (player.equals(p) || hits.contains(player)) {
								continue;
							}
							if (player.getLocation().distance(b.getLocation()) > 3.0D) {
								continue;
							}
							if (hasAntikit(player)) {
								p.sendMessage(Main.plugin.antikit_message);
								continue;
							}
							hits.add(player);
							Main.plugin.darDano(player, p, 5.0D, true);
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
		kitItems.add(createItem(Material.GOLD_HOE, "§r§6Burstmaster"));
		return new Kit("burstmaster",
				Arrays.asList(new String[] {
						"Ao clicar em sua enxada de ouro você deixará um rastro de blocos por onde seu poder passa quando seu poder atingir um bloco, ele criará uma explosao relativa a uma TNT" }),
				kitItems, null, new ItemStack(Material.TNT));
	}

}
