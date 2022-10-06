package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Hulk extends KitInterface {

	public Hulk(Main main) {
		super(main);
		addAntikit();
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		if (hasAbility(p) && (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR)
				&& event.getRightClicked() instanceof Player && p.getPassenger() == null && !p.isInsideVehicle()
				&& event.getRightClicked().getPassenger() == null && !event.getRightClicked().isInsideVehicle()) {
			if (Main.plugin.stage != Estagio.GAMETIME) {
				p.sendMessage("§cVocê não pode usar isto agora!");
				return;
			}
			Player getclicked = (Player) event.getRightClicked();
			if (Main.plugin.adm.isSpectating(getclicked)) {
				return;
			}
			if (Main.plugin.adm.isSpectating(p)) {
				return;
			}
			if (CooldownManager.isInCooldown(p.getUniqueId(), "hulk")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "hulk");
				p.sendMessage("§cHulk em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			if (hasAntikit(getclicked)) {
				p.sendMessage(Main.plugin.antikit_message);
				return;
			}
			p.setPassenger(event.getRightClicked());
			CooldownManager cd = new CooldownManager(p.getUniqueId(), "hulk", 5);
			cd.start();
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (hasAbility(p) && p.getPassenger() != null && event.getAction().name().contains("LEFT")
				&& (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR)) {
			Entity e = p.getPassenger();
			e.eject();
			if (e.isInsideVehicle()) {
				e.getVehicle().eject();
			}
			e.setVelocity(p.getEyeLocation().getDirection().multiply(1.5F).setY(0.35F));
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("hulk",
				Arrays.asList(new String[] {
						"Ao clicar em um jogador, ele ficara em cima de voce, voce pode jogar-lo batendo na direçao desejada, ou bater-lo com uma frequencia Maior" }),
				kitItems, null, new ItemStack(Material.DISPENSER));
	}

}
