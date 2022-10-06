package me.ghost.hg;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.ghost.hg.admin.Mode;
import me.ghost.hg.admin.Vanish;
import me.ghost.hg.api.ScoreboardAPI;
import me.ghost.hg.barapi.BarAPI;
import me.ghost.hg.bungeecord.IncomingChannel;
import me.ghost.hg.commands.AdminCMD;
import me.ghost.hg.commands.BoxCMD;
import me.ghost.hg.commands.BuildCMD;
import me.ghost.hg.commands.ChatCMD;
import me.ghost.hg.commands.CreatearenaCMD;
import me.ghost.hg.commands.DanoCMD;
import me.ghost.hg.commands.DropsCMD;
import me.ghost.hg.commands.FeastCMD;
import me.ghost.hg.commands.FfeastCMD;
import me.ghost.hg.commands.FlyCMD;
import me.ghost.hg.commands.ForcekitCMD;
import me.ghost.hg.commands.GamemodeCMD;
import me.ghost.hg.commands.HelpCMD;
import me.ghost.hg.commands.InvseeCMD;
import me.ghost.hg.commands.KitCMD;
import me.ghost.hg.commands.MacrotestCMD;
import me.ghost.hg.commands.MinifeastCMD;
import me.ghost.hg.commands.PitCMD;
import me.ghost.hg.commands.ReviveCMD;
import me.ghost.hg.commands.SecondkitCMD;
import me.ghost.hg.commands.SkitCMD;
import me.ghost.hg.commands.SpawnCMD;
import me.ghost.hg.commands.SpecsCMD;
import me.ghost.hg.commands.StartCMD;
import me.ghost.hg.commands.StatusCMD;
import me.ghost.hg.commands.TellCMD;
import me.ghost.hg.commands.TempoCMD;
import me.ghost.hg.commands.TpCMD;
import me.ghost.hg.config.Config;
import me.ghost.hg.config.ConfigEnum;
import me.ghost.hg.constructors.BO3Blocks;
import me.ghost.hg.constructors.Status;
import me.ghost.hg.enums.BoxType;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.events.GameStartEvent;
import me.ghost.hg.events.PlayerStartGameEvent;
import me.ghost.hg.events.UpdateScheduler;
import me.ghost.hg.listeners.CombatLogListener;
import me.ghost.hg.listeners.DamagerFixer;
import me.ghost.hg.listeners.EspectadorListener;
import me.ghost.hg.listeners.EspectatorInv;
import me.ghost.hg.listeners.ForceFieldListener;
import me.ghost.hg.listeners.GameListener;
import me.ghost.hg.listeners.PlayerListener;
import me.ghost.hg.listeners.PregameListener;
import me.ghost.hg.manager.AbilityManager;
import me.ghost.hg.manager.BoxManager;
import me.ghost.hg.manager.CombatLogManager;
import me.ghost.hg.manager.KitManager;
import me.ghost.hg.manager.Permissions;
import me.ghost.hg.manager.ReflectionManager;
import me.ghost.hg.mysql.Connect;
import me.ghost.hg.mysql.MySQLCommands;
import me.ghost.hg.packets.Injector;
import me.ghost.hg.utils.Pit;
import me.ghost.hg.utils.Wand;
import net.minecraft.server.v1_7_R4.EntityLiving;

public class Main extends JavaPlugin {
	public int PreGameTimer = 300;
	public int GameTimer = 0;
	public Integer Invenci = 120;
	public Estagio stage = Estagio.PREGAME;
	public static Main plugin;
	public ReflectionManager rm = new ReflectionManager();
	public Config config = new Config(this);
	public String host = "";
	public String database = "";
	public String password = "";
	public String user = "";
	public String port = "3306";
	public String bossBarMessage = "";
	public String ipshort = "?";
	public int TotalPlayers;
	public ArrayList<UUID> gamers = new ArrayList<>();
	public ArrayList<UUID> isFlying = new ArrayList<>();
	public Permissions perm;
	public KitManager kit;
	public BoxManager box;
	public Vanish vanish = new Vanish(this);
	public Mode adm = new Mode(this);
	public ArrayList<Block> forcefieldblock2 = new ArrayList<>();
	private HashMap<UUID, Integer> playerKills;
	public String site = "http://loja.likekits.com.br";
	public boolean timeChanged = false;
	public UUID winner = null;
	private CombatLogManager combatLog;
	public PlayerListener listener;
	public HashMap<UUID, String> kitToSecond = new HashMap<>();
	public MySQLCommands sqlcmd = new MySQLCommands(this);
	public Connect connection = new Connect(this);
	public static Connection con;
	public boolean sql = true;
	public String antikit_message = "§cJogador possui antikit";
	public List<Block> portaColiseu = new ArrayList<>();
	public List<Block> blocosColiseu = new ArrayList<>();
	public HashMap<UUID, Wand> wands = new HashMap<>();
	public HashMap<Player, Player> reply = new HashMap<Player, Player>();
	// LISTA DE BO3s
	public ArrayList<BO3Blocks> cake;
	public ArrayList<BO3Blocks> coliseu;
	public ArrayList<BO3Blocks> gladiator;
	public ArrayList<BO3Blocks> pit;
	public ArrayList<BO3Blocks> structure;
	public ArrayList<BO3Blocks> cheststructure;
	public ArrayList<BO3Blocks> ministructure;
	public static Pit pit2;
	public boolean entrar = false;
	// EVENTOS
	public boolean evento = false;
	public boolean dano = true;
	public boolean pvp = true;
	public boolean drops = true;
	public boolean chat = true;
	public boolean build = true;
	// COMANDOS EM GERAL
	public ConcurrentHashMap<UUID, Integer> clicks = new ConcurrentHashMap<UUID, Integer>();

	@EventHandler
	public void onLoad() {
		config.loadConfig();
		String world;
		if (config.fileExists(ConfigEnum.CONFIG))
			world = config.getConfig(ConfigEnum.CONFIG).getString("world-name");
		else
			world = "world";
		getServer().unloadWorld(world, true);
		deleteDir(new File(world));
		getLogger().info("Apagando mundo...");
		Injector.inject();
	}

	@Override
	public void onEnable() {
		plugin = this;
		prepareConfig();
		structure = loadBO3("feast");
		ministructure = loadBO3("minefeast");
		cheststructure = loadBO3("chests");
		cake = loadBO3("cake");
		coliseu = loadBO3("coliseu");
		gladiator = loadBO3("gladiator");
		pit = loadBO3("pit");
		pit2 = new Pit(Main.plugin);
		connection.trySQLConnection();
		perm = new Permissions(this);
		kit = new KitManager(this);
		box = new BoxManager(this);
		playerKills = new HashMap<>();
		combatLog = new CombatLogManager();
		listener = new PlayerListener(this);
		AbilityManager hm = new AbilityManager(this);
		hm.registerAbilityListeners();
		getServer().getPluginManager().registerEvents(new ForceFieldListener(this), this);
		getServer().getPluginManager().registerEvents(new GameListener(this), this);
		getServer().getPluginManager().registerEvents(new PregameListener(this), this);
		getServer().getPluginManager().registerEvents(listener, this);
		getServer().getPluginManager().registerEvents(new CombatLogListener(combatLog), this);
		getServer().getPluginManager().registerEvents(new PregameListener(this), this);
		getServer().getPluginManager().registerEvents(new EspectadorListener(this), this);
		getServer().getPluginManager().registerEvents(new EspectatorInv(this), this);
		getServer().getPluginManager().registerEvents(new DamagerFixer(), this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new IncomingChannel(this));
		getServer().getScheduler().runTaskTimer(this, new UpdateScheduler(), 1, 1); 
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				World world = getServer().getWorld(config.getConfig(ConfigEnum.CONFIG).getString("world-name"));
				world.setSpawnLocation(0, getServer().getWorlds().get(0).getHighestBlockYAt(0, 0), 0);
				world.setDifficulty(Difficulty.NORMAL);
				if (world.hasStorm())
					world.setStorm(false);
				world.setWeatherDuration(999999999);
				world.setGameRuleValue("doDaylightCycle", "false");
				world.setGameRuleValue("doMobSpawning", "false");

			}
		});
		registrarComandos();
		changeViewDistance(3);
		getLogger().log(Level.INFO, "Gerando Borda e Coiseu");
		gerarBorda();
		generateColiseu();
		new BukkitRunnable() {

			@Override
			public void run() {
				Location mainBlock = new Location(
						Bukkit.getWorld(config.getConfig(ConfigEnum.CONFIG).getString("world-name")), 0, 120, 0);
				for (double x = -30; x <= 30; x++) {
					for (double z = -30; z <= 30; z++) {
						for (double y = 1; y <= 9; y++) {
							Location loc = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 120 + y,
									mainBlock.getZ() + z);
							blocosColiseu.add(loc.getBlock());
						}
					}
				}

			}
		}.runTaskAsynchronously(plugin);
		getLogger().log(Level.INFO, "Carregando chunks");
		for (int x = -500; x < 500; x++) {
			for (int z = -500; z < 500; z++) {
				Chunk chunk = Bukkit.getWorld("world").getBlockAt(x, 64, z).getChunk();
				if (chunk.isLoaded()) {
					continue;
				}
				chunk.load(true);
			}
		}
	
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		kit.antikits.clear();
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer("Server is restarting");
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!sqlcmd.expires.isEmpty()) {
			sqlcmd.expires.clear();
		}
		Connect.SQLdisconnect();
	}

	public void checkWinner() {
		if (winner != null)
			return;
		Iterator<UUID> iterator = gamers.iterator();
		while (iterator.hasNext()) {
			UUID uuid = iterator.next();
			if (PlayerListener.relog.contains(uuid))
				continue;
			if (getServer().getPlayer(uuid) == null)
				iterator.remove();
		}
		if (gamers.size() > 1)
			return;
		if (gamers.size() < 1) {
			getServer().shutdown();
			return;
		}
		getServer().getScheduler().cancelAllTasks();
		dano = false;
		final Player p = getServer().getPlayer(gamers.get(0));
		if (p == null) {
			getServer().shutdown();
			return;
		}
		winner = p.getUniqueId();

		if (!evento) {
			Status status = Status.getStatus(p);
			status.addWin(p);
		}

		stage = Estagio.WINNER;
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				giveWinnerItems(p);
			}
		}, 50);
	}

	@SuppressWarnings("deprecation")
	public void giveWinnerItems(final Player p) {
		Location l = p.getLocation();
		l.setY(120);
		Random r = new Random();
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0, -10 + r.nextInt(20)));
		new BukkitRunnable() {
			@Override
			public void run() {
				spawnRandomFirework(p.getLocation().add(-10, 0, -10));
				spawnRandomFirework(p.getLocation().add(-10, 0, 10));
				spawnRandomFirework(p.getLocation().add(10, 0, -10));
				spawnRandomFirework(p.getLocation().add(10, 0, 10));
				spawnRandomFirework(p.getLocation().add(-5, 0, -5));
				spawnRandomFirework(p.getLocation().add(-5, 0, 5));
				spawnRandomFirework(p.getLocation().add(5, 0, -5));
				spawnRandomFirework(p.getLocation().add(5, 0, 5));
				spawnRandomFirework(p.getLocation().add(-4, 0, -3));
				spawnRandomFirework(p.getLocation().add(-3, 0, 4));
				spawnRandomFirework(p.getLocation().add(2, 0, -6));
				spawnRandomFirework(p.getLocation().add(1, 0, 9));
			}
		}.runTaskTimer(this, 10, 30L);
		for (BO3Blocks bo3 : cake) {
			Block b = new Location(l.getWorld(), l.getX() + bo3.getX(), l.getY() + bo3.getY(), l.getZ() + bo3.getZ())
					.getBlock();
			b.setType(bo3.getType());
			b.setData(bo3.getData());
		}
		for (Player todos : Bukkit.getOnlinePlayers()) {
			p.showPlayer(todos);
		}
		p.teleport(l.add(0, 10, 0));
		p.getWorld().setTime(19000);
		p.getInventory().clear();
		p.setItemInHand(new ItemStack(Material.WATER_BUCKET));
		if (!evento) {
			try {
				sqlcmd.addPlayerBoxes(p.getUniqueId(), BoxType.OURO, 1);
			} catch (SQLException e) {
				return;
			}
		}
		getServer().broadcastMessage("§6" + p.getName() + " Venceu a partida!");
		getServer().broadcastMessage("§6Kills: §7" + getKills(p));
		getServer().broadcastMessage("§6Kit: §7" + kit.getPlayerKit(p));
		getServer().broadcastMessage("§6Tempo: §7" + getHourTime(GameTimer));
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				p.kickPlayer("§6Parabéns " + p.getName() + "\nVocê ganhou a partida!");
				for (Player player : getServer().getOnlinePlayers()) {
					player.kickPlayer("§6" + p.getName() + " venceu a partida!\n" + "§7Servidor reiniciando!");
				}
				getServer().shutdown();
			}
		}, 600);
	}

	public void spawnRandomFirework(Location loc) {
		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		Random r = new Random();
		int rt = r.nextInt(4) + 1;
		Type type = Type.BALL;
		if (rt == 1)
			type = Type.BALL;
		if (rt == 2)
			type = Type.BALL_LARGE;
		if (rt == 3)
			type = Type.BURST;
		if (rt == 4)
			type = Type.STAR;

		Color c1 = Color.WHITE;
		Color c2 = Color.YELLOW;
		Color c3 = Color.ORANGE;
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withColor(c2)
				.withFade(c3).with(type).trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}

	public String getTime(Integer i) {
		if (i.intValue() >= 60) {
			Integer time = Integer.valueOf(i.intValue() / 60);
			String add = "";
			if (time > 1) {
				add = "s";
			}
			return time + " minuto" + add;
		}
		Integer time = i;
		String add = "";
		if (time > 1) {
			add = "s";
		}
		return time + " segundo" + add;
	}

	@SuppressWarnings("deprecation")
	public void startGame() {
		World world = getServer().getWorld(config.getConfig(ConfigEnum.CONFIG).getString("world-name"));
		world.setTime(0);
		world.setGameRuleValue("doDaylightCycle", "true");
		world.setGameRuleValue("doMobSpawning", "true");
		world.setThundering(false);
		world.setWeatherDuration(1000000);
		abrirPortas();
		kitToSecond.clear();
		box.bronzeBoxes.clear();
		box.prataBoxes.clear();
		box.goldBoxes.clear();
		box.diamondBoxes.clear();
		if (!isFlying.isEmpty()) {
			isFlying.clear();
		}

		getServer().broadcastMessage("§cO jogo começou");
		// WorldBorder border = WorldBorder.getInstance(world);
		// border.changeSize(1000, 50, 50 * 60);
		getServer().getPluginManager().callEvent(new GameStartEvent());

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.setWalkSpeed(0.2f);
			p.setHealth(20.0);
			p.setFoodLevel(20);
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
			BarAPI.removeBar(p);
			if (adm.isSpectating(p)) {
				vanish.makeVanished(p);
				p.getInventory().clear();
				continue;
			}
			p.setAllowFlight(false);
			p.setFlying(false);
			gamers.add(p.getUniqueId());
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (adm.isSpectating(p)) {
						continue;
					}
					kit.giveItem(p);
					kit.giveItemBox(p);
					getServer().getPluginManager().callEvent(new PlayerStartGameEvent(p));
				}
			}
		}.runTaskAsynchronously(this);
		TotalPlayers = gamers.size();
		stage = Estagio.INVENCIBILITY;
	}

	private void prepareConfig() {
		FileConfiguration c = config.getConfig(ConfigEnum.CONFIG);
		bossBarMessage = c.getString("boss-bar");
		host = c.getString("sql-host");
		database = c.getString("sql-db");
		password = c.getString("sql-pass");
		user = c.getString("sql-user");
		ipshort = c.getString("ip-short");
		getLogger().info("Carregando Config!");
	}

	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				deleteDir(new File(dir, children[i]));
			}
		}
		dir.delete();
	}

	public void changeViewDistance(int distance) {
		((CraftServer) this.getServer()).getHandle().getServer().worlds.get(0).getPlayerChunkMap().a(distance);
	}

	public boolean isInsideSpawn(Player p) {
		return !((p.getLocation().getBlockX() > 20) || (p.getLocation().getBlockX() < -20)
				|| (p.getLocation().getBlockZ() > 20) || (p.getLocation().getBlockZ() < -20));
	}

	public void sendToSpawn(final Player p) {
		p.setFlying(false);
		Location spawn = p.getWorld().getSpawnLocation().clone();
		int chances = 0;
		if (p.isInsideVehicle())
			p.leaveVehicle();
		p.eject();
		if (isInsideSpawn(p))
			return;
		Random random = new Random();
		while (true) {
			Location hisSpawn = new Location(spawn.getWorld(), spawn.getX() + (random.nextInt(10 * 2) - 10), 124,
					spawn.getZ() + (random.nextInt(10 * 2) - 10));
			chances = chances + 1;
			while (hisSpawn.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				if (hisSpawn.getY() > 0)
					hisSpawn.add(0, -1, 0);
				else
					continue;
			}
			if (hisSpawn.getBlock().getType() == Material.AIR
					&& hisSpawn.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
				spawn = hisSpawn.clone();
				break;
			}
			if (chances == 300) {
				hisSpawn.setY(p.getWorld().getHighestBlockYAt(hisSpawn));
				spawn = hisSpawn.clone();
				break;
			}
		}
		final Location destination = spawn.add(0.5, 0.5, 0.5).clone();
		p.teleport(destination);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				p.teleport(destination);
			}
		});
	}

	public void dropItems(Player p, Location l) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack item : p.getPlayer().getInventory().getContents())
			if (item != null && item.getType() != Material.AIR)
				items.add(item.clone());
		for (ItemStack item : p.getPlayer().getInventory().getArmorContents())
			if (item != null && item.getType() != Material.AIR)
				items.add(item.clone());
		if (p.getPlayer().getItemOnCursor() != null && p.getPlayer().getItemOnCursor().getType() != Material.AIR)
			items.add(p.getPlayer().getItemOnCursor().clone());
		dropItems(p, items, l);
	}

	public void darDano(LivingEntity levarDano, Player darDano, double damage, boolean b) {
		if (levarDano == null || levarDano.isDead() || darDano == null || darDano.isDead()) {
			return;
		}
		EntityLiving p = ((CraftLivingEntity) levarDano).getHandle();
		levarDano.setNoDamageTicks(0);
		if (b) {
			if (p.getHealth() < damage) {
				levarDano.setHealth(1.0D);
				darDano.setMetadata("custom", new FixedMetadataValue(plugin, null));
				levarDano.damage(6.0D, darDano);
			} else {
				levarDano.damage(damage);
			}
		} else {
			darDano.setMetadata("custom", new FixedMetadataValue(plugin, null));
			levarDano.damage(damage, darDano);
		}
	}

	@SuppressWarnings("deprecation")
	public void dropItems(Player p, List<ItemStack> items, Location l) {
		if (stage != Estagio.PREGAME) {
			World world = l.getWorld();
			for (ItemStack item : items) {
				if (item == null || item.getType() == Material.AIR)
					continue;
				if (item.getType() == Material.POTION) {
					if (item.getDurability() == 8201) {
						continue;
					}
				}
				if (item.getType() == Material.SKULL_ITEM)
					continue;
				if (item.getType() == Material.STONE_BUTTON && item.getItemMeta() != null
						&& item.getItemMeta().hasDisplayName()
						&& item.getItemMeta().getDisplayName().contains("Detonador"))
					continue;
				if (item.getType() == Material.BOW && item.getItemMeta() != null && item.getItemMeta().hasDisplayName()
						&& item.getItemMeta().getDisplayName().contains("Archer"))
					continue;
				if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()
						&& item.getItemMeta().getDisplayName().contains("Blody Bane"))
					continue;
				if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()
						&& item.getItemMeta().getDisplayName().equals("§6Armadura"))
					continue;
				boolean isItemKit = false;
				for (ItemStack i : kit.getPlayerKitItems(p)) {
					if (item.getType() == i.getType()) {
						isItemKit = true;
						break;
					}
				}
				if (isItemKit) {
					continue;
				}
				if (item.hasItemMeta())
					world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
				else
					world.dropItemNaturally(l, item);
			}
			p.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
			p.getPlayer().getInventory().clear();
			p.getPlayer().setItemOnCursor(new ItemStack(0));
			for (PotionEffect pot : p.getActivePotionEffects()) {
				p.removePotionEffect(pot.getType());
				break;
			}
		}
	}

	public boolean isNotPlaying(Player p) {
		return !gamers.contains(p.getUniqueId());
	}

	public void removeGamer(Player p) {
		gamers.remove(p.getUniqueId());
	}

	public void spawnBordaPit() {
		for (int x = -50; x <= 50; x++) {
			if (x == -50 || x == 50) {
				for (int z = -50; z <= 50; z++) {
					for (int y = 0; y <= 250; y++) {
						Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
						if (!loc.getChunk().isLoaded())
							loc.getChunk().load();
						if (new Random().nextBoolean()) {
							loc.getBlock().setType(Material.BEDROCK);
						} else {
							loc.getBlock().setType(Material.BEDROCK);

						}

					}
				}
			}
		}
		for (int z = -50; z <= 50; z++) {
			if (z == -50 || z == 50) {
				for (int x = -50; x <= 50; x++) {
					for (int y = 0; y <= 250; y++) {
						Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
						if (!loc.getChunk().isLoaded())
							loc.getChunk().load();
						if (new Random().nextBoolean()) {
							loc.getBlock().setType(Material.BEDROCK);
						} else {
							loc.getBlock().setType(Material.BEDROCK);
						}
					}
				}
			}
		}
	}

	public void gerarBorda() {
		for (int x = -500; x <= 500; x++) {
			if (x == -500 || x == 500) {
				for (int z = -500; z <= 500; z++) {
					for (int y = 0; y <= 250; y++) {
						Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
						if (!loc.getChunk().isLoaded())
							loc.getChunk().load();
						// Block b = loc.getBlock();
						// forcefieldblock.add(b);
						if (new Random().nextBoolean()) {
							loc.getBlock().setType(Material.GLASS);
						} else {
							loc.getBlock().setType(Material.PUMPKIN);

						}

					}
				}
			}
		}
		for (int z = -500; z <= 500; z++) {
			if (z == -500 || z == 500) {
				for (int x = -500; x <= 500; x++) {
					for (int y = 0; y <= 250; y++) {
						Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
						if (!loc.getChunk().isLoaded())
							loc.getChunk().load();
						// Block b = loc.getBlock();
						// forcefieldblock.add(b);
						if (new Random().nextBoolean()) {
							loc.getBlock().setType(Material.GLASS);
						} else {
							loc.getBlock().setType(Material.PUMPKIN);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void generateColiseu() {
		Location l = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		l.setY(120);
		for (BO3Blocks bo3 : coliseu) {
			Block b = new Location(l.getWorld(), l.getX() + bo3.getX(), l.getY() + bo3.getY(), l.getZ() + bo3.getZ())
					.getBlock();
			b.setType(bo3.getType());
			b.setData(bo3.getData());
			if (b.getType() == Material.PISTON_BASE) {
				portaColiseu.add(b);
			} else {
				blocosColiseu.add(b);
			}
		}

	}

	public void abrirPortas() {
		for (Block b : portaColiseu) {
			if (b.getType() != null && b.getType() != Material.AIR)
				b.setType(Material.AIR);
		}
		portaColiseu.clear();
	}

	public String getHourTime(Integer i) {
		int minutes = i / 60;
		int seconds = i % 60;
		String disMinu = (minutes < 10 ? "" : "") + minutes;
		String disSec = (seconds < 10 ? "0" : "") + seconds;
		String formattedTime = disMinu + ":" + disSec;
		return formattedTime;
	}

	public int getKills(Player p) {
		if (playerKills.containsKey(p.getUniqueId())) {
			return playerKills.get(p.getUniqueId());
		}
		return 0;
	}

	public void addKill(Player p) {
		int kills = 0;
		if (playerKills.containsKey(p.getUniqueId()))
			kills = playerKills.get(p.getUniqueId());
		kills += 1;
		playerKills.put(p.getUniqueId(), kills);
	}

	public void removeKills(Player p) {
		playerKills.remove(p.getUniqueId());
	}

	public Location getRespawnLocation() {
		Random r = new Random();
		int x = 200 + r.nextInt(200);
		int z = 200 + r.nextInt(200);
		if (r.nextBoolean())
			x = -x;
		if (r.nextBoolean())
			z = -z;

		World world = getServer().getWorlds().get(0);
		int y = world.getHighestBlockYAt(x, z) + 3;
		Location loc = new Location(world, x, y, z);
		if (!loc.getChunk().isLoaded()) {
			loc.getChunk().load();
		}
		return loc;
	}

	public ArrayList<BO3Blocks> loadBO3(String path) {
		File file = new File(getDataFolder(), path + ".bo3");
		if (!file.exists()) {
			getLogger().log(Level.SEVERE, "Nao foi possivel encontrar o arquivo " + path + ".bo3");
			return new ArrayList<>();
		}
		ArrayList<BO3Blocks> blocks = new ArrayList<>();
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (!line.startsWith("Block"))
					continue;
				String[] bo3 = line.replace("Block(", "").replace(")", "").split(",");
				int x = Integer.valueOf(bo3[0]);
				int y = Integer.valueOf(bo3[1]);
				int z = Integer.valueOf(bo3[2]);
				String mat = bo3[3];
				byte data = (byte) 0;
				if (bo3[3].contains(":")) {
					String[] material = bo3[3].split(":");
					mat = material[0];
					data = Byte.valueOf(material[1]);
				}
				blocks.add(new BO3Blocks(x, y, z, Material.valueOf(mat), data));
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			getLogger().log(Level.INFO, "O arquivo " + path + ".bo3 foi carregado!");
		}
		return blocks;
	}

	public void MandarParaSpecs(String mensagem) {
		for (World w : Bukkit.getWorlds()) {
			for (Player mods : w.getPlayers()) {
				if (adm.isSpectating(mods) || perm.isTrial(mods)) {
					mods.sendMessage(mensagem);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void topKills(Objective o) {
		List<Player> competitive = new ArrayList<>();

		for (Player g : Bukkit.getOnlinePlayers()) {
			if (!isNotPlaying(g)) {
				competitive.add(g);
			}
		}

		if (competitive.size() == 0) {
			return;
		}

		int x = 0;

		ArrayList<UUID> cant = new ArrayList<>();
		ArrayList<Player> order = new ArrayList<>();

		while (x < 10) {
			Player winner = null;
			for (Player g : Bukkit.getOnlinePlayers()) {
				if (cant.contains(g.getUniqueId())) {
					continue;
				}
				if (!isNotPlaying(g)) {
					if (winner == null) {
						winner = g;
					} else {
						if (getKills(g) > getKills(winner)) {
							winner = g;
						}
					}
				}
			}
			if (winner != null) {
				cant.add(winner.getPlayer().getUniqueId());
				order.add(winner);
			}
			x++;
		}

		for (Player g : Bukkit.getOnlinePlayers()) {
			if (!isNotPlaying(g)) {
				o.getScore(g.getName()).setScore(getKills(g));
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void lookSomeone(Player player, Player target) {
		Scoreboard sb = player.getScoreboard();
		Objective ob = sb.getObjective("kills");
		if (ob != null) {
			if (ob.getDisplayName().replace("§7> " + "§6", "").equalsIgnoreCase(kit.getPlayerKit(target))) {
				return;
			} else {
				ob.unregister();
			}
		}
		ob = sb.registerNewObjective("kills", "dummy");
		ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
		String kitName = "§7> " + "§6" + kit.getPlayerKit(target);
		if (kitName.length() > 32)
			kitName = kitName.substring(0, 32);
		ob.setDisplayName(kitName);
		ob.getScore((OfflinePlayer) target).setScore(getKills(target));
		player.setScoreboard(sb);
	}

	public void notLookingAtSomeone(Player player) {
		Scoreboard sb = player.getScoreboard();
		Objective ob = sb.getObjective("kills");
		if (ob != null)
			ob.unregister();
		player.setScoreboard(sb);
	}

	public void applyScore(Player player) {
		Status playerstatuc = Status.getStatus(player);
		ScoreboardAPI board = new ScoreboardAPI(player, " §f§lHARDCORE GAMES ", false, "§b§l", "§f§l§n") {
			@SuppressWarnings("deprecation")
			public void update() {
				if (stage == Estagio.PREGAME) {
					String players = Bukkit.getOnlinePlayers().length - adm.admin.size() + "/" + Bukkit.getMaxPlayers();
					setText("     §7hg.likekits.com.br", 15);
					setText("§0  ", 14);
					setText("     §6§l§nINFORMAÇOES", 13);
					setText("§9  ", 12);
					setText("  Jogadores: §e" + players + "   ", 11);
					setText("  Inicia em: §e" + getHourTime(PreGameTimer), 10);
					setText("  XP: §e" + playerstatuc.getXp(), 9);
					setText("§0  ", 8);
					setText("  Kit 1: §e" + kit.getPlayerKit1noSurprise(player) + "      ", 7);
					setText("  Kit 2: §e" + kit.getPlayerKit2NoSurprise(player) + "      ", 6);
					setText("§9  ", 5);
					setText("  AntiKit: §e" + kit.getPlayerAntiKit(player), 4);
					setText("§0  ", 3);
					setText("  IP: §e" + ipshort, 2);
					setText("     §7www.likekits.com.br", 1);
					// setText(new String[] { " §7hg.likekits.com.br", "§0 ", "
					// §6§l§nINFORMAÇOES", "§9 ",
					// " Jogadores: §e" + players, " Inicia em: §e" +
					// getHourTime(PreGameTimer), " " });
				} else if (stage == Estagio.INVENCIBILITY) {
					String players = "" + gamers.size() + "";
					setText("     §7hg.likekits.com.br", 15);
					setText("§0  ", 14);
					setText("     §6§l§nINFORMAÇOES", 13);
					setText("§9  ", 12);
					setText("  Jogadores: §e" + players + "   ", 11);
					setText("  Acaba em: §e" + getHourTime(Invenci), 10);
					setText("  XP: §e" + +playerstatuc.getXp(), 9);
					setText("§0  ", 8);
					setText("  Kit 1: §e" + kit.getPlayerKit1noSurprise(player) + "      ", 7);
					setText("  Kit 2: §e" + kit.getPlayerKit2NoSurprise(player) + "      ", 6);
					setText("§9  ", 5);
					setText("  AntiKit: §e" + kit.getPlayerAntiKit(player), 4);
					setText("§0  ", 3);
					setText("  IP: §e" + ipshort, 2);
					setText("     §7www.likekits.com.br", 1);
				}
			}

		};

		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				if (Bukkit.getOnlinePlayers().length >= 1 && stage != Estagio.GAMETIME) {
					board.update();
				} else {
					// scoreEnable = false;
					board.removeScoreboard();
					cancel();
					// Bukkit.getConsoleSender().sendMessage("Scoreboard
					// pausada");
				}
			}
		}.runTaskTimer(plugin, 0L, 0L);
	}

	private void registrarComandos() {
		Main.plugin.getCommand("tempo").setExecutor(new TempoCMD(this));
		Main.plugin.getCommand("tp").setExecutor(new TpCMD(this));
		Main.plugin.getCommand("teleport").setExecutor(new TpCMD(this));
		Main.plugin.getCommand("tpall").setExecutor(new TpCMD(this));
		Main.plugin.getCommand("tphere").setExecutor(new TpCMD(this));
		Main.plugin.getCommand("gamemode").setExecutor(new GamemodeCMD());
		Main.plugin.getCommand("gm").setExecutor(new GamemodeCMD());
		Main.plugin.getCommand("admin").setExecutor(new AdminCMD(this));
		Main.plugin.getCommand("desisto").setExecutor(new AdminCMD(this));
		Main.plugin.getCommand("kit").setExecutor(new KitCMD(this));
		Main.plugin.getCommand("start").setExecutor(new StartCMD(this));
		Main.plugin.getCommand("help").setExecutor(new HelpCMD(this));
		Main.plugin.getCommand("info").setExecutor(new HelpCMD(this));
		Main.plugin.getCommand("forcekit").setExecutor(new ForcekitCMD(this));
		Main.plugin.getCommand("second").setExecutor(new SecondkitCMD(this));
		Main.plugin.getCommand("givebox").setExecutor(new BoxCMD(this));
		Main.plugin.getCommand("specs").setExecutor(new SpecsCMD(this));
		Main.plugin.getCommand("ffeast").setExecutor(new FfeastCMD(this));
		Main.plugin.getCommand("feast").setExecutor(new FeastCMD(this));
		Main.plugin.getCommand("chat").setExecutor(new ChatCMD(this));
		Main.plugin.getCommand("build").setExecutor(new BuildCMD(this));
		Main.plugin.getCommand("dano").setExecutor(new DanoCMD(this));
		Main.plugin.getCommand("evento").setExecutor(new DanoCMD(this));
		Main.plugin.getCommand("pvp").setExecutor(new DanoCMD(this));
		Main.plugin.getCommand("toggledrops").setExecutor(new DropsCMD(this));
		Main.plugin.getCommand("invsee").setExecutor(new InvseeCMD(this));
		Main.plugin.getCommand("skit").setExecutor(new SkitCMD(this));
		Main.plugin.getCommand("macrotest").setExecutor(new MacrotestCMD(this));
		Main.plugin.getCommand("pit").setExecutor(new PitCMD(this));
		Main.plugin.getCommand("revive").setExecutor(new ReviveCMD(this));
		Main.plugin.getCommand("createarena").setExecutor(new CreatearenaCMD(this));
		Main.plugin.getCommand("wand").setExecutor(new CreatearenaCMD(this));
		Main.plugin.getCommand("set").setExecutor(new CreatearenaCMD(this));
		Main.plugin.getCommand("fmini").setExecutor(new MinifeastCMD(this));
		Main.plugin.getCommand("fly").setExecutor(new FlyCMD(this));
		Main.plugin.getCommand("status").setExecutor(new StatusCMD(this));
		Main.plugin.getCommand("top10").setExecutor(new StatusCMD(this));
		Main.plugin.getCommand("tell").setExecutor(new TellCMD(this));
		Main.plugin.getCommand("msg").setExecutor(new TellCMD(this));
		Main.plugin.getCommand("w").setExecutor(new TellCMD(this));
		Main.plugin.getCommand("r").setExecutor(new TellCMD(this));
		Main.plugin.getCommand("ignore").setExecutor(new TellCMD(this));
		Main.plugin.getCommand("spawn").setExecutor(new SpawnCMD(this));
	}
}
