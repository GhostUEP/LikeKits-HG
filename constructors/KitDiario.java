package me.ghost.hg.constructors;

import java.util.UUID;

public class KitDiario {
	private UUID uuid;
	private long expire;
	private String kit;

	public KitDiario(UUID uuid, long expire, String kit) {
		this.uuid = uuid;
		this.expire = expire;
		this.kit = kit;
	}

	public UUID getUuid() {
		return uuid;
	}

	public long getExpire() {
		return expire;
	}

	public String getKit() {
		return kit;
	}

	public void setKit(String kit) {
		this.kit = kit;
	}

	public void addLong(long l) {
		this.expire = l;
	}

}
