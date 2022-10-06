package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;

public class Boxer extends KitInterface {

	public Boxer(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onEntityDamagebyEntity(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player))
			return;
		Player damager = (Player) e.getDamager();
		if (hasAbility(damager)) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (hasAntikit(p)) {
					return;
				}
			}
			e.setDamage(e.getDamage() + 0.5D);
		}
		if (e.getEntity() instanceof Player && (hasAbility((Player) e.getEntity()))) {
			e.setDamage(e.getDamage() - 0.5D);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("boxer",
				Arrays.asList(new String[] {
						"Qualquer item que esteja em sua mao irá causar 0,25 coraçoes de dano extra, todo dano fisico recebido é reduzido em 0,25 coraçoes" }),
				kitItems, null, new ItemStack(Material.STONE_SWORD));
	}

}
