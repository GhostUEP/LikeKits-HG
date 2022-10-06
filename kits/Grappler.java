package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftSnowball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.KitInterface;
import net.minecraft.server.v1_7_R4.EntityFishingHook;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntitySnowball;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;

public class Grappler extends KitInterface {

	public Grappler(Main main) {
		super(main);
	}

	private HashMap<Player, GrapplerHook> hooks = new HashMap<Player, GrapplerHook>();
	private HashMap<Player, Long> cooldown = new HashMap<Player, Long>();
	private HashMap<Player, Long> rightCD = new HashMap<Player, Long>();
	private String message = "§cSeu gancho nao prendeu em nada!";

	@EventHandler
	public void grappler(PlayerItemHeldEvent e) {
		if (this.hooks.containsKey(e.getPlayer())) {
			((GrapplerHook) this.hooks.get(e.getPlayer())).remove();
			this.hooks.remove(e.getPlayer());
		}
	}

	@EventHandler
	public void dano(EntityDamageByEntityEvent event) {
		if (event.getDamager().hasMetadata("corda")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void grappler(PlayerLeashEntityEvent e) {
		if (hasAbility(e.getPlayer())) {
			e.setCancelled(true);
			e.getPlayer().updateInventory();
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getItem() != null && e.getItem().getType() == Material.LEASH && e.getItem().hasItemMeta()
				&& e.getItem().getItemMeta().getDisplayName().contains("Grappler Hook")) && hasAbility(p)) {
			e.setCancelled(true);
			if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
				if (this.cooldown.containsKey(p) && this.cooldown.get(p) > System.currentTimeMillis()) {
					return;
				}
				if (this.hooks.containsKey(p)) {
					((GrapplerHook) this.hooks.get(p)).remove();
				}
				if (this.hitnerf.containsKey(p) && this.hitnerf.get(p) > System.currentTimeMillis()) {
					p.setVelocity(new Vector(0.0D, -1.0D, 0.0D));
					p.sendMessage("§cVocê levou um hit recentemente");
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 5), true);
					return;
				}
				this.cooldown.put(p, System.currentTimeMillis() + 250L);
				GrapplerHook nmsHook = new GrapplerHook(p.getWorld(), ((CraftPlayer) p).getHandle());
				nmsHook.spawn(p.getEyeLocation().add(p.getLocation().getDirection().getX(),
						p.getLocation().getDirection().getY(), p.getLocation().getDirection().getZ()));
				nmsHook.move(p.getLocation().getDirection().getX() * 5.0D, p.getLocation().getDirection().getY() * 5.0D,
						p.getLocation().getDirection().getZ() * 5.0D);
				this.hooks.put(p, nmsHook);
			} else {
				if (!this.hooks.containsKey(p)) {
					return;
				}
				if (this.rightCD.containsKey(p) && this.rightCD.get(p) > System.currentTimeMillis()) {
					return;
				}
				if (!((GrapplerHook) this.hooks.get(p)).isHooked()) {
					p.sendMessage(this.message);
					GrapplerHook gh = (GrapplerHook) this.hooks.get(p);
					gh.move(gh.motX, gh.motY - 2.0D, gh.motY);
					return;
				}
				if (this.hooks.get(p).getBukkitEntity().getLocation().getY() > 128.0D) {
					p.sendMessage("§cSua corda está muito alta!");
					return;
				}
				this.rightCD.put(p, System.currentTimeMillis() + 125L);
				if (this.hooks.get(p).getHooked() instanceof LivingEntity) {
					double d = ((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation()
							.distance(p.getLocation());
					double t = d;
					double v_x = (1.0D + 0.10000000000000001D * t)
							* (((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation().getX()
									- p.getLocation().getX())
							/ t;

					double v_z = (1.0D + 0.10000000000000001D * t)
							* (((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation().getZ()
									- p.getLocation().getZ())
							/ t;
					if (p.isOnGround()) {
						if (((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation().getY()
								- p.getLocation().getY() < 0.5D) {
						}
					}
					double v_y;
					if (hooks.get(p).getBukkitEntity().getLocation().getBlockY() < p.getLocation().getBlockY()) {
						v_y = -1.0D;
					} else {
						v_y = (0.9D + 0.09D * t)
								* ((((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation().getY()
										- p.getLocation().getY()) / t);
					}
					Vector v = p.getVelocity();
					v.setX(v_x);
					v.setY(v_y);
					v.setZ(v_z);
					p.setVelocity(v);
				} else {
					double d = ((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation()
							.distance(p.getLocation());
					double t = d;
					double v_x = (1.0D + 0.03500000000000001D * t)
							* (((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation().getX()
									- p.getLocation().getX())
							/ t;

					double v_z = (1.0D + 0.03500000000000001D * t)
							* (((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation().getZ()
									- p.getLocation().getZ())
							/ t;
					if (p.isOnGround()) {
						if (((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation().getY()
								- p.getLocation().getY() < 0.5D) {
						}
					}
					double v_y;
					if (hooks.get(p).getBukkitEntity().getLocation().getBlockY() < p.getLocation().getBlockY()) {
						v_y = 0.1D;
					} else {
						v_y = (0.9D + 0.03D * t)
								* ((((GrapplerHook) this.hooks.get(p)).getBukkitEntity().getLocation().getY()
										- p.getLocation().getY()) / t);
					}
					Vector v = p.getVelocity();
					v.setX(v_x);
					v.setY(v_y);
					v.setZ(v_z);
					p.setVelocity(v);
					if (this.hooks.get(p).getBukkitEntity().getLocation().getY() > p.getLocation().getY()
							&& p.getFallDistance() > 20.0F) {
						p.setNoDamageTicks(0);
						p.damage(0.0D);
						p.sendMessage("§aSe não fosse por esta corda, voce estaria morto!");
						p.setVelocity(new Vector());
						p.setFallDistance(0.0F);
					} else {
						p.setFallDistance(0.0F);
					}
					p.getWorld().playSound(p.getLocation(), Sound.STEP_GRAVEL, 3.0F, (byte) 1);
				}
			}
		}
	}

	private HashMap<Player, Long> hitnerf = new HashMap<Player, Long>();

	@EventHandler
	public void levarHitNerf(final EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (Main.plugin.stage != Estagio.GAMETIME) {
			return;
		}
		if (event.getDamager() instanceof Player && Main.plugin.adm.isSpectating((Player) event.getDamager())) {
			return;
		}
		if (((event.getEntity() instanceof Player)) && (hasAbility((Player) event.getEntity()))) {
			this.hitnerf.put((Player) event.getEntity(), System.currentTimeMillis() + 6000L);
		}
	}

	@EventHandler
	public void sair(PlayerQuitEvent event) {
		if (this.hooks.containsKey(event.getPlayer())) {
			this.hooks.get(event.getPlayer()).remove();
			this.hooks.remove(event.getPlayer());
		}
	}

	@EventHandler
	public void morrer(PlayerDeathEvent event) {
		if (this.hooks.containsKey(event.getEntity())) {
			this.hooks.get(event.getEntity()).getBukkitEntity().remove();
		}
	}

	public class GrapplerHook extends EntityFishingHook {
		private Snowball sb;
		private EntitySnowball controller;
		public int a;
		public EntityHuman owner;
		public Entity hooked;
		public boolean lastControllerDead;
		public boolean isHooked;

		public GrapplerHook(org.bukkit.World world, EntityHuman entityhuman) {
			super(((CraftWorld) world).getHandle(), entityhuman);
			this.owner = entityhuman;
		}

		public Entity getHooked() {
			return this.hooked;
		}

		protected void c() {
		}

		public void h() {
			if ((!this.lastControllerDead) && (this.controller.dead)) {
				((Player) this.owner.getBukkitEntity()).sendMessage(("§aSua corda esta presa!"));
			}
			this.lastControllerDead = this.controller.dead;
			for (Entity entity : this.controller.world.getWorld().getEntities()) {
				if (!(entity instanceof LivingEntity)) {
					continue;
				}

				if ((!(entity instanceof Firework)) && (entity.getEntityId() != getBukkitEntity().getEntityId())
						&& (entity.getEntityId() != this.owner.getBukkitEntity().getEntityId())
						&& (entity.getEntityId() != this.controller.getBukkitEntity().getEntityId())
						&& ((entity.getLocation().distance(this.controller.getBukkitEntity().getLocation()) < 2.0D)
								|| (((entity instanceof Player)) && ((!Main.plugin.isNotPlaying((Player) entity)))
										&& (((Player) entity).getEyeLocation()
												.distance(this.controller.getBukkitEntity().getLocation()) < 2.0D)))) {
					this.controller.die();
					this.hooked = entity;
					this.isHooked = true;
					this.locX = entity.getLocation().getX();
					this.locY = entity.getLocation().getY();
					this.locZ = entity.getLocation().getZ();
					this.motX = 0.0D;
					this.motY = 0.04D;
					this.motZ = 0.0D;
				}
			}
			try {
				this.locX = this.hooked.getLocation().getX();
				this.locY = this.hooked.getLocation().getY();
				this.locZ = this.hooked.getLocation().getZ();
				this.motX = 0.0D;
				this.motY = 0.04D;
				this.motZ = 0.0D;
				this.isHooked = true;
			} catch (Exception e) {
				if (this.controller.dead) {
					this.isHooked = true;
				}
				this.locX = this.controller.locX;
				this.locY = this.controller.locY;
				this.locZ = this.controller.locZ;
			}
		}

		public void die() {
		}

		public void remove() {
			super.die();
		}

		@SuppressWarnings("deprecation")
		public void spawn(Location location) {
			this.sb = ((Snowball) this.owner.getBukkitEntity().launchProjectile(Snowball.class));
			this.sb.setVelocity(this.sb.getVelocity().multiply(2.25D));
			this.sb.setMetadata("corda", new FixedMetadataValue(Main.plugin, "corda-grappler"));
			this.controller = ((CraftSnowball) this.sb).getHandle();

			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { this.controller.getId() });
			Player[] arrayOfPlayer;
			int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
			for (int i = 0; i < j; i++) {
				Player p = arrayOfPlayer[i];
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			}
			((CraftWorld) location.getWorld()).getHandle().addEntity(this);
		}

		public boolean isHooked() {
			return this.isHooked;
		}

		public void setHookedEntity(Entity damaged) {
			this.hooked = damaged;
		}

	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		kitItems.add(createItem(Material.LEASH, 1, "§r§cGrappler Hook"));
		return new Kit("grappler",
				Arrays.asList(new String[] {
						"Você tem uma corda que ao clicar com botao esquerdo, voce lança um gancho que ao ser preso em um Mob ou Bloco você pode clicar com botao direito para levar Impulso até ele" }),
				kitItems, null, new ItemStack(Material.LEASH));
	}
}
