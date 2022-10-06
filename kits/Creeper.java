package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Creeper extends KitInterface {

	public Creeper(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (hasAbility(e.getEntity())) {
			e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 3.0F);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("creeper",
				Arrays.asList(new String[] {
						"Inicie a partida com uma habilidade que ao morrer cria-se uma explosão onde voce morreu" }),
				kitItems, null, new ItemStack(Material.MONSTER_EGG, 1, (short) 50));
	}

}
