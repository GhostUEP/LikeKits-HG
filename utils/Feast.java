package me.ghost.hg.utils;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.BO3Blocks;

public class Feast {
	public int time = 300;
	private int radius = 25;
	public Block central;
	public boolean spawned = false;
	public ArrayList<BO3Blocks> structure;
	public ArrayList<BO3Blocks> cheststructure;
	public ArrayList<Block> feastBlocks = new ArrayList<>();
	public ArrayList<Block> chests = new ArrayList<>();
	public ArrayList<ItemStack> feastItems;

	public Feast(Main m, int spawnRadius, int radius, boolean useStructure) {
		if (useStructure) {
			this.structure = m.structure;
			this.cheststructure = m.cheststructure;
		}
		this.radius = radius;
		Random r = new Random();
		World w = m.getServer().getWorld("world");
		int x = -spawnRadius + r.nextInt(2 * spawnRadius);
		int z = -spawnRadius + r.nextInt(2 * spawnRadius);
		int y = w.getHighestBlockYAt(x, z);
		central = new Location(w, x, y, z).getBlock();
	}

	@SuppressWarnings("deprecation")
	public void generateFeast() {
		spawned = true;
		if (structure != null) {
			Location loc = central.getLocation();
			loc.getChunk().load(true);
			cleanArea(loc, 25, 25, 50);
			for (BO3Blocks bo3 : structure) {
				central.getLocation().getChunk().load(true);
				Block b = new Location(loc.getWorld(), loc.getX() + bo3.getX(), loc.getY() + bo3.getY(),
						loc.getZ() + bo3.getZ()).getBlock();
				b.setType(bo3.getType());
				b.setData(bo3.getData());
				feastBlocks.add(b);
				int y = 0;
				while (y <= 11) {
					Location l = new Location(loc.getWorld(), loc.getX() + bo3.getX(), loc.getY() + bo3.getY() + y,
							loc.getZ() + bo3.getZ());
					y++;
					feastBlocks.add(l.getBlock());
				}
			}

		} else {
			central.getLocation().getChunk().load(true);
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					Location l = central.getLocation().add(x, 0, z);
					if (central.getLocation().distance(l) < 20) {
						l.getBlock().setType(Material.GRASS);
						feastBlocks.add(l.getBlock());
						int y = 1;
						while (y <= 11) {
							Location loc = central.getLocation().add(x, y, z);
							loc.getBlock().setType(Material.AIR);
							y++;
							feastBlocks.add(l.getBlock());
						}
					}
				}
			}
		}
	}

	public void cleanArea(Location location, int posx, int posz, int posy) {
		for (int x = -posx; x <= posx; x++) {
			for (int z = -posz; z <= posz; z++) {
				if (location.clone().add(x, 0, z).distance(location) > 65.0D) {
					continue;
				}
				for (int y = 0; y <= posy; y++) {
					AntiLagRemover.quickChangeBlockAt(location.clone().add(x, y, z).getBlock().getLocation(),
							Material.AIR);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void generateChests() {
		new FeastItems(this);
		if (cheststructure != null) {
			final Location loc = central.getLocation();
			loc.setY(loc.getY() + 2);
			for (BO3Blocks bo3 : cheststructure) {
				Block b = new Location(loc.getWorld(), loc.getX() + bo3.getX(), loc.getY() + bo3.getY(),
						loc.getZ() + bo3.getZ()).getBlock();
				b.setType(bo3.getType());
				b.setData(bo3.getData());
				if (bo3.getType() == Material.CHEST)
					chests.add(b);
			}
		} else {
			central.getLocation().add(0, 1, 0).getBlock().setType(Material.ENCHANTMENT_TABLE);
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					Location loc = central.getLocation().add(x, 1, z);
					if (!((x == 0 && (z == 1 || z == -1)) || ((x == 1 || x == -1) && z == 0)
							|| ((x == 1 || x == -1) && (z == 2 || z == -2))
							|| ((x == 2 || x == -2) && (z == 1 || z == -1)) || (x == 0 && z == 0))) {
						loc.getBlock().setType(Material.CHEST);
						if (loc.getBlock().getType() == Material.CHEST) {
							chests.add(loc.getBlock());
						}
					}
				}
			}
		}
		spawnItems();
	}

	public void spawnItems() {
		if (feastItems == null) {
			return;
		}
		for (Block b : chests) {
			if (b.getState() instanceof Chest) {
				Chest chest = (Chest) b.getState();
				Inventory inv = chest.getBlockInventory();
				int items = 15;
				Random r = new Random();
				int size = feastItems.size() - 1;
				if (size <= 0)
					return;
				while (items > 0) {
					int i = r.nextInt(size);
					if (feastItems.size() - 1 < i) {
						items--;
						continue;
					}
					ItemStack item = feastItems.get(i);
					if (item != null && item.getType() != Material.AIR && item.getAmount() > 0) {
						inv.addItem(item);
						chest.update();
						feastItems.remove(i);
					}
					items--;
				}
			}
		}
	}

	public boolean isSpawned() {
		return spawned;
	}

	public boolean isFeastBlock(Block b) {
		return feastBlocks.contains(b) && time > 0;
	}
}
