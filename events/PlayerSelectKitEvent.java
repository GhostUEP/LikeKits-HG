package me.ghost.hg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSelectKitEvent extends Event {
	public static final HandlerList handlers = new HandlerList();
	private Player player;
	private String kit;

	public PlayerSelectKitEvent(Player player, String kit) {
		this.player = player;
		this.kit = kit;
	}

	public Player getPlayer() {
		return player;
	}

	public String getKit() {
		return kit;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
