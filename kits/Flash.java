package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Flash extends KitInterface {

	public Flash(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (hasAbility(e.getPlayer()) && isKitItem(e.getItem(), Material.REDSTONE_TORCH_ON, "§r§cFlash")) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
				return;
			}
			if (e.getAction() != Action.RIGHT_CLICK_AIR) {
				return;
			}
			e.setCancelled(true);
			Player p = e.getPlayer();
			if (CooldownManager.isInCooldown(p.getUniqueId(), "flash")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "flash");
				p.sendMessage("§cFlash em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			Block b = p.getTargetBlock(null, 100);
			if (b.getType() == Material.AIR) {
				p.sendMessage("§cBloco invalido! O bloco não pode ser ar");
				return;
			}
			BlockIterator list = new BlockIterator(p.getEyeLocation(), 0, 100);
			while (list.hasNext()) {
				p.getWorld().playEffect(list.next().getLocation(), Effect.ENDER_SIGNAL, 100);
			}
			p.teleport(b.getLocation().clone().add(0, 1.5, 0));
			p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			CooldownManager cd = new CooldownManager(p.getUniqueId(), "flash", 60);
			cd.start();
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.REDSTONE_TORCH_ON, 1, "§r§cFlash"));
		return new Kit("flash",
				Arrays.asList(new String[] {
						"Ao clicar em sua torcha de redstone olhando para um bloco, Você sera teleportado para o bloco deixando um rastro de efeitos para tras" }),
				kitItems, null, new ItemStack(Material.REDSTONE_TORCH_ON));
	}

}
