package me.ghost.hg.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class VelocityAPI {

	public static void setVelocityToTarget(Entity start, Entity target, double velocity) {
		Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(),
				target.getLocation().getZ());
		double g = velocity;
		double d = loc.distance(start.getLocation());
		double t = d;
		double v_x = -(g - (loc.getX() - start.getLocation().getX()) / t);
		double v_y = -(g - (loc.getY() - start.getLocation().getY()) / t);
		double v_z = -(g - (loc.getZ() - start.getLocation().getZ()) / t);
		Vector v = start.getVelocity();
		v.setX(v_x);
		v.setY(v_y);
		v.setZ(v_z);
		start.setVelocity(v);
	}
}
