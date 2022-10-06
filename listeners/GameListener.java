package me.ghost.hg.listeners;

import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.ghost.hg.Main;
import me.ghost.hg.barapi.BarAPI;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.events.InvencibilityEndEvent;
import me.ghost.hg.events.TimeSecondEvent;
import me.ghost.hg.events.UpdateEvent;
import me.ghost.hg.events.UpdateEvent.UpdateType;
import me.ghost.hg.utils.Feast;
import me.ghost.hg.utils.Minifeast;
import me.ghost.hg.utils.Pit;

public class GameListener implements Listener {
	public Main main;
	public static Feast feast;

	public GameListener(Main m) {
		main = m;
		feast = new Feast(main, 100, 25, true);
	}

	private void killPlayer(Player p) {
		String playerName = "§6" + p.getName() + "(" + main.kit.getPlayerKit(p) + ")";
		String messageDeath = playerName + "§7" + " morreu por que alguém tinha mais kills";
		main.removeGamer(p);
		if (!main.perm.isPro(p)) {
			if (main.gamers.size() + 1 <= 10) {
				p.kickPlayer("§6Você conseguiu a posição " + (main.gamers.size() + 1) + " de " + main.TotalPlayers
						+ ". parabéns! \n" + messageDeath);
				main.listener.deathMessage.put(p.getUniqueId(), "Voce morreu! " + messageDeath);
				main.dropItems(p, Arrays.asList(p.getInventory().getContents()), p.getLocation());
				main.kit.FIRSTKITS.remove(p.getUniqueId());
				main.kit.SECONDKITS.remove(p.getUniqueId());
				main.kit.ANTIKIT.remove(p.getUniqueId());
				return;
			}
			p.kickPlayer("Voce perdeu! " + messageDeath);
			main.listener.deathMessage.put(p.getUniqueId(), "Voce morreu! " + messageDeath);
		} else if (main.perm.isTrial(p)) {
			main.listener.deathMessage.put(p.getUniqueId(), "Voce morreu! " + messageDeath);
			main.adm.setAdmin(p);
		} else {
			main.listener.deathMessage.put(p.getUniqueId(), "Voce morreu! " + messageDeath);
			main.adm.setYoutuber(p);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		main.getServer().getPluginManager().callEvent(new TimeSecondEvent());
		switch (main.stage) {
		case GAMETIME:
			main.GameTimer++;
			if (main.GameTimer >= 60 * 60) {
				Player p = null;
				int kills = 0;
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (main.isNotPlaying(player))
						continue;
					if (main.getKills(player) > kills) {
						if (p != null) {
							killPlayer(p);
						}
						kills = main.getKills(player);
						p = player;
					} else {
						killPlayer(player);
					}
				}
				main.checkWinner();
			}
			if (main.GameTimer >= 240) {
				if (!main.blocosColiseu.isEmpty()) {
					main.getServer()
							.broadcastMessage("§6Estamos removendo o coliseu, isso pode causar algum lag momentâneo");
					for (Block b : main.blocosColiseu) {
						if (b.getType() != null && b.getType() != Material.AIR)
							b.setType(Material.AIR);
					}
					main.blocosColiseu.clear();
				}
			}
			if (main.GameTimer == (60 * 12) + 30) {
				feast.generateFeast();
				clerLag();
			}
			if (feast.spawned && feast.time >= 0) {
				if (feast.time > 0) {
					if (feast.time % 60 == 0) {
						main.getServer()
								.broadcastMessage("§6O Feast vai spawnar " + "[§7" + feast.central.getX() + "§6, §7"
										+ feast.central.getY() + "§6, §7" + feast.central.getZ() + "§6]" + " em §6"
										+ main.getTime(feast.time) + "§7.\n§6Use /feast para apontar a bússola.");
					} else if (feast.time == 45 || feast.time == 30 || feast.time == 15 || feast.time == 10
							|| feast.time <= 5) {
						main.getServer()
								.broadcastMessage("§6O Feast vai spawnar " + "[§7" + feast.central.getX() + "§6, §7"
										+ feast.central.getY() + "§6, §7" + feast.central.getZ() + "§6]" + " em §6"
										+ main.getTime(feast.time) + "§7.\n§6Use /feast para apontar a bússola.");
					}
					feast.time--;
				} else if (feast.time == 0) {
					feast.generateChests();
					main.getServer()
							.broadcastMessage("§6O Feast spawnou" + " [§7" + feast.central.getX() + ", "
									+ feast.central.getY() + ", " + feast.central.getZ()
									+ "§6]\n§6Use /feast para apontar a bússola.");
					feast.time = -1;
				}
			}
			if (main.GameTimer == (60 * 45)) {
				main.getServer().broadcastMessage(
						"§cDaqui 5 minutos todos serão teleportados para a arena final, e terão de lutar até a morte");
			}
			if (main.GameTimer == (60 * 50)) {
				if (!Pit.spawned) {
					Main.pit2.spawnPit();
				}
			}
			if (main.GameTimer % 240 == 0) {
				new Minifeast(main);
			}
			break;
		case INVENCIBILITY:
			if (main.Invenci > 0) {
				if (main.Invenci % 60 == 0) {
					main.getServer().broadcastMessage("§cInvencibilidade acaba em §7" + main.getTime(main.Invenci));
				} else if (main.Invenci == 45 || main.Invenci == 30 || main.Invenci == 15 || main.Invenci == 10
						|| main.Invenci <= 5) {
					main.getServer().broadcastMessage("§cInvencibilidade acaba em §7" + main.getTime(main.Invenci));
				}
				main.Invenci--;
				main.GameTimer++;
			} else {
				main.getServer().getPluginManager().callEvent(new InvencibilityEndEvent());
				main.stage = Estagio.GAMETIME;
				main.getServer().broadcastMessage("§cA invencibilidade acabou!");
			}
			break;
		case PREGAME:
			if (main.PreGameTimer > 0) {
				BarAPI.setMessage(main.bossBarMessage.replace("&", "§"));
				int size = main.getServer().getOnlinePlayers().length - main.adm.admin.size();
				if (size < 5 && main.PreGameTimer > 30) {
					return;
				}

				if (main.PreGameTimer % 60 == 0) {
					main.getServer().broadcastMessage("§6A partida inicia em §7" + main.getTime(main.PreGameTimer));
				} else if (main.PreGameTimer == 45 || main.PreGameTimer == 30 || main.PreGameTimer == 15
						|| main.PreGameTimer == 10 || main.PreGameTimer <= 5) {
					main.getServer().broadcastMessage("§6A partida inicia em §7" + main.getTime(main.PreGameTimer));
				}
				if (main.PreGameTimer <= 15) {
					if (!main.isFlying.isEmpty()) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (main.isFlying.contains(p.getUniqueId())) {
								Location l2 = new Location(p.getWorld(), 0, 125, 0);
								p.teleport(l2);
								p.sendMessage(
										"§cVoce estava voando e por isso foi teleportado para o spawn e parou de voar");
								main.isFlying.remove(p.getUniqueId());
								p.setFlying(false);
								p.setAllowFlight(false);
							}
						}
					}
				}
				main.PreGameTimer--;
			} else {
				int size = main.getServer().getOnlinePlayers().length - main.adm.admin.size();
				if (size > 0) {
					main.startGame();
				} else {
					main.PreGameTimer = 120;
				}
			}
			break;
		default:
			break;
		}
	}

	public void clerLag() {
		for (Iterator<World> iterator1 = Bukkit.getWorlds().iterator(); iterator1.hasNext();) {
			World world = (World) iterator1.next();
			for (Iterator<Entity> iterator2 = world.getEntities().iterator(); iterator2.hasNext();) {
				Entity e = (Entity) iterator2.next();
				if (!(e instanceof Player)) {
					e.remove();
				}
			}
		}
	}
}
