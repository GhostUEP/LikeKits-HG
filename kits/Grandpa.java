package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Grandpa extends KitInterface {

	public Grandpa(Main main) {
		super(main);
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItemStack("§rGrandpa", Material.STICK, 1, Enchantment.KNOCKBACK, 2));
		return new Kit("grandpa", Arrays.asList(new String[] { "Você nasce com um graveto com repulsao II" }), kitItems,
				null, new ItemStack(Material.STICK));
	}

}
