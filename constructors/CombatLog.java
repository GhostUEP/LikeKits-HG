package me.ghost.hg.constructors;

import java.util.UUID;

public class CombatLog {
	private UUID combatLogged;
	private long time;

	public CombatLog(UUID combatLogged) {
		this.combatLogged = combatLogged;
		this.time = System.currentTimeMillis() + 10000;
	}

	public UUID getCombatLogged() {
		return combatLogged;
	}

	public long getTime() {
		return time;
	}

	public void hitted(UUID uuid) {
		this.combatLogged = uuid;
		this.time = System.currentTimeMillis() + 10000;
	}

}
