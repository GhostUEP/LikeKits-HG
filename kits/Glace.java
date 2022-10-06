package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
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

public class Glace extends KitInterface {

	public Glace(Main main) {
		super(main);
		addAntikit();
	}

	HashMap<UUID, Long> glace = new HashMap<UUID, Long>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!hasAbility(e.getPlayer())) {
			return;
		}
		Player p = e.getPlayer();
		if (e.getAction().name().contains("RIGHT") && isKitItem(e.getItem(), Material.SNOW_BLOCK, "§r§bGlace")) {
			e.setCancelled(true);
			if (Main.plugin.stage != Estagio.GAMETIME) {
				p.sendMessage("§cVocê não pode usar isto agora!");
				return;
			}
			if (CooldownManager.isInCooldown(p.getUniqueId(), "glace")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "glace");
				p.sendMessage("§cGlace em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			CooldownManager cd = new CooldownManager(p.getUniqueId(), "glace", 30);
			cd.start();
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 2), true);
			glace.put(p.getUniqueId(), System.currentTimeMillis() + 4000L);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if ((!(e.getDamager() instanceof Player)) || !hasAbility((Player) e.getDamager())
				|| (!(e.getEntity() instanceof LivingEntity))) {
			return;
		}
		Player p = (Player) e.getDamager();
		if (!glace.containsKey(p.getUniqueId()) || glace.get(p.getUniqueId()) < System.currentTimeMillis()) {
			return;
		}
		glace.remove(p.getUniqueId());
		if (e.getEntity() instanceof Player) {
			if (hasAntikit((Player) e.getEntity())) {
				return;
			}
		}
		LivingEntity l = (LivingEntity) e.getEntity();
		l.setVelocity(p.getEyeLocation().getDirection().multiply(1.75F).setY(1.0F));
		l.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 255), true);
		l.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 250), true);
		Main.plugin.darDano(l, p, 6.0D, true);
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.SNOW_BLOCK, "§r§bGlace"));
		return new Kit("glace",
				Arrays.asList(new String[] {
						"Ao clicar em seu item, voce ganha efeitos para correr e bater em um jogador ao bater em um jogador, ele sera jogado para frente e recebera lentidao + Dano" }),
				kitItems, null, new ItemStack(Material.ICE));
	}

}
