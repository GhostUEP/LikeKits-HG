package me.ghost.hg.utils;

import org.bukkit.Location;

public class Wand {
	private Location first;
	private Location second;

	public Wand(Location first, Location second) {
		this.first = first;
		this.second = second;
	}

	public Location getFirst() {
		return first;
	}

	public void setFirst(Location first) {
		this.first = first;
	}

	public Location getSecond() {
		return second;
	}

	public void setSecond(Location second) {
		this.second = second;
	}

	public double getBiggerX() {
		if (getFirst().getX() > getSecond().getX()) {
			return getFirst().getX();
		} else {
			return getSecond().getX();
		}
	}

	public double getBiggerY() {
		if (getFirst().getY() > getSecond().getY()) {
			return getFirst().getY();
		} else {
			return getSecond().getY();
		}
	}

	public double getBiggerZ() {
		if (getFirst().getZ() > getSecond().getZ()) {
			return getFirst().getZ();
		} else {
			return getSecond().getZ();
		}
	}

	public double getSmallerX() {
		if (getFirst().getX() < getSecond().getX()) {
			return getFirst().getX();
		} else {
			return getSecond().getX();
		}
	}

	public double getSmallerY() {
		if (getFirst().getY() < getSecond().getY()) {
			return getFirst().getY();
		} else {
			return getSecond().getY();
		}
	}

	public double getSmallerZ() {
		if (getFirst().getZ() < getSecond().getZ()) {
			return getFirst().getZ();
		} else {
			return getSecond().getZ();
		}
	}

	public double getBlockQuantidade() {
		if (getFirst() == null)
			return 0;
		if (getSecond() == null)
			return 0;
		double diffX = getBiggerX() - getSmallerX() + 1;
		double diffY = getBiggerY() - getSmallerY() + 1;
		double diffZ = getBiggerZ() - getSmallerZ() + 1;
		return diffX * diffY * diffZ;
	}

}
