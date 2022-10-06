package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class C4 extends KitInterface {

	public C4(Main main) {
		super(main);

	}

	private HashMap<UUID, Item> bomba = new HashMap<UUID, Item>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction().name().contains("RIGHT") && hasAbility(e.getPlayer())) {
			if (e.getPlayer().getItemInHand().getType() == Material.SLIME_BALL
					&& e.getPlayer().getItemInHand().getItemMeta() != null
					&& e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("§rC4")) {
				e.setCancelled(true);
				if (Main.plugin.stage != Estagio.GAMETIME) {
					e.getPlayer().sendMessage("§cVocê não pode usar isto agora!");
					return;
				}
				if (CooldownManager.isInCooldown(e.getPlayer().getUniqueId(), "c4")) {
					int timeleft = CooldownManager.getTimeLeft(e.getPlayer().getUniqueId(), "c4");
					e.getPlayer().sendMessage("§cC4 em cooldown, faltando: " + timeleft + " segundos");
					return;
				}
				Item i = e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getEyeLocation(),
						createItem(Material.TNT, 1, e.getPlayer().getName()));
				i.setVelocity(e.getPlayer().getEyeLocation().getDirection().multiply(1.2D));
				i.setPickupDelay(Integer.MAX_VALUE);
				this.bomba.put(e.getPlayer().getUniqueId(), i);
				e.getPlayer().setItemInHand(createItem(Material.STONE_BUTTON, 1, "§rDetonador"));
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.SLIME_ATTACK, 1.0F, 1.0F);
			} else if (e.getPlayer().getItemInHand().getType() == Material.STONE_BUTTON
					&& e.getPlayer().getItemInHand().getItemMeta() != null
					&& e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("§rDetonador")) {
				e.setCancelled(true);
				if (!this.bomba.containsKey(e.getPlayer().getUniqueId())) {
					e.getPlayer().sendMessage("§cVocê não lançou a C4");
					return;
				}
				if (this.bomba.get(e.getPlayer().getUniqueId()).isDead()) {
					e.getPlayer().sendMessage("§cSua C4 foi destruida");
					e.getPlayer().setItemInHand(createItem(Material.SLIME_BALL, 1, "§rC4"));
					this.bomba.remove(e.getPlayer().getUniqueId());
					return;
				}
				Item i = this.bomba.get(e.getPlayer().getUniqueId());
				i.getWorld().createExplosion(i.getLocation(), 3.0F);
				i.remove();
				this.bomba.remove(e.getPlayer().getUniqueId());
				e.getPlayer().setItemInHand(createItem(Material.SLIME_BALL, 1, "§rC4"));
				CooldownManager cd = new CooldownManager(e.getPlayer().getUniqueId(), "c4", 60);
				cd.start();
			}
		} else if (e.getAction().name().contains("LEFT")
				&& (e.getPlayer().getItemInHand().getType() == Material.STONE_BUTTON
						&& e.getPlayer().getItemInHand().getItemMeta() != null
						&& e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("§rDetonador"))) {
			e.setCancelled(true);
			if (this.bomba.containsKey(e.getPlayer().getUniqueId())) {
				if (!this.bomba.get(e.getPlayer().getUniqueId()).isDead()) {
					this.bomba.get(e.getPlayer().getUniqueId()).remove();
				}
				this.bomba.remove(e.getPlayer().getUniqueId());
			}
			e.getPlayer().setItemInHand(createItem(Material.SLIME_BALL, 1, "§rC4"));
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (hasAbility(p)) {
			if (e.getItemDrop().getItemStack().getType() != Material.STONE_BUTTON)
				return;
			if (e.getItemDrop().getItemStack().getItemMeta() == null)
				return;
			if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("Detonador"))
				e.setCancelled(true);
			p.updateInventory();
			return;
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		ItemStack bomba = new ItemStack(Material.SLIME_BALL, 1);
		ItemMeta bombaim = bomba.getItemMeta();
		bombaim.setDisplayName("§rC4");
		bomba.setItemMeta(bombaim);
		kitItems.add(bomba);
		return new Kit("c4", Arrays.asList(new String[] { "Esse sua bomba como uma C4 para explodir seus inimigos" }),
				kitItems, null, new ItemStack(Material.TNT));
	}
}
