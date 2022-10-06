package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Fear extends KitInterface {

	public Fear(Main main) {
		super(main);
		addAntikit();
	}

	private HashMap<UUID, Long> susto = new HashMap<UUID, Long>();

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = (Player) event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && hasAbility(p) && p.getItemInHand() != null
				&& p.getItemInHand().getType() == Material.STONE_HOE && p.getItemInHand().hasItemMeta()
				&& p.getItemInHand().getItemMeta().getDisplayName().contains("Fear")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		if (Main.plugin.stage != Estagio.GAMETIME || (!(event.getRightClicked() instanceof Player))) {
			return;
		}
		Player target = (Player) event.getRightClicked();
		if (Main.plugin.adm.isSpectating(target)) {
			return;
		}
		if (hasAbility(p) && p.getItemInHand() != null && p.getItemInHand().getType() == Material.STONE_HOE
				&& p.getItemInHand().hasItemMeta()
				&& p.getItemInHand().getItemMeta().getDisplayName().contains("Fear")) {
			event.setCancelled(true);
			if (CooldownManager.isInCooldown(p.getUniqueId(), "fear")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "fear");
				p.sendMessage("§cFear em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			if (hasAntikit(target)) {
				p.sendMessage(Main.plugin.antikit_message);
				return;
			}

			CooldownManager cd = new CooldownManager(p.getUniqueId(), "fear", 30);
			cd.start();
			Location location = target.getLocation();
			location.setPitch(p.getLocation().getPitch());
			location.setYaw(p.getLocation().getYaw());
			p.sendMessage("§aVocê assustou " + target.getDisplayName());
			target.sendMessage("§c§mVocê se assustou...");
			target.teleport(location);
			target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0), true);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 250), true);
			target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 250), true);
			susto.put(target.getUniqueId(), System.currentTimeMillis() + 3000L);
		}
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		Player damager = (Player) event.getDamager();
		if (susto.containsKey(damager.getUniqueId()) && susto.get(damager.getUniqueId()) > System.currentTimeMillis()) {
			event.setCancelled(true);
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.STONE_HOE, 1, "§5Fear"));
		return new Kit("fear",
				Arrays.asList(new String[] {
						"Ao clicar com seu item em um inimigo ele ficará assutando correndo sem rumo na direcao que voce está olhando por 5 segundos, por 3 segundos o jogador alvo não poderá atacar ninguem" }),
				kitItems, null, new ItemStack(Material.JACK_O_LANTERN));
	}

}
