package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Demoman extends KitInterface {

	public Demoman(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Block b = e.getBlock();
		Player p = e.getPlayer();
		if (!hasAbility(p)) {
			return;
		}
		if (b.getType() == Material.GRAVEL) {
			if (b.hasMetadata("Demoman")) {
				b.removeMetadata("Demoman", getMain());
				b.setMetadata("Demoman", new FixedMetadataValue(getMain(), p.getName()));
			} else {
				b.setMetadata("Demoman", new FixedMetadataValue(getMain(), p.getName()));
			}
		}
		if (b.getType() == Material.STONE_PLATE) {
			if (b.hasMetadata("Demoman")) {
				b.removeMetadata("Demoman", getMain());
				b.setMetadata("Demoman", new FixedMetadataValue(getMain(), p.getName()));
			} else {
				b.setMetadata("Demoman", new FixedMetadataValue(getMain(), p.getName()));
			}
		}
	}

	@EventHandler
	public void interact(PlayerInteractEvent e) {
		Block b = e.getClickedBlock();
		if (e.getAction() != Action.PHYSICAL)
			return;
		if (b == null)
			return;
		if (!b.hasMetadata("Demoman"))
			return;
		if (b.getType() != Material.STONE_PLATE)
			return;
		if (b.getRelative(BlockFace.DOWN).getType() != Material.GRAVEL)
			return;
		if (hasAntikit(e.getPlayer()))
			return;
		b.removeMetadata("Demoman", getMain());
		b.setType(Material.AIR);
		b.getWorld().createExplosion(b.getLocation().clone().add(0.5D, 0.5D, 0.5D), 4.0F);
	}

	@Override
	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<>();
		kititems.add(new ItemStack(Material.STONE_PLATE, 6));
		kititems.add(new ItemStack(Material.GRAVEL, 6));
		return new Kit("demoman", Arrays.asList(new String[] { "Plante bombas para explodir seus inimigos" }), null,
				kititems, new ItemStack(Material.STONE_PLATE));
	}

}
