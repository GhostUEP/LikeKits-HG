package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class Forcefield extends KitInterface {

	public Forcefield(Main main) {
		super(main);
	}

	public HashMap<Player, Long> ff = new HashMap<Player, Long>();

	@EventHandler
	public void ativarFF(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (hasAbility(p) && isKitItem(e.getItem(), "§r§4Forcefield") && e.getAction().name().contains("RIGHT")) {
			e.setCancelled(true);
			p.updateInventory();
			if (Main.plugin.stage != Estagio.GAMETIME) {
				p.sendMessage("§cVocê não pode usar isto agora!");
				return;
			}
			if (CooldownManager.isInCooldown(p.getUniqueId(), "forcefield")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "forcefield");
				p.sendMessage("§cForcefield em cooldown, faltando: " + timeleft + " segundos");
				return;
			}

			CooldownManager cd = new CooldownManager(p.getUniqueId(), "forcefield", 90);
			cd.start();
			p.sendMessage("§cVocê ativou seu Forcefield [Duração : 5 segundos]");
			ff.put(p, System.currentTimeMillis() + 5000L);
		}
	}

	double raio = 4.0;

	private HashMap<UUID, Long> cooldown = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void moverFF(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (hasAbility(p) && ff.containsKey(p) && ff.get(p) > System.currentTimeMillis()) {
			for (Entity nearby : p.getNearbyEntities(raio, raio, raio)) {
				if (nearby instanceof Player) {
					Player perto = (Player) nearby;
					if (Main.plugin.adm.isSpectating(perto)) {
						continue;
					}
					if (hasAntikit(perto)) {
						continue;
					}
					if (Endermage.invencible.containsKey(perto.getUniqueId())
							&& Endermage.invencible.get(perto.getUniqueId()) > System.currentTimeMillis()) {
						continue;
					}

					if (cooldown.containsKey(perto.getUniqueId())
							&& cooldown.get(perto.getUniqueId()) > System.currentTimeMillis()) {
						return;
					}

					cooldown.put(perto.getUniqueId(), System.currentTimeMillis() + 1000L);

					if (perto.isOnGround()) {
						perto.setVelocity(p.getEyeLocation().getDirection());
					}
					EntityPlayer p2 = ((CraftPlayer) perto).getHandle();
					if (p2.getHealth() >= 3) {
						perto.damage(2.0D);
					} else {
						p2.setHealth(1);
						perto.setNoDamageTicks(0);
						Main.plugin.darDano(perto, p, 10.0D, true);
					}
				}
			}
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.IRON_FENCE, 1, "§r§4Forcefield"));
		return new Kit("forcefield",
				Arrays.asList(new String[] {
						"Ao clicar em um player com o item do Forcefield, criará um campo de força (Forcefield) que dará dano" }),
				kitItems, null, new ItemStack(Material.IRON_FENCE));
	}

}
