package me.ghost.hg.constructors;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ghost.hg.Main;

public class Status {
	private String name;
	private UUID uuid;
	private Integer kills = Integer.valueOf(0);
	private Integer deaths = Integer.valueOf(0);
	private Integer wins = Integer.valueOf(0);
	private Integer xp = Integer.valueOf(0);

	public Status(String name, Integer kills, Integer deaths, Integer wins, Integer xp) {
		this.name = name;
		this.kills = kills;
		this.deaths = deaths;
		this.wins = wins;
		this.xp = xp;
	}

	public Status(String name, UUID uuid, Integer kills, Integer deaths, Integer wins, Integer xp) {
		this.name = name;
		this.uuid = uuid;
		this.kills = kills;
		this.deaths = deaths;
		this.wins = wins;
		this.xp = xp;
	}

	public String getname() {
		return this.name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public UUID getuuid() {
		return this.uuid == null ? UUID.randomUUID() : this.uuid;
	}

	public void setuuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Integer getKills() {
		return this.kills;
	}

	public Integer getXp() {
		return this.xp;
	}

	public Integer getTopStatus(Order ordem) {
		if (ordem == Order.WINS) {
			return this.wins;
		}
		if (ordem == Order.KILLS) {
			return this.kills;
		}
		if (ordem == Order.DEATHS) {
			return this.deaths;
		}
		if (ordem == Order.XP) {
			return this.xp;
		}
		return Integer.valueOf(0);
	}

	public void setXp(Integer xp) {
		this.xp = xp;
	}

	public void setKills(Integer kills) {
		this.kills = kills;
	}

	public Integer getDeaths() {
		return this.deaths;
	}

	public void setDeaths(Integer deaths) {
		this.deaths = deaths;
	}

	public Integer getWins() {
		return this.wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Double getKD() {
		return Double.valueOf(this.kills.doubleValue() / this.deaths.doubleValue());
	}

	public String getKDString() {
		return this.deaths.intValue() == 0 ? "inf"
				: this.kills.intValue() == 0 ? "1"
						: String.format("%.2f",
								new Object[] { Double.valueOf(1.0D * this.kills.intValue() / this.deaths.intValue()) });
	}

	public String toString() {
		return "Status{name='" + this.name + '\'' + ", uuid=" + this.uuid.toString() + ", kills="
				+ this.kills.toString() + ", deaths=" + this.deaths.toString() + ", wins=" + this.wins.toString() + '}';
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (getClass() != o.getClass()))
			return false;
		Status status = (Status) o;
		if (this.deaths != null ? !this.deaths.equals(status.deaths) : status.deaths != null)
			return false;
		if (this.kills != null ? !this.kills.equals(status.kills) : status.kills != null)
			return false;
		if (this.name != null ? !this.name.equals(status.name) : status.name != null)
			return false;
		if (this.wins != null ? !this.wins.equals(status.wins) : status.wins != null)
			return false;
		return this.uuid != null ? this.uuid.equals(status.uuid) : status.uuid == null;
	}

	public int hashCode() {
		int result = this.name != null ? this.name.hashCode() : 0;
		result = 31 * result + (this.uuid != null ? this.uuid.hashCode() : 0);
		result = 31 * result + (this.kills != null ? this.kills.hashCode() : 0);
		result = 31 * result + (this.deaths != null ? this.deaths.hashCode() : 0);
		result = 31 * result + (this.wins != null ? this.wins.hashCode() : 0);
		return result;
	}

	public static boolean isConnected() {
		try {
			return (Main.con != null) && (!Main.con.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void inicialCommands() {
		try {
			if (!isConnected()) {
				return;
			}
			PreparedStatement ps = Main.con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `status` ( `uuid` VARCHAR(255) NOT NULL, `name` VARCHAR(255) NOT NULL, `kills` INT NULL, `deaths` INT NULL, `wins` INT NULL, PRIMARY KEY (`uuid`), INDEX `name` (`name`) ) COLLATE='utf8_general_ci'");
			ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertStatus(Player p) throws SQLException {
		PreparedStatement ps = Main.con
				.prepareStatement("SELECT * from `status` WHERE uuid='" + p.getUniqueId().toString() + "'");
		ResultSet rs = ps.executeQuery();
		try {
			if (!rs.next()) {
				ps.execute("INSERT INTO `status` (uuid, name, kills, deaths, wins, xp) VALUES ('"
						+ p.getUniqueId().toString() + "', '" + p.getName() + "', 0, 0, 0, 0)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException sqlex) {
					sqlex.printStackTrace();
				}
		}
	}

	public void addDeath(Player p) {
		addDeaths(p, Integer.valueOf(1));
	}

	public void addDeaths(final Player p, final Integer deaths) {
		new BukkitRunnable() {
			public void run() {
				try {
					Statement stm = Main.con.createStatement();
					stm.execute("UPDATE `status` SET deaths=deaths+" + deaths + " WHERE uuid='"
							+ p.getUniqueId().toString() + "'");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.plugin);
	}

	public void addKill(Player p) {
		addKills(p, Integer.valueOf(1));
	}

	public void addKills(final Player p, final Integer kills) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					Statement stm = Main.con.createStatement();
					stm.execute("UPDATE `status` SET kills=kills+" + kills + " WHERE uuid='"
							+ p.getUniqueId().toString() + "'");
					stm.execute("UPDATE `status` SET xp=xp+" + 3 * kills + " WHERE uuid='" + p.getUniqueId().toString()
							+ "'");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.plugin);
	}

	public void addXP(final Player p, final Integer xp) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					Statement stm = Main.con.createStatement();
					stm.execute("UPDATE `status` SET xp=xp+" + xp + " WHERE uuid='" + p.getUniqueId().toString() + "'");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.plugin);
	}

	public void addWin(Player p) {
		addWins(p, Integer.valueOf(1));
	}

	public void addWins(final Player p, final Integer wins) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					Statement stm = Main.con.createStatement();
					stm.execute("UPDATE `status` SET wins=wins+" + wins + " WHERE uuid='" + p.getUniqueId().toString()
							+ "'");
					stm.execute("UPDATE `status` SET xp=xp+" + 20 * wins + " WHERE uuid='" + p.getUniqueId().toString()
							+ "'");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.plugin);
	}

	public static Integer getRank(Player p) {
		try {
			Statement stm = Main.con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT COUNT(*) from `status` WHERE kills > " + getStatus(p).getKills());
			rs.next();
			return Integer.valueOf(rs.getInt(1) + 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Integer.valueOf(999);
	}

	public static ArrayList<Status> getTopStatus() {
		return getTopStatus(Integer.valueOf(10), Order.KILLS);
	}

	public static ArrayList<Status> getTopStatus(Integer limit, Order ordem) {
		ArrayList<Status> entryStats = new ArrayList<>();
		try {
			Statement stm = Main.con.createStatement();
			ResultSet rs = stm.executeQuery(
					"SELECT * from `status` ORDER BY " + ordem.toString().toLowerCase() + " DESC LIMIT " + limit);
			while (rs.next()) {
				entryStats.add(new Status(rs.getString("name"), UUID.fromString(rs.getString("uuid")),
						Integer.valueOf(rs.getInt("kills")), Integer.valueOf(rs.getInt("deaths")),
						Integer.valueOf(rs.getInt("wins")), Integer.valueOf(rs.getInt("xp"))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entryStats;
	}

	public static Status getStatus(Player p) {
		return getStatus(p.getUniqueId());
	}

	public static Status getStatus(UUID uuid) {
		try {
			Statement stm = Main.con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * from `status` WHERE uuid='" + uuid.toString() + "'");
			if (rs.next()) {
				return new Status(rs.getString("name"), UUID.fromString(rs.getString("uuid")),
						Integer.valueOf(rs.getInt("kills")), Integer.valueOf(rs.getInt("deaths")),
						Integer.valueOf(rs.getInt("wins")), Integer.valueOf(rs.getInt("xp")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Status("", uuid, Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
	}

	public enum Order {
		WINS, KILLS, DEATHS, XP;
	}
}
