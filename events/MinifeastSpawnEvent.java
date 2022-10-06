package me.ghost.hg.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinifeastSpawnEvent extends Event {
	public static final HandlerList handlers = new HandlerList();
	private int x;
	private int y;
	private int z;

	public MinifeastSpawnEvent(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
