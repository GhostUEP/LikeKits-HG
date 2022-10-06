package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Berserker extends KitInterface {

	public Berserker(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		Player p = (Player) event.getDamager();
		Player tomou = (Player) event.getEntity();
		if (hasAbility(p)) {
			if (Main.plugin.stage != Estagio.GAMETIME) {
				return;
			}
			if (p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
				return;
			}
			Random r = new Random();
			if (r.nextInt(100) <= 10) {
				if (CooldownManager.isInCooldown(p.getUniqueId(), "berserker")) {
					return;
				}
				if (hasAntikit(tomou)) {
					return;
				}
				CooldownManager cd = new CooldownManager(p.getUniqueId(), "berserker", 7);
				cd.start();
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, 0));
				p.playSound(event.getDamager().getLocation(), Sound.ZOMBIE_PIG_ANGRY, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("berserker",
				Arrays.asList(new String[] {
						"Inicie a partida com a habilidade que ao bater em um jogador tem 10% de chance de voce receber forca I por 3 segundos" }),
				kitItems, null, new ItemStack(Material.POTION, 1, (short) 16425));
	}

}
