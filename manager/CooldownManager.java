package me.ghost.hg.manager;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

	private static HashMap<String, CooldownManager> cooldowns = new HashMap<String, CooldownManager>();
	private long start;
	private final int timeInSeconds;
	private final UUID id;
	private final String cooldownName;

	public CooldownManager(UUID id, String cooldownName, int timeInSeconds) {
		this.id = id;
		this.cooldownName = cooldownName;
		this.timeInSeconds = timeInSeconds;
	}

	public static boolean isInCooldown(UUID id, String cooldownName) {
		if (getTimeLeft(id, cooldownName) >= 1) {
			return true;
		} else {
			stop(id, cooldownName);
			return false;
		}
	}

	public static void stop(UUID id, String cooldownName) {
		CooldownManager.cooldowns.remove(id + cooldownName);
	}

	private static CooldownManager getCooldown(UUID id, String cooldownName) {
		return cooldowns.get(id.toString() + cooldownName);
	}

	public static int getTimeLeft(UUID id, String cooldownName) {
		CooldownManager cooldown = getCooldown(id, cooldownName);
		int f = -1;
		if (cooldown != null) {
			long now = System.currentTimeMillis();
			long cooldownTime = cooldown.start;
			int totalTime = cooldown.timeInSeconds;
			int r = (int) (now - cooldownTime) / 1000;
			f = (r - totalTime) * (-1);
		}
		return f;
	}

	public void start() {
		this.start = System.currentTimeMillis();
		cooldowns.put(this.id.toString() + this.cooldownName, this);
	}

}
