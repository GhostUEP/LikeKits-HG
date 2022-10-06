package me.ghost.hg.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.ghost.hg.Main;
import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

public class Vortex {

	public String particle;
	public float radius = 2.0F;
	public float grow = 0.5F;
	public double radials = 0.19634954084936207D;
	public int circles = 4;
	public int helixes = 4;
	protected int step = 0;
	public boolean cancelled = false;

	public void effect(Location location) {
		for (int x = 0; x < this.circles; x++) {
			for (int i = 0; i < this.helixes; i++) {
				double angle = this.step * this.radials + 6.283185307179586D * i / this.helixes;
				Vector v = new Vector(Math.cos(angle) * this.radius, 0 * this.grow, Math.sin(angle) * this.radius);
				VectorUtils.rotateAroundAxisX(v, (location.getPitch() + 90.0F) * 0.017453292F);
				VectorUtils.rotateAroundAxisY(v, -location.getYaw() * 0.017453292F);

				display(this.particle, location.clone().add(v));
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void display(String particle, Location location) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, (float) (location.getX()),
				(float) (location.getY()), (float) (location.getZ()), 0, 0, 0, 0, 1);

		for (Player player : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void playVortex(final Vortex vor, final Player player) {
		new BukkitRunnable() {

			double t = 0;

			Vector v = player.getEyeLocation().getDirection().normalize();
			Location location = player.getEyeLocation();

			@Override
			public void run() {

				if (vor.cancelled) {
					cancel();
					return;
				}

				double x = v.getX() * t;
				double y = v.getY() * t;
				double z = v.getZ() * t;

				Location display = location.add(x, y, z);

				vor.effect(display);

				display.subtract(x, y, z);

				t += 0.5;
				if (t > 45) {
					cancel();
					player.sendMessage("STOP!");
				}
			}
		}.runTaskTimer(Main.plugin, 0, 1);
	}

	public static class VectorUtils {

		public static Vector rotateAroundAxisX(Vector v, double angle) {
			double cos = Math.cos(angle);
			double sin = Math.sin(angle);
			double y = v.getY() * cos - v.getZ() * sin;
			double z = v.getY() * sin + v.getZ() * cos;
			return v.setY(y).setZ(z);
		}

		public static Vector rotateAroundAxisY(Vector v, double angle) {
			double cos = Math.cos(angle);
			double sin = Math.sin(angle);
			double x = v.getX() * cos + v.getZ() * sin;
			double z = v.getX() * -sin + v.getZ() * cos;
			return v.setX(x).setZ(z);
		}

		public static Vector rotateAroundAxisZ(Vector v, double angle) {
			double cos = Math.cos(angle);
			double sin = Math.sin(angle);
			double x = v.getX() * cos - v.getY() * sin;
			double y = v.getX() * sin + v.getY() * cos;
			return v.setX(x).setY(y);
		}

		public static Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
			rotateAroundAxisX(v, angleX);
			rotateAroundAxisY(v, angleY);
			rotateAroundAxisZ(v, angleZ);
			return v;
		}

		public static double angleToXAxis(Vector vector) {
			return Math.atan2(vector.getX(), vector.getY());
		}
	}

	public void onPlay(Location location) {
		// TODO Auto-generated method stub

	}
}
