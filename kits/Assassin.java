package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;
import me.ghost.hg.utils.Particulas;

public class Assassin extends KitInterface {

	public Assassin(Main main) {
		super(main);
		addAntikit();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (hasAbility(p)) {
			if (isKitItem(event.getItem(), "§6§lAssassin") && event.getAction().name().contains("RIGHT")) {
				event.setCancelled(true);
				if (Main.plugin.stage != Estagio.GAMETIME) {
					p.sendMessage("§cVocê não pode usar isto agora");
					return;
				}
				if (CooldownManager.isInCooldown(p.getUniqueId(), "assassin")) {
					int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "assassin");
					p.sendMessage("§cAssassin em cooldown, faltando: " + timeleft + " segundos");
					return;
				}
				CooldownManager cd = new CooldownManager(p.getUniqueId(), "assassin", 20);
				cd.start();
				spawnNinjaEffects(p.getLocation());
				Location loc = p.getTargetBlock(null, 5).getLocation();
				loc.setYaw(p.getLocation().getYaw());
				loc.setPitch(p.getLocation().getPitch());
				p.teleport(loc);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 0), true);
			}
		}
	}

	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		Player damager = (Player) event.getDamager();
		Player p = (Player) event.getEntity();
		if (hasAbility(damager)) {
			if (hasAntikit(p)) {
				damager.sendMessage(Main.plugin.antikit_message);
				return;
			}
			if (damager.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
				event.setDamage(event.getDamage() + 2.0D);
				damager.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}
	}

	private void spawnNinjaEffects(Location loc) {
		Particulas.LARGE_SMOKE.part(loc, 0, 0, 0, 0.05F, 300);
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.SKULL_ITEM, "§6§lAssassin"));
		return new Kit("assassin",
				Arrays.asList(new String[] {
						"Ao clicar no item do assassin você ira receber um teleporte de curta distancia para onde voce esta olhando, e receberá uma invisibilidade por até 5 segundos, ao bater em um jogador você quebra sua invisibilidade e causa dano extra acompanhada de lentidao por 3 segundos" }),
				kitItems, null, new ItemStack(Material.GOLD_SWORD));
	}

}
