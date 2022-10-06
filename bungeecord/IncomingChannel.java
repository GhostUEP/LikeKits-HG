package me.ghost.hg.bungeecord;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import me.ghost.hg.Main;

public class IncomingChannel implements PluginMessageListener {
	// private Main main;

	public IncomingChannel(Main main) {
		// this.main = main;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord"))
			return;
		// ByteArrayDataInput in = ByteStreams.newDataInput(message);
		// String subchannel = in.readUTF();
	}

}
