package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Fireman extends KitInterface {

	public Fireman(Main main) {
		super(main);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && hasAbility((Player) e.getEntity())
				&& (e.getCause() == DamageCause.LAVA || e.getCause().name().contains("FIRE"))) {
			int x = Math.abs(e.getEntity().getLocation().getBlockX());
			int z = Math.abs(e.getEntity().getLocation().getBlockZ());
			if (e.getCause() == DamageCause.LIGHTNING && (x > 490 || z > 490)) {
				return;
			}
			e.setCancelled(true);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(new ItemStack(Material.WATER_BUCKET));
		return new Kit("fireman",
				Arrays.asList(new String[] {
						"Você inicia a partida com um balde d'gua e você não leva dano de fogo, lava e nem raios" }),
				null, kitItems, new ItemStack(Material.WATER_BUCKET));
	}

}
