package me.ghost.hg.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftChunk;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import net.minecraft.server.v1_7_R4.Block;

public class AntiLagRemover {

	public static void quickChangeBlockAt(Location location, Material setTo) {
		quickChangeBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
				setTo);
	}

	public static void quickChangeBlockAt(Location location, int id, byte data) {
		quickChangeBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), id,
				data);
	}

	public static void quickChangeBlockAt(World world, int x, int y, int z, Material setTo) {
		quickChangeBlockAt(world, x, y, z, setTo, 0);
	}

	@SuppressWarnings("deprecation")
	public static void quickChangeBlockAt(World world, int x, int y, int z, Material setTo, int data) {
		quickChangeBlockAt(world, x, y, z, setTo.getId(), data);
	}

	public static void quickChangeBlockAt(World world, int x, int y, int z, int id, int data) {
		Chunk chunk = world.getChunkAt(x >> 4, z >> 4);
		net.minecraft.server.v1_7_R4.Chunk c = ((CraftChunk) chunk).getHandle();

		c.a(x & 0xF, y, z & 0xF, Block.getById(id), data);
		((CraftWorld) world).getHandle().notify(x, y, z);
	}

}
