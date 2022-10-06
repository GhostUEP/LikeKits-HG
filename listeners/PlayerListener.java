package me.ghost.hg.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;
import me.ghost.hg.barapi.BarAPI;
import me.ghost.hg.constructors.KitDiario;
import me.ghost.hg.constructors.Status;
import me.ghost.hg.enums.BoxType;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.events.InvencibilityEndEvent;
import me.ghost.hg.manager.CooldownManager;
import me.ghost.hg.mysql.Connect;
import me.ghost.hg.utils.InventorySelector;
import me.ghost.hg.utils.InventorySelector.SelectorType;
import me.ghost.hg.utils.Nome;
import me.ghost.hg.utils.Wand;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class PlayerListener implements Listener {
	private ItemStack firstKitSelector;
	private ItemStack caixasSelector;
	private ItemStack specSelector;
	public Main main;
	public HashMap<UUID, String> deathMessage = new HashMap<>();
	public static ArrayList<UUID> relog = new ArrayList<UUID>();
	private ArrayList<UUID> reloged = new ArrayList<UUID>();
	private ArrayList<UUID> joined = new ArrayList<UUID>();

	@SuppressWarnings("deprecation")
	public PlayerListener(Main m) {
		main = m;
		ItemStack cacto = new ItemStack(Material.MUSHROOM_SOUP);
		ItemStack cacau = new ItemStack(Material.MUSHROOM_SOUP);
		ItemStack nether = new ItemStack(Material.MUSHROOM_SOUP);
		ItemStack abobora = new ItemStack(Material.MUSHROOM_SOUP);
		ShapelessRecipe cactorec = new ShapelessRecipe(cacto);
		ShapelessRecipe cacaurec = new ShapelessRecipe(cacau);
		ShapelessRecipe netherrec = new ShapelessRecipe(nether);
		ShapelessRecipe aboborarec = new ShapelessRecipe(abobora);
		cactorec.addIngredient(Material.BOWL);
		cactorec.addIngredient(Material.CACTUS);
		cacaurec.addIngredient(Material.BOWL);
		cacaurec.addIngredient(Material.INK_SACK, 3);
		netherrec.addIngredient(Material.BOWL);
		netherrec.addIngredient(Material.NETHER_STALK);
		aboborarec.addIngredient(Material.BOWL);
		aboborarec.addIngredient(Material.PUMPKIN_SEEDS);
		aboborarec.addIngredient(Material.PUMPKIN_SEEDS);
		main.getServer().addRecipe(cactorec);
		main.getServer().addRecipe(cacaurec);
		main.getServer().addRecipe(netherrec);
		main.getServer().addRecipe(aboborarec);
		firstKitSelector = new ItemStack(Material.CHEST);
		ItemMeta im = firstKitSelector.getItemMeta();
		im.setDisplayName("§6Seletor de Kits");
		firstKitSelector.setItemMeta(im);

		caixasSelector = new ItemStack(Material.EMERALD);
		ItemMeta im2 = caixasSelector.getItemMeta();
		im2.setDisplayName("§6Caixas");
		caixasSelector.setItemMeta(im2);

		specSelector = new ItemStack(Material.CHEST);
		ItemMeta im3 = specSelector.getItemMeta();
		im3.setDisplayName("§6Jogadores vivos.");
		specSelector.setItemMeta(im3);

	}

	@EventHandler
	public void onInv(InvencibilityEndEvent e) {
	}

	@EventHandler
	public void onKit(PlayerMoveEvent event) {
		if (!(event.getPlayer() instanceof Player))
			return;
		if (main.stage != Estagio.GAMETIME)
			return;
		if (main.isNotPlaying(event.getPlayer())) {
			return;
		}
		for (Entity kit : event.getPlayer().getNearbyEntities(2.0D, 2.0D, 2.0D)) {
			if (kit instanceof Player) {
				if (kit != event.getPlayer()) {
					Player p2 = (Player) kit;
					if (main.isNotPlaying(p2))
						continue;
					BarAPI.setMessage(event.getPlayer(), p2.getName() + " - " + main.kit.getPlayerKit(p2), 3);
				}
			}
		}
	}

	/*
	 * @EventHandler public void onKit(PlayerMoveEvent event) { if
	 * (!(event.getPlayer() instanceof Player)) return; if (main.stage !=
	 * Estagio.GAMETIME) return; if (main.isNotPlaying(event.getPlayer())) {
	 * return; } for (Entity kit : event.getPlayer().getNearbyEntities(1.0D,
	 * 1.0D, 1.0D)) { if (kit instanceof Player) { if (kit != event.getPlayer())
	 * { Player p2 = (Player) kit; if (main.isNotPlaying(p2)) continue;
	 * main.lookSomeone(event.getPlayer(), p2); } } else {
	 * main.notLookingAtSomeone(event.getPlayer()); } } }
	 */

	@EventHandler
	public void onWorldEdit(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (!main.wands.containsKey(p.getUniqueId()))
			return;
		if (event.getItem() == null)
			return;
		if (event.getItem().getType() != Material.WOOD_AXE)
			return;
		if (!event.getAction().toString().contains("BLOCK"))
			return;
		Wand wand = main.wands.get(p.getUniqueId());
		event.setCancelled(true);
		Location loc = event.getClickedBlock().getLocation();
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			wand.setFirst(loc);
			p.sendMessage("§dPrimeira posicao selecionada (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ()
					+ ") [" + wand.getBlockQuantidade() + "]");
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			wand.setSecond(loc);
			p.sendMessage("§dSegunda posicao selecionada (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ") ["
					+ wand.getBlockQuantidade() + "]");
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void loadKits(PlayerJoinEvent e) {
		main.perm.loadPlayerKits(e.getPlayer().getUniqueId());
		main.applyScore(e.getPlayer());
	}

	@EventHandler
	public void onDamageZombiePigamn(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof PigZombie) {
			e.setDamage(2.5);
			return;
		}
	}

	/*
	 * @EventHandler public void onTimeSecond(TimeSecondEvent event) { if
	 * (main.stage == Estagio.PREGAME) return; for (Player player :
	 * Bukkit.getOnlinePlayers()) { if (PlayerLooking.getEntityInSight(player)
	 * != null) { main.lookSomeone(player,
	 * PlayerLooking.getEntityInSight(player)); } else {
	 * main.notLookingAtSomeone(player); } } }
	 */

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		ItemStack item = event.getItemDrop().getItemStack();
		Player p = event.getPlayer();
		if (item == null || item.getType() == Material.AIR)
			return;
		boolean isItemKit = false;
		for (ItemStack i : main.kit.getPlayerKitItems(p)) {
			if (item.getType() == i.getType()) {
				isItemKit = true;
				break;
			}
		}
		if (isItemKit) {
			event.setCancelled(true);
		}
		if (item.getType() == Material.SKULL_ITEM) {
			event.getItemDrop().remove();
		}
	}

	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event) {
		Material skullItem = Material.SKULL_ITEM;
		if (event.getItemInHand().getType() == skullItem) {
			event.getBlock().setType(Material.AIR);
		}
	}

	@EventHandler
	public void onLimparBoxes(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (main.box.bronzeBoxes.containsKey(p.getUniqueId())) {
			main.box.bronzeBoxes.remove(p.getUniqueId());
		}
		if (main.box.prataBoxes.containsKey(p.getUniqueId())) {
			main.box.prataBoxes.remove(p.getUniqueId());
		}
		if (main.box.goldBoxes.containsKey(p.getUniqueId())) {
			main.box.goldBoxes.remove(p.getUniqueId());
		}
		if (main.box.diamondBoxes.containsKey(p.getUniqueId())) {
			main.box.diamondBoxes.remove(p.getUniqueId());
		}
	}

	public void deathMessage(String message) {
		main.getServer().broadcastMessage(message);
		String s = "";
		String s2 = "";
		if (main.gamers.size() > 1) {
			s = "es";
			s2 = "s";
		}
		main.getServer().broadcastMessage("§6" + (main.gamers.size() - 1) + " jogador" + s + " restante" + s2);
	}

	@EventHandler
	public void onGenerate(EntityCreatePortalEvent event) {
		if (event.getEntity() instanceof EnderDragon) {
			for (BlockState b : event.getBlocks()) {
				if (b.getType() == Material.ENDER_PORTAL) {
					b.setType(Material.STATIONARY_WATER);
				}
			}
		}
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		if (!main.drops)
			event.setCancelled(true);
	}

	@EventHandler
	public void onStatus(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		Status status = Status.getStatus(p);
		if (status.getname().equalsIgnoreCase("")) {
			try {
				status.insertStatus(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void Sopa(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getAction().name().contains("RIGHT")) {
			ItemStack hand = player.getItemInHand();
			if ((hand != null) && (hand.getType() == Material.MUSHROOM_SOUP)) {
				if (main.stage == Estagio.PREGAME)
					return;
				EntityPlayer p = ((CraftPlayer) player).getHandle();
				if (p.getHealth() < p.getMaxHealth()) {
					if (p.getHealth() + 7 >= p.getMaxHealth()) {
						p.setHealth(p.getMaxHealth());
						player.getItemInHand().setType(Material.BOWL);
						player.updateInventory();
					} else {
						p.setHealth(p.getHealth() + 7);
						player.getItemInHand().setType(Material.BOWL);
						player.updateInventory();
					}
				} else if (player.getFoodLevel() < 20) {
					if (player.getFoodLevel() + 6 < 20.0F) {
						player.setFoodLevel((int) (player.getFoodLevel() + 6));
					} else {
						player.setFoodLevel((int) (player.getFoodLevel() + 6));
					}
					player.getItemInHand().setType(Material.BOWL);
					player.updateInventory();
				} else {
					player.getFoodLevel();
				}
			}
			if (hand != null && hand.getType() == Material.WOOL) {
				if (main.stage == Estagio.PREGAME)
					return;
				if (hand.getItemMeta() != null && hand.getItemMeta().getDisplayName() != null) {
					e.setCancelled(true);
					String kit = hand.getItemMeta().getDisplayName().replace("Presente para o kit: ", "").toLowerCase();
					if (main.kit.hasAbility(player, kit)) {
						player.setItemInHand(null);
						main.kit.giveMini(player);
						player.updateInventory();
						player.sendMessage("§cVoce recebeu seu Kit.");
					}
				}
			}
		}
		if (main.stage != Estagio.PREGAME) {
			String bussola = "§cNenhum jogador encontrado. Apontando para o spawn";
			ItemStack item = e.getItem();
			if (item == null || item.getType() == Material.AIR)
				return;
			if (item.getType() == Material.COMPASS) {
				Player target = null;
				double distance = 10000;
				if (!CooldownManager.isInCooldown(player.getUniqueId(), "bussola")) {
					for (Player game : Bukkit.getOnlinePlayers()) {
						if (main.isNotPlaying(game))
							continue;
						double distOfPlayerToVictim = player.getLocation().distance(game.getPlayer().getLocation());
						if (distOfPlayerToVictim < distance && distOfPlayerToVictim > 25) {
							distance = distOfPlayerToVictim;
							target = game;
						}
					}
				}
				if (target == null) {
					if (!CooldownManager.isInCooldown(player.getUniqueId(), "bussola")) {
						bussola = "§cNenhum jogador encontrado. Apontando para o spawn";
						player.setCompassTarget(Bukkit.getWorlds().get(0).getSpawnLocation());
						CooldownManager c = new CooldownManager(player.getUniqueId(), "bussola", 1);
						c.start();
					}
					player.sendMessage(bussola);
				} else {
					if (!CooldownManager.isInCooldown(player.getUniqueId(), "bussola")) {
						player.setCompassTarget(target.getLocation());
						bussola = "§6Proxima vitima: §7" + target.getName();
						CooldownManager c = new CooldownManager(player.getUniqueId(), "bussola", 1);
						c.start();
					}
					player.sendMessage(bussola);
				}
			}
		}

	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (main.stage == Estagio.PREGAME) {
			if (item == null)
				return;
			if (!item.hasItemMeta())
				return;
			if (!item.getItemMeta().hasDisplayName())
				return;
			e.setCancelled(true);
			if (CooldownManager.isInCooldown(p.getUniqueId(), "menus")) {
				p.sendMessage("§cEspere para abrir novamente");
				return;
			}
			if (item.getItemMeta().getDisplayName().contains("Seletor de Kits")) {
				new InventorySelector(p, main.kit, SelectorType.FIRSTKIT).open();
				CooldownManager cd = new CooldownManager(p.getUniqueId(), "menus", 2);
				cd.start();
				return;
			}
			if (item.getItemMeta().getDisplayName().contains("Kit Diario")) {
				main.kit.setKitDiário(p);
				CooldownManager cd = new CooldownManager(p.getUniqueId(), "menus", 2);
				cd.start();
				p.updateInventory();
				return;
			}
			if (item.getItemMeta().getDisplayName().contains("Caixas")) {
				main.box.openBox(p);
				CooldownManager cd = new CooldownManager(p.getUniqueId(), "menus", 2);
				cd.start();
				return;
			}
		}
	}

	@EventHandler
	public void onInteract(InventoryClickEvent event) {
		if (event.getInventory().getName().equalsIgnoreCase("Caixas")) {
			if (event.getWhoClicked() instanceof Player) {
				Player p = (Player) event.getWhoClicked();
				event.setCancelled(true);
				if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.CHEST) {
					if (event.getCurrentItem().hasItemMeta()
							&& event.getCurrentItem().getItemMeta().getDisplayName().contains("Ouro")) {
						main.box.openBox(p, BoxType.OURO);
					} else if (event.getCurrentItem().hasItemMeta()
							&& event.getCurrentItem().getItemMeta().getDisplayName().contains("Diamante")) {
						main.box.openBox(p, BoxType.DIAMANTE);
					} else if (event.getCurrentItem().hasItemMeta()
							&& event.getCurrentItem().getItemMeta().getDisplayName().contains("Prata")) {
						main.box.openBox(p, BoxType.PRATA);
					} else if (event.getCurrentItem().hasItemMeta()
							&& event.getCurrentItem().getItemMeta().getDisplayName().contains("Bronze")) {
						main.box.openBox(p, BoxType.BRONZE);
					}
				}
			}
		}
		if (main.stage == Estagio.PREGAME) {
			if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.ENDER_CHEST) {
				if (event.getCurrentItem().hasItemMeta()
						&& event.getCurrentItem().getItemMeta().getDisplayName().contains("Kit Diario")) {
					event.setCancelled(true);
					event.getWhoClicked().setItemOnCursor(null);
				}
			}
		}
		if (event.getInventory().getName().contains("Caixa")) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);
		}
	}

	@EventHandler
	public void onSendItems(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (main.stage == Estagio.PREGAME) {
			p.getInventory().clear();
			p.getInventory().addItem(firstKitSelector);
			p.getInventory().setItem(4, caixasSelector);
			ItemStack item = new ItemStack(Material.ENDER_CHEST);
			ItemMeta itemm = item.getItemMeta();
			itemm.setDisplayName("§6Kit Diario : " + main.kit.getKitName(main.kit.getKitDiário(p)));
			item.setItemMeta(itemm);
			p.getInventory().setItem(8, item);
		}
	}

	@EventHandler
	public void onTeleport(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (main.stage == Estagio.PREGAME) {
			main.sendToSpawn(p);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		if (main.stage != Estagio.PREGAME) {
			if (!main.isNotPlaying(p)) {
				if (relog.contains(p.getUniqueId())) {
					if (!reloged.contains(p.getUniqueId()))
						return;
				}
			}
			if (main.GameTimer > 300) {
				if (!main.perm.isPro(p)) {
					if (deathMessage.containsKey(p.getUniqueId()))
						event.disallow(Result.KICK_OTHER, deathMessage.get(p.getUniqueId()));
					else
						event.disallow(Result.KICK_OTHER, "§cO jogo já começou!");
				}
			} else {
				if (main.perm.isMvp(p) && joined.contains(p.getUniqueId())) {
					if (!main.perm.isPro(p)) {
						if (deathMessage.containsKey(p.getUniqueId()))
							event.disallow(Result.KICK_OTHER, deathMessage.get(p.getUniqueId()));
						else
							event.disallow(Result.KICK_OTHER, "§cO jogo já começou!");
					}
				} else if (!main.perm.isMvp(p)) {
					if (deathMessage.containsKey(p.getUniqueId()))
						event.disallow(Result.KICK_OTHER, deathMessage.get(p.getUniqueId()));
					else
						event.disallow(Result.KICK_OTHER, "§cO jogo já começou!");
				}
			}
		}
		if (event.getResult() == Result.KICK_FULL
				|| (main.getServer().getOnlinePlayers().length - main.adm.admin.size()) >= 100) {
			if (main.perm.isMvp(p)) {
				event.allow();
			} else {
				event.disallow(Result.KICK_FULL,
						"§cServidor lotado. Compre vip em: " + main.site + " para entrar em um servidor lotados.");
			}
		}
	}

	@EventHandler
	public void ListaDeServidores(ServerListPingEvent e) {
		if (main.stage == Estagio.PREGAME) {
			e.setMotd("       §6§lLikeHG §b§l>> §a§lIniciando em " + main.getHourTime(main.PreGameTimer));
		} else {
			e.setMotd("       §6§lLikeHG §b§l>> §c§lPartida em Andamento " + main.getHourTime(main.GameTimer));
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		p.setAllowFlight(false);
		p.setFlying(false);
		p.setGameMode(GameMode.SURVIVAL);
		event.setJoinMessage("§6" + p.getName() + "§7" + " entrou no servidor");
		if (main.stage == Estagio.PREGAME) {
			event.setJoinMessage(null);
		}
		if (main.isNotPlaying(p)) {
			event.setJoinMessage(null);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSetSpectating(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if (relog.contains(p.getUniqueId()) && !main.isNotPlaying(p)) {
			reloged.add(p.getUniqueId());
			new BukkitRunnable() {
				@Override
				public void run() {
					relog.remove(p.getUniqueId());
				}
			}.runTaskLater(main, 30 * 20);
			return;
		}
		p.setGameMode(GameMode.SURVIVAL);
		if (!main.perm.isTrial(p)) {
			main.adm.setPlayer(p);
		}
		boolean needUpdate = true;
		if (main.stage == Estagio.PREGAME) {
			if (main.perm.isTrial(p)) {
				main.adm.setAdmin(p);
				needUpdate = false;
			}
		} else {
			if (!joined.contains(p.getUniqueId()) && main.GameTimer < 300 && !desistiu(p)) {
				main.TotalPlayers += 1;
				p.setAllowFlight(false);
				p.setFlying(false);
				p.getInventory().clear();
				p.getInventory().addItem(new ItemStack(Material.COMPASS, 1));
				p.updateInventory();
				joined.add(p.getUniqueId());
				main.gamers.add(p.getUniqueId());
				Location loc = main.getRespawnLocation();
				if (!loc.getChunk().isLoaded()) {
					loc.getChunk().load(true);
				}
				p.teleport(loc);
				p.sendMessage("§6Você entrou na partida depois que ela já começou, use /kit para escolher um kit");
			} else {
				if (main.perm.isTrial(p)) {
					p.getInventory().clear();
					main.adm.setAdmin(p);
				} else {
					p.getInventory().clear();
					p.getInventory().setItem(0, specSelector);
					main.adm.setYoutuber(p);
					needUpdate = false;
				}
			}
		}
		if (needUpdate)
			main.vanish.updateVanished();
	}

	public boolean desistiu(Player p) {
		if (deathMessage.containsKey(p.getUniqueId())) {
			if (deathMessage.get(p.getUniqueId()).contains("desistiu")) {
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		event.setRespawnLocation(p.getLocation());
		if (main.perm.isTrial(p)) {
			if (!main.adm.isAdmin(p)) {
				main.adm.setAdmin(p);
				p.getInventory().setItem(1, specSelector);
			}
		} else {
			if (!main.adm.isYTUT(p)) {
				main.adm.setYoutuber(p);
				p.getInventory().setItem(1, specSelector);
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final Player p = event.getPlayer();
		if (p.isDead()) {
			event.setQuitMessage(null);
			return;
		}
		if (main.adm.admin.contains(p)) {
			main.adm.admin.remove(p);
		}
		event.setQuitMessage("§6" + p.getName() + "§7 saiu do servidor");
		if (main.isNotPlaying(p) || main.stage == Estagio.PREGAME) {
			event.setQuitMessage(null);
			return;
		}
		if (main.winner == p.getUniqueId()) {
			main.getServer().shutdown();
			return;
		}
		final String color = "§7";
		if (!relog.contains(p.getUniqueId())) {
			relog.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				public void run() {
					if (!reloged.contains(p.getUniqueId())) {
						relog.remove(p.getUniqueId());
						String player = "§6" + p.getName() + "(" + main.kit.getPlayerKit(p) + ")";
						deathMessage.put(p.getUniqueId(), player + color + " desistiu do jogo");
						deathMessage(player + color + " desistiu do jogo");
						main.dropItems(p, p.getLocation());
						main.removeGamer(p);
						main.checkWinner();
					} else {
						reloged.remove(p.getUniqueId());
					}
				}
			}, 30 * 20);
			return;
		}
		if (main.stage == Estagio.GAMETIME && !p.isDead()) {
			String player = "§6" + p.getName() + "(" + main.kit.getPlayerKit(p) + ")";
			deathMessage.put(p.getUniqueId(), player + color + " desistiu do jogo");
			deathMessage(player + color + " desistiu do jogo");
			main.dropItems(p, p.getLocation());
			main.kit.FIRSTKITS.remove(p.getUniqueId());
			main.kit.SECONDKITS.remove(p.getUniqueId());
			main.removeGamer(p);
			main.checkWinner();
		}
	}

	@EventHandler
	public void onstaffchat(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (main.stage != Estagio.PREGAME && main.stage != Estagio.WINNER && main.isNotPlaying(p)
				&& !main.perm.isTrial(p)) {
			e.setCancelled(true);
			main.MandarParaSpecs("§7" + "[MORTO]" + p.getDisplayName() + "§7: " + msg);
		}
		if (!main.chat && !main.perm.isYoutuber(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onAsync(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			return;
		}
		Connect.lock.lock();
		try {
			main.sqlcmd.setPlayerBoxesandXP(event.getUniqueId());
		} catch (SQLException e) {
			e.printStackTrace();
			event.disallow(org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
					"§cNao foi possivel carregar suas caixar e xps.");
		} finally {
			Connect.lock.unlock();
		}
	}

	@EventHandler
	public void sair(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (main.reply.containsKey(p)) {
			Player player = main.reply.get(p);
			if (main.reply.containsKey(player)) {
				main.reply.remove(player);
			}
			if (main.reply.containsKey(p)) {
				main.reply.remove(p);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDeath(PlayerDeathEvent e) {
		final Player p = e.getEntity();
		if (main.isNotPlaying(p)) {
			e.getDrops().clear();
			e.setDroppedExp(0);
			e.setDeathMessage(null);
			return;
		}
		if (main.stage == Estagio.PREGAME) {
			e.setDeathMessage(null);
			return;
		}
		if (main.perm.isMvp(p) && main.GameTimer < 300 && !e.getDeathMessage().contains("desistiu")) {
			if (!main.evento) {
				if (main.stage != Estagio.PREGAME) {
					if (p.getKiller() != null) {
						Player killer = p.getKiller();
						main.addKill(killer);
					}
					Player victim = e.getEntity();
					Player killer = e.getEntity().getKiller();
					if (killer instanceof Player && victim instanceof Player) {
						if ((killer != null) && (!victim.equals(killer))) {
							Status statusv = Status.getStatus(victim);
							Status statusk = Status.getStatus(killer);
							statusv.addDeath(victim);
							statusk.addKill(killer);
						}
					}
				}
			}
			main.dropItems(p, e.getDrops(), p.getLocation());
			e.getDrops().clear();
			p.setHealth(20.0);
			p.setFoodLevel(20);
			p.setSaturation(5);
			for (PotionEffect pot : p.getActivePotionEffects()) {
				p.removePotionEffect(pot.getType());
				break;
			}
			Location loc = main.getRespawnLocation();
			if (!loc.getChunk().isLoaded()) {
				loc.getChunk().load(true);
			}
			p.closeInventory();
			p.setFireTicks(0);
			e.setDeathMessage(null);
			new BukkitRunnable() {
				@Override
				public void run() {
					p.teleport(loc.clone().add(0, 0.5, 0));
					p.getInventory().clear();
					p.getInventory().addItem(new ItemStack(Material.COMPASS));
					p.setFireTicks(0);
					p.updateInventory();
				}
			}.runTaskLater(main, 1);
			return;
		}
		if (!main.evento) {
			if (main.stage != Estagio.PREGAME) {
				if (p.getKiller() != null) {
					Player killer = p.getKiller();
					main.addKill(killer);
				}
				Player victim = e.getEntity();
				Player killer = e.getEntity().getKiller();
				if (killer instanceof Player && victim instanceof Player) {
					if ((killer != null) && (!victim.equals(killer))) {
						Status statusv = Status.getStatus(victim);
						Status statusk = Status.getStatus(killer);
						statusv.addDeath(victim);
						statusk.addKill(killer);
					}
				}
			}
		}
		String player = "§6" + p.getName() + "(" + main.kit.getPlayerKit(p) + ")";
		DamageCause cause = p.getLastDamageCause().getCause();
		String cor = "§7";
		String messageDeath = "";
		if (p.getLastDamageCause() != null && p.getLastDamageCause().getCause() != null) {
			switch (cause) {
			case BLOCK_EXPLOSION:
				messageDeath = player + cor + " morreu explodido";
				deathMessage(messageDeath);
				break;
			case CONTACT:
				messageDeath = player + cor + " foi espetado até a morte";
				deathMessage(messageDeath);
				break;
			case CUSTOM:
				if (e.getDeathMessage().contains("desistiu")) {
					messageDeath = player + cor + " desistiu do jogo";
					deathMessage(messageDeath);
				} else {
					messageDeath = player + cor + " morreu para a borda do mundo";
					deathMessage(messageDeath);
				}
				break;
			case DROWNING:
				messageDeath = player + cor + " morreu afogado";
				deathMessage(messageDeath);
				break;
			case ENTITY_ATTACK:
				if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent entity = (EntityDamageByEntityEvent) p.getLastDamageCause();
					if (entity.getDamager() instanceof Player) {
						Player killer = p.getKiller();
						String kill = "§6" + killer.getName() + "(" + main.kit.getPlayerKit(killer) + ")";
						String arma = Nome.getItemName(killer.getItemInHand());
						messageDeath = kill + cor + " acabou com a vida de " + player + cor + " com " + arma;
						deathMessage(messageDeath);
					} else {
						if (entity.getDamager().getType().name().contains("ZOMBIE")) {
							messageDeath = player + cor + " foi devorado por um Zumbi";
							deathMessage(messageDeath);
						} else if (entity.getDamager().getType().name().contains("SKELETON")) {
							messageDeath = player + cor + " morreu para as flechas de um Esqueleto";
							deathMessage(messageDeath);
						} else {
							messageDeath = player + cor + " morreu para "
									+ entity.getDamager().getType().toString().replace("_", "").toLowerCase();
							deathMessage(messageDeath);
						}
					}
				} else {
					messageDeath = player + cor + " morreu";
					deathMessage(messageDeath);
				}
				break;
			case ENTITY_EXPLOSION:
				messageDeath = player + cor + " foi explodido em 1000 pedaços por um Creeper";
				deathMessage(messageDeath);
				break;
			case FALL:
				messageDeath = player + cor + " caiu de muito alto e morreu";
				deathMessage(messageDeath);
				break;
			case FALLING_BLOCK:
				messageDeath = player + cor + " foi esmagado até a morte";
				deathMessage(messageDeath);
				break;
			case FIRE:
				messageDeath = player + cor + " queimou até sua morte";
				deathMessage(messageDeath);
				break;
			case FIRE_TICK:
				messageDeath = player + cor + " queimou até sua morte";
				deathMessage(messageDeath);
				break;
			case LAVA:
				messageDeath = player + cor + " tentou nadar na lava";
				deathMessage(messageDeath);
				break;
			case LIGHTNING:
				messageDeath = player + cor + " foi atingido por um raio e morreu";
				deathMessage(messageDeath);
				break;
			case MAGIC:
				messageDeath = player + cor + " foi enfeitiçado e morreu";
				deathMessage(messageDeath);
				break;
			case MELTING:
				messageDeath = player + cor + " morreu derretido";
				deathMessage(messageDeath);
				break;
			case POISON:
				messageDeath = player + cor + " foi envenenado até sua morte";
				deathMessage(messageDeath);
				break;
			case PROJECTILE:
				if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent entity = (EntityDamageByEntityEvent) p.getLastDamageCause();
					if (entity.getDamager() instanceof Projectile) {
						Projectile projectile = (Projectile) entity.getDamager();
						ProjectileSource shooter = projectile.getShooter();
						String arma = projectile.getType().toString().toLowerCase();
						if (shooter instanceof Player) {
							Player killer = (Player) shooter;
							String kill = "§6" + killer.getName() + "(" + main.kit.getPlayerKit(killer) + ")";
							messageDeath = kill + cor + " acertou a cabeça de " + player + cor + " com " + arma;
							deathMessage(messageDeath);
						} else {
							messageDeath = player + cor + " morreu";
							deathMessage(messageDeath);
						}
					} else {
						messageDeath = player + cor + " morreu";
						deathMessage(messageDeath);
					}
				} else {
					messageDeath = player + cor + " morreu";
					deathMessage(messageDeath);
				}
				break;
			case STARVATION:
				messageDeath = player + cor + " morreu de fome";
				deathMessage(messageDeath);
				break;
			case SUFFOCATION:
				messageDeath = player + cor + " foi sufocado até a morte";
				deathMessage(messageDeath);
				break;
			case SUICIDE:
				messageDeath = player + cor + " não aguentou a pressão e se matou";
				deathMessage(messageDeath);
				break;
			case THORNS:
				messageDeath = player + cor + " morreu espetado por uma armadura";
				deathMessage(messageDeath);
				break;
			case VOID:
				messageDeath = player + cor + " morreu fora do forcefield";
				deathMessage(messageDeath);
				break;
			case WITHER:
				messageDeath = player + cor + " apodreceu até a morte";
				deathMessage(messageDeath);
				break;
			default:
				messageDeath = player + cor + " morreu";
				deathMessage(messageDeath);
				break;

			}
		} else {
			messageDeath = player + cor + " morreu";
			deathMessage(messageDeath);
		}
		e.setDeathMessage(null);
		main.removeGamer(p);
		main.checkWinner();
		main.removeKills(p);
		p.getInventory().clear();
		if (!main.perm.isPro(p)) {
			if (main.gamers.size() + 1 <= 10) {
				p.kickPlayer("§6Você conseguiu a posição " + (main.gamers.size() + 1) + " de " + main.TotalPlayers
						+ ". parabéns! \n" + messageDeath);
				deathMessage.put(p.getUniqueId(), "Voce morreu! " + messageDeath);
				main.dropItems(p, e.getDrops(), p.getLocation());
				main.kit.FIRSTKITS.remove(p.getUniqueId());
				main.kit.SECONDKITS.remove(p.getUniqueId());
				e.getDrops().clear();
				return;
			}
			if (main.GameTimer <= 300) {
				p.kickPlayer("§6Voce morreu! Compre VIP em " + main.site + " e de respawn nos primeiros 5 minutos! \n"
						+ messageDeath);
			} else {
				p.kickPlayer("§6Voce morreu! \n" + messageDeath);
			}
			deathMessage.put(p.getUniqueId(), "Voce morreu! " + messageDeath);
		} else if (main.perm.isTrial(p)) {
			deathMessage.put(p.getUniqueId(), "Voce morreu! " + messageDeath);
			main.adm.setAdmin(p);
		} else {
			deathMessage.put(p.getUniqueId(), "Voce morreu! " + messageDeath);
			main.adm.setYoutuber(p);
		}
		main.dropItems(p, e.getDrops(), p.getLocation());
		main.kit.FIRSTKITS.remove(p.getUniqueId());
		main.kit.SECONDKITS.remove(p.getUniqueId());
		main.kit.ANTIKIT.remove(p.getUniqueId());
		e.getDrops().clear();
		e.setDeathMessage(null);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onAsyncKit(AsyncPlayerPreLoginEvent event) {
		if (main.stage == Estagio.GAMETIME && main.GameTimer > 300)
			return;
		try {
			main.sqlcmd.loadExpire(event.getUniqueId());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		final UUID uuid = event.getUniqueId();
		if (!main.sqlcmd.expires.containsKey(uuid))
			return;
		final KitDiario kit = main.sqlcmd.getKitDiario(uuid);
		if (kit == null)
			return;
		if (kit.getExpire() < System.currentTimeMillis()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					try {
						main.sqlcmd.removeExpire(kit.getUuid());
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					kit.setKit(null);
					main.sqlcmd.expires.remove(uuid);
					new BukkitRunnable() {
						@Override
						public void run() {
							Player target = Bukkit.getPlayer(uuid);
							if (target != null) {
								target.sendMessage("§cSeu kit diário acabou, você já pode pegar outro");
								ItemStack item = new ItemStack(Material.ENDER_CHEST);
								ItemMeta itemm = item.getItemMeta();
								itemm.setDisplayName("§6Kit Diario : Nenhum");
								item.setItemMeta(itemm);
								target.getInventory().setItem(8, item);
							}
						}
					}.runTaskLater(Main.plugin, 5);

				}
			}.runTaskLaterAsynchronously(Main.plugin, 40);
		}
	}
}
