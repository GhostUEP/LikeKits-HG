package me.ghost.hg.utils;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FeastItems {
	public FeastItems(Feast feast) {
		ArrayList<ItemStack> items = new ArrayList<>();
		for (int i = 0; i <= 4; i++) {
			items.add(new ItemStack(Material.DIAMOND_SWORD));
		}
		for (int i = 0; i <= 10; i++) {
			items.add(new ItemStack(Material.DIAMOND));
		}
		for (int i = 0; i <= 4; i++) {
			items.add(new ItemStack(Material.DIAMOND_HELMET));
		}
		for (int i = 0; i <= 4; i++) {
			items.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
		}
		for (int i = 0; i <= 4; i++) {
			items.add(new ItemStack(Material.DIAMOND_LEGGINGS));
		}
		for (int i = 0; i <= 4; i++) {
			items.add(new ItemStack(Material.DIAMOND_BOOTS));
		}
		for (int i = 0; i <= 64; i++) {
			items.add(new ItemStack(Material.COOKED_BEEF));
		}
		for (int i = 0; i <= 64; i++) {
			items.add(new ItemStack(Material.COOKED_BEEF));
		}
		for (int i = 0; i <= 6; i++) {
			items.add(new ItemStack(Material.WATER_BUCKET));
		}
		for (int i = 0; i <= 6; i++) {
			items.add(new ItemStack(Material.LAVA_BUCKET));
		}
		for (int i = 0; i <= 20; i++) {
			items.add(new ItemStack(Material.MUSHROOM_SOUP));
		}
		for (int i = 0; i <= 10; i++) {
			items.add(new ItemStack(Material.ENDER_PEARL));
		}
		for (int i = 0; i <= 64; i++) {
			items.add(new ItemStack(Material.TNT));
		}
		for (int i = 0; i <= 4; i++) {
			items.add(new ItemStack(Material.FLINT_AND_STEEL));
		}
		for (int i = 0; i <= 64; i++) {
			items.add(new ItemStack(Material.ARROW));
		}
		for (int i = 0; i <= 5; i++) {
			items.add(new ItemStack(Material.BOW));
		}
		for (int i = 0; i <= 32; i++) {
			items.add(new ItemStack(Material.EXP_BOTTLE));
		}
		for (int i = 0; i <= 3; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16385));
		}
		for (int i = 0; i <= 3; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16386));
		}
		for (int i = 0; i <= 3; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16387));
		}
		for (int i = 0; i <= 3; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16388));
		}
		for (int i = 0; i <= 3; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16389));
		}
		for (int i = 0; i <= 3; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16394));
		}
		for (int i = 0; i <= 3; i++) {
			items.add(new ItemStack(Material.POTION, 1, (short) 16396));
		}
		feast.feastItems = items;
	}
}
