package me.ghost.hg.packets;

import java.io.IOException;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;

public class PacketPlayOutTitle extends Packet {

	private PacketPlayOutTitle.EnumTitleAction a;
	private IChatBaseComponent b;
	private int c;
	private int d;
	private int e;

	public PacketPlayOutTitle() {
	}

	public PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction packetplayouttitle_enumtitleaction, IChatBaseComponent ichatbasecomponent) {
		this(packetplayouttitle_enumtitleaction, ichatbasecomponent, -1, -1, -1);
	}

	public PacketPlayOutTitle(int i, int j, int k) {
		this(PacketPlayOutTitle.EnumTitleAction.TIMES, (IChatBaseComponent) null, i, j, k);
	}

	public PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction packetplayouttitle_enumtitleaction, IChatBaseComponent ichatbasecomponent, int i, int j, int k) {
		this.a = packetplayouttitle_enumtitleaction;
		this.b = ichatbasecomponent;
		this.c = i;
		this.d = j;
		this.e = k;
	}

	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = PacketPlayOutTitle.EnumTitleAction.values()[packetdataserializer.a()];
		if (this.a == PacketPlayOutTitle.EnumTitleAction.TITLE || this.a == PacketPlayOutTitle.EnumTitleAction.SUBTITLE) {
			this.b = ChatSerializer.a(packetdataserializer.c(32767));
		}

		if (this.a == PacketPlayOutTitle.EnumTitleAction.TIMES) {
			this.c = packetdataserializer.readInt();
			this.d = packetdataserializer.readInt();
			this.e = packetdataserializer.readInt();
		}

	}

	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.b(this.a.ordinal());
		if (this.a == PacketPlayOutTitle.EnumTitleAction.TITLE || this.a == PacketPlayOutTitle.EnumTitleAction.SUBTITLE) {
			packetdataserializer.a(ChatSerializer.a(this.b));
		}

		if (this.a == PacketPlayOutTitle.EnumTitleAction.TIMES) {
			packetdataserializer.writeInt(this.c);
			packetdataserializer.writeInt(this.d);
			packetdataserializer.writeInt(this.e);
		}
	}

	public void handle(PacketListener packetlistener) {}

	public static enum EnumTitleAction {

		TITLE, SUBTITLE, TIMES, CLEAR, RESET;

		private EnumTitleAction() {
		}

		public static PacketPlayOutTitle.EnumTitleAction a(String s) {
			PacketPlayOutTitle.EnumTitleAction[] apacketplayouttitle_enumtitleaction = values();
			int i = apacketplayouttitle_enumtitleaction.length;
			for (int j = 0; j < i; ++j) {
				PacketPlayOutTitle.EnumTitleAction packetplayouttitle_enumtitleaction = apacketplayouttitle_enumtitleaction[j];

				if (packetplayouttitle_enumtitleaction.name().equalsIgnoreCase(s)) {
					return packetplayouttitle_enumtitleaction;
				}
			}
			return PacketPlayOutTitle.EnumTitleAction.TITLE;
		}

		public static String[] a() {
			String[] astring = new String[values().length];
			int i = 0;
			PacketPlayOutTitle.EnumTitleAction[] apacketplayouttitle_enumtitleaction = values();
			int j = apacketplayouttitle_enumtitleaction.length;
			for (int k = 0; k < j; ++k) {
				PacketPlayOutTitle.EnumTitleAction packetplayouttitle_enumtitleaction = apacketplayouttitle_enumtitleaction[k];

				astring[i++] = packetplayouttitle_enumtitleaction.name().toLowerCase();
			}
			return astring;
		}
	}
}
