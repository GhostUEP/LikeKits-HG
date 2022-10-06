package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Cultivator extends KitInterface {

	public Cultivator(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void place(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.SAPLING && hasAbility(e.getPlayer())) {
			e.getBlock().setType(Material.AIR);
			e.getBlock().getWorld().generateTree(e.getBlock().getLocation(), TreeType.TREE);
		} else if (e.getBlock().getType() == Material.CROPS && hasAbility(e.getPlayer())) {
			e.getBlock().setData((byte) 7);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("cultivator",
				Arrays.asList(new String[] {
						"Inicie a partida com uma habilidade que ao plantar semente ou arvores faça crescer rapidamente" }),
				kitItems, null, new ItemStack(Material.SAPLING));
	}

}
