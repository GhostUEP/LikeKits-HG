package me.ghost.hg.admin;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;

public class Mode {
	public ArrayList<Player> admin = new ArrayList<Player>();
	public ArrayList<Player> youtuber = new ArrayList<Player>();
	public ArrayList<Player> pro = new ArrayList<Player>();
	private Main m;
	public ItemStack chest;
	public ItemStack watch;

	public Mode(Main m) {
		this.m = m;
	}

	public void setAdmin(Player p) {
		if (!admin.contains(p)) {
			admin.add(p);
		}
		p.setGameMode(GameMode.CREATIVE);
		m.vanish.makeVanished(p);
		m.vanish.updateVanished();
		p.sendMessage("§5Modo Admin ATIVADO");
		p.sendMessage("§7Voce esta invisivel para " + getInvisible(p) + " e abaixo!");
	}

	public void setYoutuber(Player p) {
		if (m.perm.isPro(p)) {
			if (!pro.contains(p) || pro.isEmpty())
				pro.add(p);
		} else if (m.perm.isYoutuber(p)) {
			if (!youtuber.contains(p) || youtuber.isEmpty())
				youtuber.add(p);
		}
		// 1.8 p.setGameMode(GameMode.SPECTATOR);
		if (!m.isNotPlaying(p))
			m.removeGamer(p);
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(true);
		p.setFlying(true);
		m.vanish.makeVanished(p);
		m.vanish.updateVanished();
		p.sendMessage("§5Modo Espectador ATIVADO");
		p.sendMessage("§7Voce esta invisivel para " + getInvisible(p) + " e abaixo!");
	}

	public void setPlayer(Player p) {
		if (admin.contains(p)) {
			p.sendMessage("§cModo Admin DESATIVADO");
			p.sendMessage("§7Voce esta visivel para todos os players");
			admin.remove(p);
		}
		if (youtuber.contains(p) || pro.contains(p)) {
			p.sendMessage("§cModo Espectador DESATIVADO");
			p.sendMessage("§7Voce esta visivel para todos os players");
			pro.remove(p);
			youtuber.remove(p);
		}
		p.setGameMode(GameMode.SURVIVAL);
		m.vanish.makeVisible(p);
		m.vanish.updateVanished();
		p.setAllowFlight(false);
		p.setFlying(false);

	}

	public boolean isAdmin(Player p) {
		return admin.contains(p);
	}

	public boolean isSpectating(Player p) {
		return admin.contains(p) || youtuber.contains(p) || pro.contains(p);
	}

	public boolean isYTUT(Player p) {
		return youtuber.contains(p) || pro.contains(p);
	}

	private String getInvisible(Player p) {
		if (m.vanish.getPlayerLevel(p) == VLevel.DONO)
			return VLevel.ADMIN.name();
		if (m.vanish.getPlayerLevel(p) == VLevel.ADMIN)
			return VLevel.MOD.name();
		if (m.vanish.getPlayerLevel(p) == VLevel.MOD)
			return VLevel.TRIAL.name();
		if (m.vanish.getPlayerLevel(p) == VLevel.TRIAL)
			return VLevel.YOUTUBER.name();
		if (m.vanish.getPlayerLevel(p) == VLevel.YOUTUBER)
			return VLevel.PRO.name();
		return VLevel.PLAYER.name();
	}
}