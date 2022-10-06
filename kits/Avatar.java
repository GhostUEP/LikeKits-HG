package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.manager.KitInterface;

public class Avatar extends KitInterface {

	public Avatar(Main main) {
		super(main);
		addAntikit();
	}

	private List<Material> materiais = Arrays.asList(
			new Material[] { Material.QUARTZ_BLOCK, Material.GRASS, Material.REDSTONE_BLOCK, Material.LAPIS_BLOCK });

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		ItemStack i = e.getItem();
		Player p = e.getPlayer();

		if ((hasAbility(e.getPlayer())) && (i != null)
				&& (this.materiais.contains(i.getType()) && (i.hasItemMeta()) && (i.getItemMeta().hasDisplayName()))) {
			if (e.getAction().name().contains("LEFT")) {
				e.setCancelled(true);
				AvatarTipo tipo = AvatarTipo.getTypeByItem(e.getItem().getType());
				if (tipo == AvatarTipo.Ar) {
					p.setItemInHand(createItem(Material.LAPIS_BLOCK, "§1§lAvatar"));
				} else if (tipo == AvatarTipo.Agua) {
					p.setItemInHand(createItem(Material.REDSTONE_BLOCK, "§4§lAvatar"));
				} else if (tipo == AvatarTipo.Fogo) {
					p.setItemInHand(createItem(Material.GRASS, "§6§lAvatar"));
				} else if (tipo == AvatarTipo.Terra) {
					p.setItemInHand(createItem(Material.QUARTZ_BLOCK, "§lAvatar"));
				}
			} else if (e.getAction().name().contains("RIGHT")) {
				e.setCancelled(true);
				if (Main.plugin.stage != Estagio.GAMETIME) {
					p.sendMessage("§cVocê não pode usar isto agora!");
					return;
				}
				if (CooldownManager.isInCooldown(p.getUniqueId(), "avatar")) {
					int timeleft = CooldownManager.getTimeLeft(p.getUniqueId(), "avatar");
					p.sendMessage("§cAvatar em cooldown, faltando: " + timeleft + " segundos");
					return;
				}
				AvatarTipo type = AvatarTipo.getTypeByItem(e.getItem().getType());

				if (p.getLocation().getPitch() == 90) {
					p.setVelocity(new Vector(0, 0.5, 0));
					p.setFallDistance(-5.0F);
					CooldownManager cd = new CooldownManager(p.getUniqueId(), "avatar", 10);
					cd.start();
					p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, type.getMaterial().getId());
					return;
				}

				lancarAvatar(p, type);
			}
		}
	}

	private void lancarAvatar(final Player p, final AvatarTipo tipo) {
		final BlockIterator iterator = new BlockIterator(p.getEyeLocation(), 0, 64);
		new BukkitRunnable() {

			private HashMap<UUID, Integer> hits = new HashMap<UUID, Integer>();

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (iterator.hasNext()) {
					Block b = iterator.next();
					b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, tipo.getMaterial().getId());
					for (Player o : Bukkit.getOnlinePlayers()) {
						if (o.equals(p) || moreThan10(o)) {
							continue;
						}
						if (Main.plugin.isNotPlaying(o))
							continue;
						if (o.getLocation().distance(b.getLocation()) > 3.0D) {
							continue;
						}
						if (hasAntikit(o)) {
							continue;
						}
						addHit(o);
						Main.plugin.darDano(o, p, tipo.getDamage(), false);
						o.addPotionEffect(tipo.getPotionEffect(), true);
						o.setFireTicks(tipo.getFireTicks());
					}
				} else {
					cancel();
				}
			}

			private boolean moreThan10(Player o) {
				if (hits.get(o.getUniqueId()) == null) {
					return false;
				}
				if (hits.get(o.getUniqueId()).intValue() == 10) {
					return true;
				}
				return false;
			}

			private void addHit(Player o) {
				int i = (hits.get(o.getUniqueId()) == null ? 0 : hits.get(o.getUniqueId())) + 1;
				hits.put(o.getUniqueId(), i);
			}
		}.runTaskTimer(Main.plugin, 0L, 1L);
		CooldownManager cd = new CooldownManager(p.getUniqueId(), "avatar", 55);
		cd.start();
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		ItemStack i = e.getItemInHand();
		if (hasAbility(e.getPlayer()) && i != null && i.getType() != Material.AIR && i.hasItemMeta()
				&& i.getItemMeta().hasDisplayName() && materiais.contains(i.getType())
				&& i.getItemMeta().getDisplayName().contains("Avatar")) {
			e.setBuild(false);
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void ondrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (hasAbility(p) && e.getItemDrop().getItemStack() != null && e.getItemDrop().getItemStack().hasItemMeta()
				&& e.getItemDrop().getItemStack().getItemMeta().hasDisplayName()
				&& e.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("Avatar")) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}

	private enum AvatarTipo {

		Ar(Material.QUARTZ_BLOCK, 1.0D, 0, new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 0)), Agua(
				Material.LAPIS_BLOCK, 1.0D, 0, new PotionEffect(PotionEffectType.POISON, 100, 0)), Fogo(
						Material.REDSTONE_BLOCK, 1.0D, 100, new PotionEffect(PotionEffectType.SLOW, 100, 0)), Terra(
								Material.GRASS, 1.0D, 0, new PotionEffect(PotionEffectType.HUNGER, 100, 1));

		private Material material;
		private double damage;
		private int fireTicks;
		private PotionEffect effect;

		private AvatarTipo(Material material, double damage, int fireTicks, PotionEffect effect) {
			this.material = material;
			this.damage = damage;
			this.fireTicks = fireTicks;
			this.effect = effect;
		}

		public Material getMaterial() {
			return this.material;
		}

		public double getDamage() {
			return this.damage;
		}

		public int getFireTicks() {
			return this.fireTicks;
		}

		public PotionEffect getPotionEffect() {
			return this.effect;
		}

		public static AvatarTipo getTypeByItem(Material material) {
			if (material.equals(Material.QUARTZ_BLOCK)) {
				return Ar;
			}
			if (material.equals(Material.REDSTONE_BLOCK)) {
				return Fogo;
			}
			if (material.equals(Material.LAPIS_BLOCK)) {
				return Agua;
			}
			if (material.equals(Material.GRASS)) {
				return Terra;
			}
			return null;
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.QUARTZ_BLOCK, "§lAvatar"));
		return new Kit("avatar",
				Arrays.asList(new String[] {
						"Inicie a partida com 4 poderes unidos em 1 item para alterar o pode aperte(Botao esquerdo) com o item para usar aperte(Botao Direito) com o item cooldown de 55 segundos" }),
				kitItems, null, new ItemStack(Material.LAPIS_BLOCK));
	}

}
