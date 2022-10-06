package me.ghost.hg.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Nome {
	public static String getItemName(ItemStack item) {
		String name = "seu(a) " + item.getType().name().replace("_", " ");
		Material i = item.getType();
		if (i == Material.AIR) {
			name = "suas Mãos";
		}
		if (i == Material.DIAMOND_SWORD) {
			name = "sua Espada de Diamante";
		}
		if (i == Material.WOOD_SWORD) {
			name = "sua Espada de Madeira";
		}
		if (i == Material.STONE_SWORD) {
			name = "sua Espada de Pedra";
		}
		if (i == Material.IRON_SWORD) {
			name = "sua Espada de Ferro";
		}
		if (i == Material.GOLD_SWORD) {
			name = "sua Espada de Ouro";
		}
		if (i == Material.DIAMOND_AXE) {
			name = "seu Machado de Diamante";
		}
		if (i == Material.WOOD_AXE) {
			name = "seu Machado de Madeira";
		}
		if (i == Material.STONE_AXE) {
			name = "seu Machado de Pedra";
		}
		if (i == Material.IRON_AXE) {
			name = "seu Machado de Ferro";
		}
		if (i == Material.GOLD_AXE) {
			name = "seu Machado de Ouro";
		}
		if (i == Material.DIAMOND_PICKAXE) {
			name = "sua Picareta de Diamante";
		}
		if (i == Material.WOOD_PICKAXE) {
			name = "sua Picareta de Madeira";
		}
		if (i == Material.STONE_PICKAXE) {
			name = "sua Picareta de Pedra";
		}
		if (i == Material.IRON_PICKAXE) {
			name = "sua Picareta de Ferro";
		}
		if (i == Material.GOLD_PICKAXE) {
			name = "sua Picareta de Ouro";
		}
		if (i == Material.BOWL) {
			name = "seu Pote";
		}
		if (i == Material.MUSHROOM_SOUP) {
			name = "sua Sopa";
		}
		if (i == Material.ARROW) {
			name = "uma Flecha";
		}
		if (i == Material.RED_MUSHROOM) {
			name = "seu Cogumelo Vermelho";
		}
		if (i == Material.BROWN_MUSHROOM) {
			name = "seu Cogumelo Marrom";
		}
		if (i == Material.GRASS) {
			name = "seu Mato";
		}
		if (i == Material.DIRT) {
			name = "sua Terra";
		}
		if (i == Material.SAND) {
			name = "sua Areia";
		}
		if (i == Material.COBBLESTONE) {
			name = "sua Pedra";
		}
		if (i == Material.STICK) {
			name = "seu Graveto";
		}
		return name;
	}
}
