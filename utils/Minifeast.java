package me.ghost.hg.utils;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.BO3Blocks;
import me.ghost.hg.events.MinifeastSpawnEvent;

public class Minifeast {
	public int time = 300;
	private Block central;
	private Main m;
	public ArrayList<BO3Blocks> structure;
	public ArrayList<Block> chests = new ArrayList<>();
	public ArrayList<ItemStack> miniItems;
	public ArrayList<String> viableKits;

	public Minifeast(Main m) {
		this.structure = m.ministructure;
		viableKits = new ArrayList<>();
		Random r = new Random();
		this.m = m;
		World w = m.getServer().getWorld("world");
		int size = (int) 1000 / 2;
		int x = (size / 2) + r.nextInt(size / 2);
		int z = (size / 2) + r.nextInt(size / 2);
		if (r.nextBoolean())
			x = -x;
		if (r.nextBoolean())
			z = -z;
		int y = w.getHighestBlockYAt(x, z);
		central = new Location(w, x, y, z).getBlock();
		System.out.print("Minifeast: " + x + " " + y + " " + z);
		miniFeastItems();
		generateMinifeast();
		Main.plugin.getServer().getPluginManager().callEvent(new MinifeastSpawnEvent(x, y, z));
	}

	@SuppressWarnings("deprecation")
	public void generateMinifeast() {
		Location loc = central.getLocation();
		for (BO3Blocks bo3 : structure) {
			Block b = new Location(loc.getWorld(), loc.getX() + bo3.getX(), loc.getY() + bo3.getY(),
					loc.getZ() + bo3.getZ()).getBlock();
			Chunk chunk = loc.getBlock().getChunk();
			if (!chunk.isLoaded())
				chunk.load();
			b.setType(bo3.getType());
			b.setData(bo3.getData());
			if (bo3.getType() == Material.CHEST)
				chests.add(b);
		}
		spawnItems();
		int realX = central.getX();
		int realZ = central.getZ();
		int x = realX;
		int z = realZ;
		while (x % 50 != 0) {
			x++;
		}
		while (z % 50 != 0) {
			z++;
		}
		m.getServer().broadcastMessage(
				"§cUm minifeast apareceu em: (X: " + x + " e " + (x - 100) + ") e (Z: " + z + " e " + (z - 100) + ")");
	}

	public void spawnItems() {
		if (miniItems == null) {
			return;
		}
		for (Block b : chests) {
			if (b.getState() instanceof Chest) {
				Chest chest = (Chest) b.getState();
				Inventory inv = chest.getBlockInventory();
				int items = 20;
				Random r = new Random();
				int size = miniItems.size() - 1;
				while (items > 0) {
					int i = r.nextInt(size);
					if (miniItems.size() - 1 < i) {
						items--;
						continue;
					}
					ItemStack item = miniItems.get(i);
					if (item != null && item.getType() != Material.AIR && item.getAmount() > 0) {
						inv.addItem(item);
						chest.update();
						miniItems.remove(i);
					}
					items--;
				}
			}
		}
	}

	public void miniFeastItems() {
		ArrayList<ItemStack> items = new ArrayList<>();
		for (String kit : m.kit.kits) {
			if (m.kit.items.get(kit.toLowerCase()).items != null && !m.kit.items.get(kit.toLowerCase()).items.isEmpty()
					&& m.kit.items.get(kit.toLowerCase()).items.size() > 0) {
				viableKits.add(kit);
			}
		}
		for (int i = 0; i <= 2; i++) {
			items.add(new ItemStack(Material.IRON_SWORD));
		}
		for (int i = 0; i <= 5; i++) {
			items.add(new ItemStack(Material.DIAMOND));
		}
		for (int i = 0; i <= 10; i++) {
			items.add(new ItemStack(Material.IRON_INGOT));
		}
		for (int i = 0; i <= 32; i++) {
			items.add(new ItemStack(Material.COOKED_BEEF));
		}
		for (int i = 0; i <= 6; i++) {
			items.add(new ItemStack(Material.INK_SACK, 1, (short) 3));
		}
		for (int i = 0; i <= 10; i++) {
			items.add(new ItemStack(Material.MUSHROOM_SOUP));
		}
		for (int i = 0; i <= 10; i++) {
			items.add(new ItemStack(Material.TNT));
		}
		items.add(new ItemStack(Material.FLINT_AND_STEEL));
		for (int i = 0; i <= 20; i++) {
			Random r = new Random();
			if (viableKits != null) {
				int size = viableKits.size() - 1;
				if (size >= 0) {
					int is = 0;
					if (size > 0)
						is = r.nextInt(size);
					String kit = "";
					if (is <= size) {
						if (viableKits.size() - 1 < is) {
							continue;
						}
						ItemStack clay = new ItemStack(Material.WOOL, 1, (short) r.nextInt(15));
						ItemMeta clayMeta = clay.getItemMeta();
						if (viableKits.get(is) != null) {
							kit = viableKits.get(is);
						}
						clayMeta.setDisplayName("Presente para o kit: " + m.kit.getKitName(kit));
						ArrayList<String> lore = new ArrayList<>();
						lore.add("§7Clique com o direito");
						clayMeta.setLore(lore);
						clay.setItemMeta(clayMeta);
						items.add(clay);
						viableKits.remove(kit);
					}
				}
			}
		}
		for (int i = 0; i <= 10; i++) {
			items.add(new ItemStack(Material.EXP_BOTTLE));
		}
		for (int i = 0; i <= 1; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16386));
		}
		for (int i = 0; i <= 1; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16388));
		}
		for (int i = 0; i <= 1; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16394));
		}
		for (int i = 0; i <= 1; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16396));
		}
		for (int i = 0; i <= 1; i++) {
			items.add(new ItemStack(Material.COMPASS));
		}
		miniItems = items;
	}
}