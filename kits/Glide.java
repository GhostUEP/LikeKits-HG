package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftFirework;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;
import me.ghost.hg.utils.Particulas;

public class Glide extends KitInterface {

	public Glide(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (hasAbility(e.getPlayer()) && Main.plugin.stage != Estagio.PREGAME && !e.getPlayer().isOnGround()
				&& isKitItem(e.getPlayer().getItemInHand(), "§r§7Glider Feather")) {
			e.getPlayer().setFallDistance(e.getPlayer().getFallDistance() / 2.0F);
			Vector v = e.getPlayer().getVelocity();
			v.setY(v.getY() / 1.5D);

			e.getPlayer().setVelocity(v);

			List<Entity> entities = e.getPlayer().getNearbyEntities(20, 20, 20);

			entities.add(e.getPlayer());

			for (Entity en : entities) {
				if (!(en instanceof Player)) {
					continue;
				}
				Player pe = (Player) en;
				try {
					Particulas.LARGE_SMOKE.part(pe, e.getPlayer().getLocation().add(0, 0.5, 0)
							.add(e.getPlayer().getEyeLocation().getDirection().multiply(-0.5D)), 0, 0, 0, 0, 1);
				} catch (Exception e1) {
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (hasAbility(event.getPlayer()) && isKitItem(event.getItem(), "§r§7Glider Feather")
				&& event.getAction().name().contains("RIGHT")) {
			event.setCancelled(true);
			Player p = event.getPlayer();
			if (CooldownManager.isInCooldown(p.getUniqueId(), "glide")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "glide");
				p.sendMessage("§cGlide em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			Vector v = p.getEyeLocation().getDirection().multiply(1.2D).setY(0.4D);
			p.setVelocity(v);

			Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
			FireworkMeta meta = firework.getFireworkMeta();
			meta.addEffect(FireworkEffect.builder().withColor(Color.ORANGE).with(Type.BALL_LARGE).build());
			firework.setFireworkMeta(meta);
			((CraftFirework) firework).getHandle().expectedLifespan = 1;

			CooldownManager cd = new CooldownManager(p.getUniqueId(), "glide", 20);
			cd.start();
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.FEATHER, "§r§7Glider Feather"));
		return new Kit("glide",
				Arrays.asList(new String[] {
						"Ao voce segurar a pena do seu kit se voce estiver caindo, voce ira planar e soltar fogo pelas suas costas" }),
				kitItems, null, new ItemStack(Material.FEATHER));
	}

}
