package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.KitInterface;

public class Cannibal extends KitInterface {

	public Cannibal(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onSnail(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		Player p = (Player) event.getEntity();
		Player d = (Player) event.getDamager();
		if (!hasAbility(d))
			return;
		if (hasAntikit(p))
			return;
		if (getMain().stage != Estagio.GAMETIME)
			return;
		if (getMain().isNotPlaying(d))
			return;
		Random r = new Random();
		if (p instanceof Player) {
			if (r.nextInt(3) == 0) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 5 * 20, 0));
				d.setFoodLevel(d.getFoodLevel() + 1);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(new ItemStack(Material.RAW_FISH, 1));
		kitItems.add(new ItemStack(Material.getMaterial(383), 1, (short) 98));
		return new Kit("cannibal",
				Arrays.asList(new String[] { "Ao bater em algum jogador ira deixalo com fome e a sua recuperara" }),
				kitItems, null, new ItemStack(Material.RAW_FISH));
	}

}
