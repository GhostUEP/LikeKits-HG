package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.KitInterface;

public class Anchor extends KitInterface {

	public Anchor(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
		if (Main.plugin.stage != Estagio.GAMETIME) {
			return;
		}
		if ((e.getEntity() instanceof Player) && (hasAbility((Player) e.getEntity()))) {
			// ANTI KIT

			if (hasAntikit((Player) e.getEntity())) {
				return;
			}
			if (e.getDamager() instanceof Player && Main.plugin.adm.isSpectating((Player) e.getDamager())) {
				return;
			}
			if (e.getDamager() instanceof Player && Main.plugin.isNotPlaying((Player) e.getDamager())) {
				return;
			}
			e.getEntity().setVelocity(new Vector(0, e.getEntity().getVelocity().getY(), 0));
			new BukkitRunnable() {
				public void run() {
					e.getEntity().setVelocity(new Vector(0, e.getEntity().getVelocity().getY(), 0));
				}
			}.runTaskLater(Main.plugin, 1L);
		} else if ((e.getDamager() instanceof Player) && (hasAbility((Player) e.getDamager()))
				&& (e.getEntity() instanceof LivingEntity)) {
			if (e.getEntity() instanceof Player) {
				Player jogador = (Player) e.getEntity();
				if (hasAntikit(jogador)) {
					return;
				}
			}
			// ANTI KIT

			// if
			// (getManager().getGamer(e.getEntity().getUniqueId()).getAntiKit()
			// .equalsIgnoreCase(Anchor.class.getSimpleName())) {
			// return;
			// }
			if (e.getDamager() instanceof Player && Main.plugin.adm.isSpectating((Player) e.getDamager())) {
				return;
			}
			if (e.getDamager() instanceof Player && Main.plugin.isNotPlaying((Player) e.getDamager())) {
				return;
			}
			e.getEntity().setVelocity(new Vector(0, e.getEntity().getVelocity().getY(), 0));
			new BukkitRunnable() {
				public void run() {
					e.getEntity().setVelocity(new Vector(0, e.getEntity().getVelocity().getY(), 0));
				}
			}.runTaskLater(Main.plugin, 1L);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("anchor", Arrays.asList(new String[] { "Não recebe e nem aplica o knockback" }), kitItems, null,
				new ItemStack(Material.ANVIL));
	}

}
