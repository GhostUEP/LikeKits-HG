package me.ghost.hg.enums;

public enum KitCategory {
	OWNED((short) 5), ALL((short) 14);

	private short id;

	private KitCategory() {
		this.id = 0;
	}

	private KitCategory(short id) {
		this.id = id;
	}

	public short getId() {
		return id;
	}
}
