package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Chemist extends KitInterface {

	public Chemist(Main main) {
		super(main);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().name().contains("RIGHT") && isKitItem(event.getItem(), "§5§lChemist")
				&& hasAbility(event.getPlayer())) {
			Player p = event.getPlayer();
			event.setCancelled(true);
			if (CooldownManager.isInCooldown(p.getUniqueId(), "chemist")) {
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "chemist");
				p.sendMessage("§cChemist em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			if (p.getInventory().firstEmpty() == -1) {
				p.sendMessage("§cInventario cheio!");
				return;
			}
			CooldownManager cd = new CooldownManager(p.getUniqueId(), "chemist", 30);
			cd.start();
			PotionType type = getRandomPotion();
			while (type == PotionType.WATER) {
				type = getRandomPotion();
			}
			ItemStack give = new ItemStack(Material.AIR);
			if (type == PotionType.INSTANT_DAMAGE || type == PotionType.INSTANT_HEAL) {
				Potion po = new Potion(type, 2);
				po.setSplash(type.equals(PotionType.INSTANT_DAMAGE));
				give = po.toItemStack(1);
				recreatePotion(give, type, 1, 1);
			} else if (type == PotionType.POISON || type == PotionType.SLOWNESS || type == PotionType.WEAKNESS) {
				Potion po = new Potion(type, 1);
				po.setSplash(true);
				give = po.toItemStack(1);
				recreatePotion(give, type, 30, 0);
			} else {
				Potion po = new Potion(type, 1);
				po.setSplash(false);
				give = po.toItemStack(1);
				recreatePotion(give, type, 30, 0);
			}
			p.getInventory().addItem(give);
			p.updateInventory();
		}
	}

	private void recreatePotion(ItemStack i, PotionType effect, int seconds, int amplifier) {
		PotionMeta pm = (PotionMeta) i.getItemMeta();
		pm.clearCustomEffects();
		pm.addCustomEffect(new PotionEffect(effect.getEffectType(), seconds * 20, amplifier), true);
		i.setItemMeta(pm);
	}

	private PotionType getRandomPotion() {
		PotionType[] array = PotionType.values();
		Random r = new Random();
		return array[r.nextInt(array.length - 1)];
	}

	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		if (!(event.getEntity().getKiller() instanceof Player))
			return;
		Player p = (Player) event.getEntity().getKiller();
		if (hasAbility(p)) {
			if (!CooldownManager.isInCooldown(p.getUniqueId(), "chemist")) {
				return;
			}
			CooldownManager.stop(p.getUniqueId(), "chemist");
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.BLAZE_POWDER, "§5§lChemist"));
		return new Kit("chemist",
				Arrays.asList(new String[] {
						"Inicie a partida com um item que ao clicar, gera uma pocao com efeito aleatorio caso seja um efeito positivo, será uma pocao que voce consiga beber caso seja um efeito negativo sera uma pocao que voce consiga jogar! Caso voce mate um jogador o cooldown irá resetar" }),
				kitItems, null, new ItemStack(Material.POTION, 1, (short) 16428));
	}

}
