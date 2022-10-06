package me.ghost.hg.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ghost.hg.Main;

public class Vanish {
	private static HashMap<UUID, VLevel> vanished = new HashMap<>();
	private Main m;
	private List<UUID> spectatorsDisabled = new ArrayList<>();

	public Vanish(Main main) {
		this.m = main;
	}

	public void setSpectatorEnabled(Player p, boolean b) {
		if (!b) {
			if (!spectatorsDisabled.contains(p.getUniqueId()))
				spectatorsDisabled.add(p.getUniqueId());
		} else {
			spectatorsDisabled.remove(p.getUniqueId());
		}
	}

	public void makeVanished(Player p) {
		if (m.perm.isDono(p)) {
			makeVanished(p, VLevel.DONO);
		} else if (m.perm.isAdmin(p)) {
			makeVanished(p, VLevel.ADMIN);
		} else if (m.perm.isMod(p)) {
			makeVanished(p, VLevel.MOD);
		} else if (m.perm.isTrial(p)) {
			makeVanished(p, VLevel.TRIAL);
		} else if (m.perm.isYoutuber(p)) {
			makeVanished(p, VLevel.YOUTUBER);
		} else if (m.perm.isPro(p)) {
			makeVanished(p, VLevel.PRO);
		}
	}

	@SuppressWarnings("deprecation")
	public void makeVanished(Player p, VLevel level) {
		if (level.equals(VLevel.PRO)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.perm.isPro(player)) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}

				if (!m.isNotPlaying(player)) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}

				if (spectatorsDisabled.contains(player.getUniqueId())) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				if (!player.canSee(p))
					player.showPlayer(p);
			}
		} else if (level.equals(VLevel.YOUTUBER)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.perm.isYoutuber(player)) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}

				if (!m.isNotPlaying(player)) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}

				if (spectatorsDisabled.contains(player.getUniqueId())) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				if (!player.canSee(p))
					player.showPlayer(p);
			}
		} else if (level.equals(VLevel.TRIAL)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.perm.isTrial(player)) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				/*
				 * if (!m.isNotPlaying(player)) { if (!player.canSee(p))
				 * continue; player.hidePlayer(p); continue; }
				 */
				if (spectatorsDisabled.contains(player.getUniqueId())) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				if (!player.canSee(p))
					player.showPlayer(p);
			}
		} else if (level.equals(VLevel.MOD)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.perm.isMod(player)) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				/*
				 * if (!m.isNotPlaying(player)) { if (!player.canSee(p))
				 * continue; player.hidePlayer(p); continue; }
				 */
				if (spectatorsDisabled.contains(player.getUniqueId())) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				if (!player.canSee(p))
					player.showPlayer(p);
			}
		} else if (level.equals(VLevel.ADMIN)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.perm.isAdmin(player)) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				/*
				 * if (!m.isNotPlaying(player)) { if (!player.canSee(p))
				 * continue; player.hidePlayer(p); continue; }
				 */
				if (spectatorsDisabled.contains(player.getUniqueId())) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				if (!player.canSee(p))
					player.showPlayer(p);
			}
		} else if (level.equals(VLevel.DONO)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.perm.isDono(player)) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				/*
				 * if (!m.isNotPlaying(player)) { if (!player.canSee(p))
				 * continue; player.hidePlayer(p); continue; }
				 */
				if (spectatorsDisabled.contains(player.getUniqueId())) {
					if (!player.canSee(p))
						continue;
					player.hidePlayer(p);
					continue;
				}
				if (!player.canSee(p))
					player.showPlayer(p);
			}
		}
		vanished.put(p.getUniqueId(), level);
	}

	public boolean isVanished(Player p) {
		return vanished.containsKey(p.getUniqueId()) && !vanished.get(p.getUniqueId()).equals(VLevel.PLAYER);
	}

	public VLevel getPlayerLevel(Player p) {
		return vanished.get(p.getUniqueId());
	}

	@SuppressWarnings("deprecation")
	public void updateVanished() {
		for (Player p : Bukkit.getOnlinePlayers())
			if (isVanished(p)) {
				makeVanished(p, vanished.get(p.getUniqueId()));
			} else {
				makeVisible(p);
			}
	}

	@SuppressWarnings("deprecation")
	public void makeVisible(Player p) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.canSee(p))
				continue;
			player.showPlayer(p);
		}
		vanished.put(p.getUniqueId(), VLevel.PLAYER);
	}
}
