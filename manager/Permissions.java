package me.ghost.hg.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.ghost.hg.Main;
import me.ghost.permissions.Enum.Group;

public class Permissions {
	public Main m;
	public HashMap<UUID, List<String>> playerKits = new HashMap<>();

	public Permissions(Main m) {
		this.m = m;
	}

	public boolean isMvp(Player p) {
		return isPro(p) || me.ghost.permissions.PermissionsManager.hasGroupPermission(p, Group.MVP);
	}

	public boolean isPro(Player p) {
		return isYoutuber(p) || me.ghost.permissions.PermissionsManager.hasGroupPermission(p, Group.PRO);
	}

	public boolean isYoutuber(Player p) {
		return isTrial(p) || me.ghost.permissions.PermissionsManager.hasGroupPermission(p, Group.YT);
	}

	public boolean isTrial(Player p) {
		return isMod(p) || me.ghost.permissions.PermissionsManager.hasGroupPermission(p, Group.TRIAL);
	}

	public boolean isMod(Player p) {
		return isAdmin(p) || me.ghost.permissions.PermissionsManager.hasGroupPermission(p, Group.MOD)
				|| me.ghost.permissions.PermissionsManager.hasGroupPermission(p, Group.YTPLUS);
	}

	public boolean isAdmin(Player p) {
		return isDono(p) || me.ghost.permissions.PermissionsManager.hasGroupPermission(p, Group.ADMIN);
	}

	public boolean isDono(Player p) {
		return me.ghost.permissions.PermissionsManager.isGroup(p, Group.DONO) || p.isOp();
	}

	public boolean isVips(Player p) {
		return me.ghost.permissions.PermissionsManager.isGroup(p, Group.PRO)
				|| me.ghost.permissions.PermissionsManager.isGroup(p, Group.YT)
				|| me.ghost.permissions.PermissionsManager.isGroup(p, Group.MVP);
	}

	public void loadPlayerKits(UUID uuid) {
		if (me.ghost.permissions.PermissionsManager.playerPerms.containsKey(uuid)) {
			List<String> seuskits = new ArrayList<String>();
			for (String kits : me.ghost.permissions.PermissionsManager.playerPerms.get(uuid)) {
				if (kits.toLowerCase().contains("kit.")) {
					seuskits.add(kits.toLowerCase());
				}
			}
			playerKits.put(uuid, seuskits);
			return;
		}
	}

}
