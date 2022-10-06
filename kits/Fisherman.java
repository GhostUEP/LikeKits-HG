package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Fisherman extends KitInterface {

	public Fisherman(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onFish(PlayerFishEvent e) {
		if (e.getCaught() instanceof LivingEntity && hasAbility(e.getPlayer())) {
			if (e.getCaught() instanceof Player) {
				Player pego = (Player) e.getCaught();
				if (hasAntikit(pego)) {
					e.getPlayer().sendMessage(Main.plugin.antikit_message);
					return;
				}
			}
			e.getCaught().teleport(e.getPlayer().getLocation());
			e.getPlayer().getItemInHand().setDurability((short) 0);
			((LivingEntity) e.getCaught()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 1));
			// LAUNCHER
			// if (Launcher.noFall.contains(e.getPlayer()) && e.getCaught()
			// instanceof Player) {
			// Launcher.noFall.add((Player) e.getCaught());
			// }
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.FISHING_ROD, "§rFisherman Rod"));
		return new Kit("fisherman",
				Arrays.asList(new String[] {
						"Inicie a partida com uma vara de pesca que ao fiscar um player Teleport ele até você" }),
				kitItems, null, new ItemStack(Material.FISHING_ROD));
	}

}
