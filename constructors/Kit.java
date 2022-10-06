package me.ghost.hg.constructors;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Kit {
	public String name;
	public List<ItemStack> items;
	public List<ItemStack> itemstodrop;
	public List<String> kitInfo;
	public ItemStack icon;

	public Kit(String kitname, List<String> kitInfo, List<ItemStack> kititems, List<ItemStack> kititemstodrop,
			ItemStack icon) {
		name = kitname.toLowerCase();
		this.kitInfo = kitInfo;
		items = kititems;
		itemstodrop = kititemstodrop;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public List<ItemStack> getItemsToDrop() {
		return itemstodrop;
	}

	public List<String> getKitInfo() {
		return kitInfo;
	}

	public ItemStack getIcon() {
		return icon;
	}

}
