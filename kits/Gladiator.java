package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.enums.GladiatorType;
import me.ghost.hg.manager.KitInterface;
import me.ghost.hg.utils.GladiatorFight;

public class Gladiator extends KitInterface {

	public static List<UUID> playersIn1v1;
	public static List<Block> gladiatorBlocks;
	public HashMap<UUID, Integer> irons = new HashMap<>();

	public Gladiator(Main main) {
		super(main);
		addAntikit();
		playersIn1v1 = new ArrayList<UUID>();
		gladiatorBlocks = new ArrayList<>();
	}

	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		ItemStack i = p.getItemInHand();
		if (!(e instanceof Player))
			return;
		if (!hasAbility(p))
			return;
		if (i.getType() == null)
			return;
		if (i.getType() != Material.IRON_FENCE)
			return;
		if (i.getItemMeta() == null)
			return;
		if (Main.plugin.stage != Estagio.GAMETIME) {
			p.sendMessage(ChatColor.RED + "Você não pode usar isso na invencibilidade");
			return;
		}
		if (hasAntikit((Player) e)) {
			p.sendMessage(Main.plugin.antikit_message);
			return;
		}
		if (Main.plugin.adm.isSpectating((Player) e))
			return;
		if (Main.plugin.isNotPlaying((Player) e))
			return;
		if (((Player) e).isDead())
			return;
		if (playersIn1v1.contains(p.getUniqueId()))
			return;
		if (playersIn1v1.contains(((Player) e).getUniqueId()))
			return;
		new GladiatorFight(p, (Player) e, GladiatorType.GLADIATOR);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack i = p.getItemInHand();
		if (event.getAction() != Action.PHYSICAL && hasAbility(p) && i.getType() != null
				&& i.getType() == Material.IRON_FENCE) {
			p.updateInventory();
			event.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlock(BlockDamageEvent event) {
		if (gladiatorBlocks.contains(event.getBlock()) && event.getBlock().getType() == Material.GLASS) {
			final Block b = event.getBlock();
			final Player p = event.getPlayer();
			p.sendBlockChange(b.getLocation(), Material.BEDROCK, (byte) 0);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (playersIn1v1.contains(p.getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		Iterator<Block> blockIt = event.blockList().iterator();
		while (blockIt.hasNext()) {
			Block b = blockIt.next();
			if (gladiatorBlocks.contains(b)) {
				blockIt.remove();
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (gladiatorBlocks.contains(event.getBlock())) {
			event.setCancelled(true);
		}
	}

	public static boolean inGladiator(Player p) {
		return playersIn1v1.contains(p.getUniqueId());
	}

	@Override
	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<>();
		kititems.add(createItem(Material.IRON_FENCE, ChatColor.RED + "" + ChatColor.BOLD + "Tire 1v1"));
		return new Kit("gladiator",
				Arrays.asList(new String[] { "Puxe seus inimigos para uma arena, e tire 1v1 com eles" }), kititems,
				null, new ItemStack(Material.IRON_FENCE));
	}
}