package me.ghost.hg.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.ghost.hg.Main;
import net.minecraft.util.com.google.common.base.Splitter;
import net.minecraft.util.com.google.common.collect.Maps;

public abstract class ScoreboardAPI {

	Player player;
	String display;
	Scoreboard scoreboard;
	Objective objective;
	Map<Player, ScoreboardAPI> scoreboards = Maps.newHashMap();

	public static void removerScoreboard(Player p) {
		p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
	}

	public ScoreboardAPI(Player player, String display, boolean scroller, String scrollerColor1,
			String scrollerColor2) {
		this.player = player;
		this.display = display;
		scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		objective = scoreboard.registerNewObjective("s" + player.getName().toUpperCase(), "dummy");
		if (scroller == true) {
			new BukkitRunnable() {
				int c = 0;

				public void run() {
					if (c + 1 < display.length()) {
						String msg = scrollerColor1 + display.substring(0, c) + scrollerColor2
								+ display.substring(c, c + 1) + scrollerColor1 + display.substring(c + 1);
						c += 1;
						objective.setDisplayName(msg);
					} else {
						c = 0;
					}
				}
			}.runTaskTimer(Main.plugin, 0, 1);
		} else {
			objective.setDisplayName(display);
		}
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		scoreboards.put(player, this);
		player.setScoreboard(scoreboard);
	}

	public Objective getObjective() {
		return objective;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public Player getPlayer() {
		return player;
	}

	public Map<Player, ScoreboardAPI> getScoreboards() {
		return scoreboards;
	}

	public void setDisplayName(String display) {
		getObjective().setDisplayName(display);
	}

	public ScoreboardAPI setText(String[] strings) {
		List<String> list = Arrays.asList(strings);
		Collections.reverse(list);
		for (int i = 0; i < list.size(); i++)
			setText(list.get(i), i);
		return this;
	}

	public ScoreboardAPI setText(String string, int score) {
		if (score > 15)
			throw new Error("Os scores sao maiores que 15.");
		if (string.length() > 32)
			throw new Error("O texto da linha " + score + " e maior que 32 chars.");

		Iterator<String> iterator = Splitter.fixedLength(16).split(string).iterator();
		String name = getTeamName(score);
		String player = getScoreName(score);

		if (getScoreboard().getTeam(name) == null) {
			Team team = getScoreboard().registerNewTeam(name);
			if (!team.hasEntry(player)) {
				team.addEntry(player);
				team.setPrefix(iterator.next());
				if (string.length() > 16)
					team.setSuffix(ChatColor.getLastColors(string) + iterator.next());
			}
			return this;
		}
		getObjective().getScore(player).setScore(score);
		Team team = getScoreboard().getTeam(name);
		team.setPrefix(iterator.next());
		if (string.length() > 16)
			team.setSuffix(ChatColor.getLastColors(string) + iterator.next());

		return this;
	}

	public abstract void update();

	public void removeText(int score) {
		String name = getTeamName(score);
		String player = getScoreName(score);
		getScoreboard().resetScores(player);
		if (getScoreboard().getTeam(name) != null)
			getScoreboard().getTeam(name).unregister();
	}

	public void removeScoreboard() {
		if (!getScoreboards().containsKey(getPlayer()))
			throw new Error("O jogador nao possui scoreboard associada.");
		ScoreboardAPI scoreboard = getScoreboards().get(getPlayer());
		scoreboard.resetTeams();
		scoreboard.resetObjectives();
		getScoreboards().remove(getPlayer());
	}

	public String getTeamName(int score) {
		return "[Team-" + score;
	}

	private String getScoreName(int i) {
		ChatColor color = ChatColor.values()[i];
		return String.format("%s" + color.getChar() + ChatColor.RESET, ChatColor.COLOR_CHAR);
	}

	public void resetTeams() {
		for (Team team : getScoreboard().getTeams())
			team.unregister();
	}

	public void resetObjectives() {
		for (Objective objective : getScoreboard().getObjectives())
			objective.unregister();
	}

}