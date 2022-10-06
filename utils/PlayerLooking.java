package me.ghost.hg.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import me.ghost.hg.Main;

public class PlayerLooking {
	private static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
		final double epsilon = 0.0001f;

		Vector3D d = p2.subtract(p1).multiply(0.5);
		Vector3D e = max.subtract(min).multiply(0.5);
		Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
		Vector3D ad = d.abs();

		if (Math.abs(c.x) > e.x + ad.x)
			return false;
		if (Math.abs(c.y) > e.y + ad.y)
			return false;
		if (Math.abs(c.z) > e.z + ad.z)
			return false;

		if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
			return false;
		if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
			return false;
		if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
			return false;

		return true;
	}

	public static LivingEntity getTarget(Player player) {
		List<Entity> nearbyE = player.getNearbyEntities(6, 6, 6);
		ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();
		for (Entity e : nearbyE) {
			if (e instanceof LivingEntity) {
				livingE.add((LivingEntity) e);
				break;
			}
		}
		LivingEntity target = null;
		BlockIterator bItr = new BlockIterator(player, 6);
		Block block;
		Location loc;
		int bx, by, bz;
		double ex, ey, ez;
		while (bItr.hasNext()) {
			block = bItr.next();
			bx = block.getX();
			by = block.getY();
			bz = block.getZ();
			for (LivingEntity e : livingE) {
				loc = e.getLocation();
				ex = loc.getX();
				ey = loc.getY();
				ez = loc.getZ();
				if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75)
						&& (by - 1 <= ey && ey <= by + 2.5)) {
					if (e instanceof Player) {
						target = e;
						break;
					}
				}
			}
		}
		return target;
	}

	public static Player getEntityInSight(Player p) {
		Location observerPos = p.getEyeLocation();
		Vector3D observerDir = new Vector3D(observerPos.getDirection());
		Vector3D observerStart = new Vector3D(observerPos);
		Vector3D observerEnd = observerStart.add(observerDir.multiply(6));
		Player hit = null;
		for (Entity entity : p.getNearbyEntities(6, 6, 6)) {
			if (!(entity instanceof Player))
				continue;
			Player pla = (Player) entity;
			if (Main.plugin.isNotPlaying(pla))
				continue;
			Vector3D targetPos = new Vector3D(pla.getLocation());
			Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
			Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);
			if (hasIntersection(observerStart, observerEnd, minimum, maximum)) {
				if (hit == null || hit.getLocation().distanceSquared(observerPos) > pla.getLocation()
						.distanceSquared(observerPos)) {
					hit = pla;
					break;
				}
			}
		}
		return hit;
	}

	public static class Vector3D {
		private final double x;
		private final double y;
		private final double z;

		private Vector3D(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		private Vector3D(Location location) {
			this(location.toVector());
		}

		private Vector3D(Vector vector) {
			if (vector == null)
				throw new IllegalArgumentException("Vector cannot be NULL.");
			this.x = vector.getX();
			this.y = vector.getY();
			this.z = vector.getZ();
		}

		private Vector3D abs() {
			return new Vector3D(Math.abs(x), Math.abs(y), Math.abs(z));
		}

		private Vector3D add(double x, double y, double z) {
			return new Vector3D(this.x + x, this.y + y, this.z + z);
		}

		private Vector3D add(Vector3D other) {
			if (other == null)
				throw new IllegalArgumentException("other cannot be NULL");
			return new Vector3D(x + other.x, y + other.y, z + other.z);
		}

		private Vector3D multiply(double factor) {
			return new Vector3D(x * factor, y * factor, z * factor);
		}

		private Vector3D multiply(int factor) {
			return new Vector3D(x * factor, y * factor, z * factor);
		}

		private Vector3D subtract(Vector3D other) {
			if (other == null)
				throw new IllegalArgumentException("other cannot be NULL");
			return new Vector3D(x - other.x, y - other.y, z - other.z);
		}
	}
}
