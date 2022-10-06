package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class Icelord extends KitInterface {

	public Icelord(Main main) {
		super(main);
		addAntikit();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void jogar(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof Snowball && event.getEntity().getShooter() instanceof Player
				&& hasAbility((Player) event.getEntity().getShooter())) {
			Player p = (Player) event.getEntity().getShooter();
			if (Main.plugin.stage != Estagio.GAMETIME) {
				event.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.SNOW_BALL));
				p.sendMessage("§cVocê não pode usar isto agora!");
				return;
			}
			if (CooldownManager.isInCooldown(p.getUniqueId(), "icelord")) {
				event.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.SNOW_BALL));
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "icelord");
				p.sendMessage("§cIcelord em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			event.getEntity().setMetadata("IceLord", new FixedMetadataValue(Main.plugin, p));
			event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(2.5));
			BlockIterator blockAdd = new BlockIterator(p.getEyeLocation(), 0, 40);
			while (blockAdd.hasNext()) {
				Location blocksAdd = blockAdd.next().getLocation();
				blocksAdd.getWorld().playEffect(blocksAdd, Effect.STEP_SOUND, Material.ICE.getId(), 10);
			}
		}
	}

	@EventHandler
	public void acertar(EntityDamageByEntityEvent event) {
		if (event.isCancelled() || Main.plugin.stage != Estagio.GAMETIME) {
			return;
		}
		if (event.getDamager() instanceof Snowball && event.getEntity() instanceof Player
				&& event.getDamager().hasMetadata("IceLord")) {
			Player atirador = (Player) event.getDamager().getMetadata("IceLord").get(0).value();
			if (CooldownManager.isInCooldown(atirador.getUniqueId(), "icelord")) {
				return;

			}
			Player levou = (Player) event.getEntity();
			if (hasAntikit(levou)) {
				return;
			}
			CooldownManager cd = new CooldownManager(atirador.getUniqueId(), "icelord", 15);
			cd.start();
			EntityPlayer p = ((CraftPlayer) levou).getHandle();
			if (p.getHealth() > 6) {
				levou.setNoDamageTicks(0);
				levou.damage(6.0D);
			} else {
				p.setHealth(1);
				levou.setNoDamageTicks(0);
				Main.plugin.darDano(levou, atirador, 6.0D, true);
			}
			levou.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 255), true);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(new ItemStack(Material.SNOW_BALL, 25));
		return new Kit("icelord",
				Arrays.asList(new String[] {
						"Ao lançar uma bola de neve e acertar um jogador este jogador perde o sprint e recebe 3,5 Corações de Dano" }),
				null, kitItems, new ItemStack(Material.PACKED_ICE));
	}

}
