package me.ghost.hg.constructors;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BO3Blocks {
	private int x;
	private int y;
	private int z;
	private Material type;
	private byte data;

	@SuppressWarnings("deprecation")
	public BO3Blocks(Block b) {
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.type = b.getType();
		this.data = b.getData();
	}

	public BO3Blocks(int x, int y, int z, Material type, byte data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.data = data;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public Material getType() {
		return type;
	}

	public byte getData() {
		return data;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BO3Blocks))
			return false;
		BO3Blocks bo3 = (BO3Blocks) obj;
		if (bo3.type != type)
			return false;
		if (bo3.data != data)
			return false;
		if (bo3.x != x)
			return false;
		if (bo3.y != y)
			return false;
		if (bo3.z != z)
			return false;
		return true;
	}
}
