package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Digger extends KitInterface {

	public Digger(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		ItemStack item = event.getItemInHand();
		if (event.getBlock().getY() > 128 || Math.abs(event.getBlock().getX()) > 489
				|| Math.abs(event.getBlock().getZ()) > 489) {
			event.setBuild(false);
			return;
		}
		if (hasAbility(event.getPlayer()) && isKitItem(item, Material.DRAGON_EGG, "§r§5Digger Egg")) {
			final Block b = event.getBlock();
			b.setType(Material.AIR);
			event.getPlayer().sendMessage("§cVocê colocou um §5Ovo§c CORRA!");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				public void run() {
					int dist = (int) Math.ceil((5 - 1) / 2.0D);
					for (int y = -1; y >= -5; y--) {
						for (int x = -dist; x <= dist; x++) {
							for (int z = -dist; z <= dist; z++) {
								if (b.getY() + y > 0) {
									Block block = b.getWorld().getBlockAt(b.getX() + x, b.getY() + y, b.getZ() + z);
									if (block.getType() != Material.BEDROCK) {
										block.setType(Material.AIR);
									}
								}
							}
						}
					}
				}
			}, 30L);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.DRAGON_EGG, 15, "§r§5Digger Egg"));
		return new Kit("digger", Arrays.asList(
				new String[] { "Ao colocar um ovo no chao, depois de 1,5 Segundos ira criar um buraco 5x5x5" }),
				kitItems, null, new ItemStack(Material.DRAGON_EGG));
	}

}
