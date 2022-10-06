package me.ghost.hg.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class WorldEditAPI {
	public static HashMap<UUID, List<BlockState>> lastChange = new HashMap<>();
	public static HashMap<UUID, List<BlockState>> nextChange = new HashMap<>();

	@SuppressWarnings("deprecation")
	public static int set(UUID uuid, Wand wand, Material mat, byte id) {
		int mudados = 0;
		List<BlockState> list = new ArrayList<BlockState>();
		for (double x = wand.getSmallerX(); x <= wand.getBiggerX(); x++) {
			for (double z = wand.getSmallerZ(); z <= wand.getBiggerZ(); z++) {
				for (double y = wand.getSmallerY(); y <= wand.getBiggerY(); y++) {
					Location l = new Location(wand.getFirst().getWorld(), x, y, z);
					if (l.getBlock().getType() != mat || l.getBlock().getData() != id) {
						l.getBlock().setType(mat);
						l.getBlock().setData(id);
						list.add(l.getBlock().getState());
						++mudados;
					}
				}
			}
		}
		if (uuid != null) {
			lastChange.put(uuid, list);
			nextChange.remove(uuid);
		}
		return mudados;
	}

	@SuppressWarnings("deprecation")
	public static int setWalls(UUID uuid, Wand wand, Material mat, byte id) {
		int mudados = 0;
		List<BlockState> list = new ArrayList<BlockState>();
		for (double x = wand.getSmallerX(); x <= wand.getBiggerX(); x++) {
			for (double z = wand.getSmallerZ(); z <= wand.getBiggerZ(); z++) {
				for (double y = wand.getSmallerY(); y <= wand.getBiggerY(); y++) {
					if (x != wand.getSmallerX() && x != wand.getBiggerX() && z != wand.getSmallerZ()
							&& z != wand.getBiggerZ())
						continue;
					Location l = new Location(wand.getFirst().getWorld(), x, y, z);
					if (l.getBlock().getType() != mat || l.getBlock().getData() != id) {
						l.getBlock().setType(mat);
						l.getBlock().setData(id);
						list.add(l.getBlock().getState());
						++mudados;
					}
				}
			}
		}
		if (uuid != null) {
			lastChange.put(uuid, list);
			nextChange.remove(uuid);
		}
		return mudados;
	}

	public static void createArenaQuadrado(Location loc, int comprimentoX, int altura, int comprimentoZ) {
		Block mainBlock = loc.getBlock();
		for (double x = -comprimentoX; x <= comprimentoX; x++) {
			for (double z = -comprimentoZ; z <= comprimentoZ; z++) {
				for (double y = 0; y <= altura; y++) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, mainBlock.getY() + y,
							mainBlock.getZ() + z);
					l.getBlock().setType(Material.BEDROCK);
				}
			}
		}
		int cXMenor = comprimentoX - 1;
		int cZMenor = comprimentoZ - 1;
		for (double x = -cXMenor; x <= cXMenor; x++) {
			for (double z = -cZMenor; z <= cZMenor; z++) {
				for (double y = 1; y <= altura; y++) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, mainBlock.getY() + y,
							mainBlock.getZ() + z);
					if (l.getBlock().getType() != Material.AIR)
						l.getBlock().setType(Material.AIR);
				}
			}
		}
	}

	public static void createArenaCirculo(Location loc, int radius, int altura) {
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (double y = 0; y <= altura; y++) {
					Block mainBlock = loc.clone().add(0, altura, 0).getBlock();
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, mainBlock.getY() + y,
							mainBlock.getZ() + z);
					if (y == 0) {
						if (mainBlock.getLocation().distance(l) <= radius) {
							l.getBlock().setType(Material.BEDROCK);
						}
					} else {
						if (mainBlock.getLocation().distance(l) <= radius
								&& mainBlock.getLocation().distance(l) >= radius - 2) {
							l.getBlock().setType(Material.BEDROCK);
						}
					}
				}
			}
		}
	}

	public static void undo(UUID uuid) {

	}

	public static void redo(UUID uuid) {

	}

}
