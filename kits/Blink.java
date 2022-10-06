package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Blink extends KitInterface {

	public Blink(Main main) {
		super(main);
	}

	private HashMap<UUID, Integer> uses = new HashMap<UUID, Integer>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction().name().contains("RIGHT")) && (hasAbility(p))) {
			if (Main.plugin.stage == Estagio.PREGAME)
				return;
			if (e.getItem().getType() == Material.NETHER_STAR && e.getItem().hasItemMeta()
					&& e.getItem().getItemMeta().getDisplayName().contains("Blink")) {
				e.setCancelled(true);

				if (CooldownManager.isInCooldown(p.getUniqueId(), "blink")) {
					int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "blink");
					p.sendMessage("§cBlink em cooldown, faltando: " + timeleft + " segundos");
					return;
				}

				Block b = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(5.0D)).getBlock();

				while (b.getY() > 128) {
					p.sendMessage("§cVocê não pode usar o blink nessa altura");
					return;
				}

				int usou = 0;

				if (this.uses.containsKey(p.getUniqueId())) {
					usou = this.uses.get(p.getUniqueId());
				}

				usou++;

				if (usou == 3) {
					CooldownManager cd = new CooldownManager(p.getUniqueId(), "blink", 30);
					cd.start();
					this.uses.remove(p.getUniqueId());
				} else {
					this.uses.put(p.getUniqueId(), usou);
				}

				if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
					b.getRelative(BlockFace.DOWN).setType(Material.LEAVES);
				}

				p.teleport(new Location(p.getWorld(), b.getX(), b.getY(), b.getZ(), p.getLocation().getYaw(),
						p.getLocation().getPitch()));
				p.setFallDistance(0.0F);
				p.playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 1.0F, 50.0F);
			}
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.NETHER_STAR, 1, "§rBlink §lStar"));
		return new Kit("blink",
				Arrays.asList(new String[] {
						"Inicie a partida com a habilidade que ao clicar com o botao direito em uma estrela do nether, ela te leva 5 blocos para frente para onde voce estava olhando" }),
				kitItems, null, new ItemStack(Material.NETHER_STAR));
	}

}
