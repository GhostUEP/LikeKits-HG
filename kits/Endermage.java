package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Endermage extends KitInterface {

	public Endermage(Main main) {
		super(main);
		addAntikit();
	}

	private ArrayList<String> portal = new ArrayList<String>();
	private HashMap<UUID, Integer> portalVolta = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Long> invencible = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!hasAbility(e.getPlayer()))
			return;
		if (e.getItem() == null)
			return;
		if (!(e.getAction().name().contains("BLOCK")))
			return;
		if (e.getItem().getType() == Material.NETHER_BRICK_ITEM && e.getItem().hasItemMeta()
				&& e.getItem().getItemMeta().getDisplayName().contains("Endermage Portal")) {
			e.setCancelled(true);
			e.getPlayer().updateInventory();
			final Block b = e.getClickedBlock();
			final BlockState bs = b.getState();
			final Player p = e.getPlayer();
			if (Main.plugin.adm.isSpectating(p)) {
				p.setItemInHand(null);
				return;
			}
			if (Math.abs(b.getX()) > 489 || Math.abs(b.getZ()) > 489) {
				p.sendMessage("§cVocê não pode usar o endermage aqui");
				return;
			}
			if (b.getType() == Material.BEDROCK) {
				p.sendMessage("§cVocê não pode puxar neste bloco!");
				return;
			}
			if (Main.plugin.blocosColiseu.contains(b)) {
				p.sendMessage("§cVocê não pode puxar neste bloco!");
				return;
			}
			if (p.getLocation().getBlockY() > 148) {
				p.sendMessage("§cUsar o endermage no gladiator não é permitido");
				return;
			}
			final Location portal2 = b.getLocation().clone().add(0.5, 1.5, 0.5);
			b.setType(Material.ENDER_PORTAL_FRAME);
			this.portalVolta.put(p.getUniqueId(), p.getInventory().getHeldItemSlot());
			p.setItemInHand(null);
			new BukkitRunnable() {
				int segundos = 5;

				public void run() {
					ArrayList<Player> players = getNearbyPlayers(p, portal2);
					if (!p.isOnline() || calculateDistance(p.getLocation(), portal2) > 50) {
						b.setType(bs.getType());
						b.setData(bs.getBlock().getData());
						if (!p.isOnline()) {
							portal.add(p.getName());
						}
						cancel();
					}
					if (!players.isEmpty()) {
						for (Player pl : players) {
							if (hasAntikit(pl)) {
								p.sendMessage(Main.plugin.antikit_message);
								continue;
							}

							pl.teleport(portal2);
							p.sendMessage("§cVocê puxou §r" + ChatColor.stripColor(pl.getDisplayName()));
							invencible.put(pl.getUniqueId(), System.currentTimeMillis() + 5000L);
							pl.sendMessage("§cVocê foi puxado por §r" + ChatColor.stripColor(p.getDisplayName()));
							pl.sendMessage("§cVocê esta invencivel por 5 Segundos");
						}
						p.getInventory().setItem(portalVolta.get(p.getUniqueId()),
								createItem(Material.NETHER_BRICK_ITEM, 1, "§rEndermage Portal"));
						p.teleport(portal2);
						p.sendMessage("§cVocê esta invencivel por 5 Segundos");
						invencible.put(p.getUniqueId(), System.currentTimeMillis() + 5000L);
						b.setType(bs.getType());
						b.setData(bs.getBlock().getData());
						cancel();
					}
					if (segundos == 0) {
						b.setType(bs.getType());
						b.setData(bs.getBlock().getData());
						p.getInventory().setItem(portalVolta.get(p.getUniqueId()),
								createItem(Material.NETHER_BRICK_ITEM, 1, "§rEndermage Portal"));
						cancel();
					}
					segundos--;
				}
			}.runTaskTimer(Main.plugin, 0L, 20L);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player))
			return;
		Player p = (Player) e.getDamager();
		if (invencible.containsKey(p.getUniqueId()) && invencible.get(p.getUniqueId()) > System.currentTimeMillis()) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();
		if (invencible.containsKey(p.getUniqueId()) && invencible.get(p.getUniqueId()) > System.currentTimeMillis()) {
			e.setCancelled(true);
		}
	}

	double distance = 3.0D;

	@SuppressWarnings("deprecation")
	private ArrayList<Player> getNearbyPlayers(Player p2, Location portal) {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.equals(p2)) {
				continue;
			}
			if (Main.plugin.isNotPlaying(p))
				continue;
			if (!isEnderable(portal, p.getLocation())) {
				continue;
			}
			if (hasAbility(p) || hasAbility(p, "endercooker")) {
				continue;
			}
			if (Main.plugin.adm.isSpectating(p)) {
				continue;
			}
			if (p.getLocation().getBlockY() > 148) {
				continue;
			}
			players.add(p);
		}
		return players;
	}

	private boolean isEnderable(Location portal, Location player) {
		return (Math.abs(portal.getX() - player.getX()) < 2.0D) && (Math.abs(portal.getZ() - player.getZ()) < 2.0D)
				&& (Math.abs(portal.getY() - player.getY()) > 2.0D);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (portal.contains(e.getPlayer().getName())) {
			final Player p = e.getPlayer();
			new BukkitRunnable() {
				public void run() {
					if (!p.isOnline()) {
						cancel();
					}
					if (Main.plugin.adm.isSpectating(p)) {
						cancel();
					}
					if (!hasAbility(p)) {
						cancel();
					}
					portal.remove(p.getName());
					p.getInventory().setItem(portalVolta.get(p.getUniqueId()),
							createItem(Material.NETHER_BRICK_ITEM, 1, "§rEndermage Portal"));
				}
			}.runTaskLater(Main.plugin, 2L);
		}
	}

	public int calculateDistance(Location a, Location b) {
		int distance = 0;

		int x1 = a.getBlockX();
		int x2 = b.getBlockX();

		int z1 = a.getBlockZ();
		int z2 = b.getBlockZ();

		if (x1 != x2) {
			if (x1 < x2) {
				for (int i = x1; i <= x2 - 1; i++) {
					distance++;
				}
			} else {
				for (int i = x2; i <= x1 - 1; i++) {
					distance++;
				}
			}
		}

		if (z1 != z2) {
			if (z1 < z2) {
				for (int i = z1; i <= z2 - 1; i++) {
					distance++;
				}
			} else {
				for (int i = z2; i <= z1 - 1; i++) {
					distance++;
				}
			}
		}
		return distance;
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.NETHER_BRICK_ITEM, 1, "§rEndermage Portal"));
		return new Kit("endermage",
				Arrays.asList(new String[] {
						"Ao colocar seu portal em um bloco você puxa pessoas emcima ou em baixo de voce teleportando-se e ficando invencivel por 5 segundos" }),
				kitItems, null, new ItemStack(Material.PORTAL));
	}

}
