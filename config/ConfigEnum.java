package me.ghost.hg.config;

public enum ConfigEnum {
	CONFIG("config.yml");

	private String file;

	private ConfigEnum(String file) {
		this.file = file;
	}

	public String getFile() {
		return this.file;
	}
}
