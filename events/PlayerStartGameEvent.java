package me.ghost.hg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerStartGameEvent extends Event {
	public static final HandlerList handlers = new HandlerList();
	private Player player;

	public PlayerStartGameEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
