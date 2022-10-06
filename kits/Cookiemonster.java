package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.KitInterface;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class Cookiemonster extends KitInterface {

	public Cookiemonster(Main main) {
		super(main);
	}

	private HashMap<UUID, Long> delay = new HashMap<UUID, Long>();

	@EventHandler
	public void quebrar(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if ((hasAbility(p)) && (e.getBlock().getType() == Material.LONG_GRASS) && (new Random().nextInt(100) <= 33)) {
			e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.COOKIE, 1));
		}
	}

	@EventHandler
	public void click(PlayerInteractEvent e) {
		if ((e.getAction().name().contains("RIGHT")) && (e.getItem() != null)
				&& (e.getItem().getType() == Material.COOKIE) && (hasAbility(e.getPlayer()))) {
			e.setCancelled(true);
			Player p = e.getPlayer();
			UUID u = p.getUniqueId();
			if (delay.containsKey(u) && delay.get(u) > System.currentTimeMillis()) {
				return;
			}
			EntityPlayer p2 = ((CraftPlayer) p).getHandle();
			if (p2.getHealth() < p2.getMaxHealth()) {
				float health = p2.getHealth() + 2;
				if (health > p2.getMaxHealth()) {
					health = p2.getMaxHealth();
				}
				p2.setHealth(health);
			} else if (p.getFoodLevel() < 20) {
				p.setFoodLevel(p.getFoodLevel() + 2);
			} else {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1), true);
			}
			if (e.getItem().getAmount() > 1) {
				e.getItem().setAmount(e.getItem().getAmount() - 1);
			} else {
				p.setItemInHand(null);
			}
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("cookiemonster",
				Arrays.asList(new String[] {
						"Inicie a partida com uma habilidade que ao quebrar uma grama tenha 80% de chance de dropar um cookie, ao comer este cookie fique com speed II por 5 segundos" }),
				kitItems, null, new ItemStack(Material.COOKIE));
	}

}
