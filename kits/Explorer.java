package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.events.MinifeastSpawnEvent;
import me.ghost.hg.manager.KitInterface;

public class Explorer extends KitInterface {

	public Explorer(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMini(MinifeastSpawnEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Main.plugin.isNotPlaying(p))
				continue;
			if (hasAbility(p)) {
				p.sendMessage("§cAs coordenadas do minifeast são: (X: " + e.getX() + ", Y: " + e.getY() + ", Z: "
						+ e.getZ() + ")");
			}
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("explorer",
				Arrays.asList(new String[] { "Receba as coordenanas exatas de um minifeast ao nace-lo" }), kitItems,
				null, new ItemStack(Material.MAP));
	}

}
