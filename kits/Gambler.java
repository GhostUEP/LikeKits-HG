package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Gambler extends KitInterface {
	private List<WonItem> itemsToWin = new ArrayList<WonItem>();
	private String whatYouWon = ChatColor.AQUA + "You win --> " + ChatColor.BLUE + "%s";

	public Gambler(Main main) {
		super(main);
		this.itemsToWin.add(new WonItem(new ItemStack(Material.DIAMOND_HELMET), 250));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.DIAMOND_CHESTPLATE), 250));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.DIAMOND_LEGGINGS), 250));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.DIAMOND_BOOTS), 250));
		this.itemsToWin.add(new WonItem(new ItemStack(Material.DIAMOND_SWORD), 250));
		this.itemsToWin.add(new WonItem(new PotionEffect(PotionEffectType.HARM, 1, 6), 500));
		this.itemsToWin.add(new WonItem(new PotionEffect(PotionEffectType.HEAL, 1, 6), 20));
		this.itemsToWin.add(new WonItem(new PotionEffect(PotionEffectType.SATURATION, 100, 6), 1));
		this.itemsToWin.add(new WonItem(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 30, 0), 1));
		this.itemsToWin.add(new WonItem(new PotionEffect(PotionEffectType.POISON, 20 * 30, 0), 1));
		this.itemsToWin.add(new WonItem(new PotionEffect(PotionEffectType.SLOW, 20 * 30, 0), 1));
		this.itemsToWin.add(new WonItem(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 30, 0), 1));
		this.itemsToWin.add(new WonItem(new PotionEffect(PotionEffectType.HUNGER, 20 * 30, 0), 1));
		this.itemsToWin.add(new WonItem(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 30, 0), 50));
		this.itemsToWin.add(new WonItem(getStrengthPotion(), 100));
		this.whatYouWon = "§bVocê ganhou --> §9%s";
	}

	private class WonItem {

		boolean isItemStack = false;
		boolean isPotion = false;
		int chance = 0;

		ItemStack itemstack = null;
		PotionEffect effect = null;

		public WonItem(ItemStack i, int chance) {
			this.isItemStack = true;
			this.itemstack = i;
			this.chance = chance;
		}

		public WonItem(PotionEffect effect, int chance) {
			this.isPotion = true;
			this.effect = effect;
			this.chance = chance;
		}

		public boolean isEffect() {
			return this.isPotion;
		}

		public boolean isItem() {
			return this.isItemStack;
		}

		public ItemStack getItem() {
			return this.itemstack;
		}

		public PotionEffect getEffect() {
			return this.effect;
		}

		public int getChance() {
			return this.chance;
		}

		public String getMessage() {
			String recebeu = "";
			if (this.isItemStack) {
				recebeu = this.itemstack.getType().toString().replace("_", " ").toLowerCase();
			} else {
				recebeu = this.effect.getType().getName().toString().replace("_", " ").toLowerCase();
			}
			return String.format(whatYouWon, recebeu);
		}
	}

	private ItemStack getStrengthPotion() {
		ItemStack i = new ItemStack(Material.POTION, 1, (short) 8201);
		PotionMeta im = (PotionMeta) i.getItemMeta();
		im.clearCustomEffects();
		im.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0), true);
		i.setItemMeta(im);
		return i;
	}

	public WonItem getRandom() {
		Random r = new Random();
		Collections.shuffle(itemsToWin, new Random());
		if (itemsToWin.size() == 0)
			return null;
		while (true) {
			Iterator<WonItem> itel = itemsToWin.iterator();
			while (itel.hasNext()) {
				WonItem item = itel.next();
				if (item.getChance() != 0)
					if (r.nextInt(item.getChance()) == 0)
						return item;
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (hasAbility(event.getPlayer()))
				&& (event.getClickedBlock().getType() == Material.STONE_BUTTON)
				&& (event.getClickedBlock().getData() <= 4) && (event.getClickedBlock().getData() > 0)) {
			Player p = event.getPlayer();
			if (CooldownManager.isInCooldown(p.getUniqueId(), "gambler")) {
				event.setCancelled(true);
				int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "gambler");
				p.sendMessage("§cGambler em cooldown, faltando: " + timeleft + " segundos");
				return;
			}
			CooldownManager cd = new CooldownManager(p.getUniqueId(), "gambler", 15);
			cd.start();
			WonItem wonItem = getRandom();
			if (wonItem.isItem()) {
				p.getWorld().dropItemNaturally(p.getLocation(), wonItem.getItem());
				p.updateInventory();
			} else if (wonItem.isEffect()) {
				PotionEffect effect = wonItem.getEffect();
				p.addPotionEffect(effect, true);
			}
			p.sendMessage(wonItem.getMessage());
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(new ItemStack(Material.STONE_BUTTON));
		return new Kit("gambler",
				Arrays.asList(new String[] {
						"Você inicia a partida com um botao ao colocar-lo e aperta-lo voce pode receber coisas boas ou coisas ruins" }),
				null, kitItems, new ItemStack(Material.STONE_BUTTON));
	}

}
