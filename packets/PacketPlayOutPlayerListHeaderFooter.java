package me.ghost.hg.packets;

import java.io.IOException;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;

public class PacketPlayOutPlayerListHeaderFooter extends Packet {

	private IChatBaseComponent a;
	private IChatBaseComponent b;

	public PacketPlayOutPlayerListHeaderFooter() {
	}

	public PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent ichatbasecomponent) {
		this.a = ichatbasecomponent;
	}

	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = ChatSerializer.a(packetdataserializer.c(32767));
		this.b = ChatSerializer.a(packetdataserializer.c(32767));
	}

	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.a(ChatSerializer.a(this.a));
		packetdataserializer.a(ChatSerializer.a(this.b));
	}

	public void handle(PacketListener packetlistener) {
	}
}
