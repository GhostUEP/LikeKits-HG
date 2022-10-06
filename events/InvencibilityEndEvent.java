package me.ghost.hg.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InvencibilityEndEvent extends Event {
	public static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
