package me.ghost.hg.utils;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

public enum Particulas {
	HUGE_EXPLOSION("hugeexplosion"),
	LARGE_EXPLODE("largeexplode"),
	FIREWORKS_SPARK("fireworksSpark"),
	BUBBLE("bubble"),
	SUSPEND("suspend"),
	DEPTH_SUSPEND("depthSuspend"),
	TOWN_AURA("townaura"),
	CRIT("crit"),
	MAGIC_CRIT("magicCrit"),
	MOB_SPELL("mobSpell"),
	MOB_SPELL_AMBIENT("mobSpellAmbient"),
	SPELL("spell"),
	INSTANT_SPELL("instantSpell"),
	WITCH_MAGIC("witchMagic"),
	NOTE("note"),
	PORTAL("portal"),
	ENCHANTMENT_TABLE("enchantmenttable"),
	EXPLODE("explode"),
	FLAME("flame"),
	LAVA("lava"),
	FOOTSTEP("footstep"),
	SPLASH("splash"),
	LARGE_SMOKE("largesmoke"),
	CLOUD("cloud"),
	RED_DUST("reddust"),
	SNOWBALL_POOF("snowballpoof"),
	DRIP_WATER("dripWater"),
	DRIP_LAVA("dripLava"),
	SNOW_SHOVEL("snowshovel"),
	SLIME("slime"),
	HEART("heart"),
	ANGRY_VILLAGER("angryVillager"),
	HAPPY_VILLAGER("happerVillager"),
	ICONCRACK("iconcrack_"),
	TILECRACK("tilecrack_");

	private String particleName;

	private Particulas(String particleName) {
		this.particleName = particleName;
	}

	public void part(Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed,
			int count) throws Exception {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles();
		setValue(packet, "a", this.particleName);
		setValue(packet, "b", Float.valueOf((float) location.getX()));
		setValue(packet, "c", Float.valueOf((float) location.getY()));
		setValue(packet, "d", Float.valueOf((float) location.getZ()));
		setValue(packet, "e", Float.valueOf(offsetX));
		setValue(packet, "f", Float.valueOf(offsetY));
		setValue(packet, "g", Float.valueOf(offsetZ));
		setValue(packet, "h", Float.valueOf(speed));
		setValue(packet, "i", Integer.valueOf(count));
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	private void setValue(Object instance, String fieldName, Object value) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(instance, value);
	}

	@SuppressWarnings("deprecation")
	public void part(Location loc, float ofX, float ofY, float ofZ, float speed, int amount) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particleName, (float) loc.getX(),
				(float) loc.getY(), (float) loc.getZ(), ofX, ofY, ofZ, speed, amount);
		for (Player player : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static Particulas get(String string) {
		Particulas p = HUGE_EXPLOSION;
		p.particleName = string;
		return p;
	}

	public void send(Player p, Location loc, float ofX, float ofY, float ofZ, float speed, int amount) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particleName, (float) loc.getX(),
				(float) loc.getY(), (float) loc.getZ(), ofX, ofY, ofZ, speed, amount);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
}
