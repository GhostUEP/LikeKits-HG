package me.ghost.hg.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.MapMaker;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.KitDiario;
import me.ghost.hg.enums.BoxType;
import me.ghost.mysql.Connect;

public class MySQLCommands {
	private Main m;
	public Map<UUID, KitDiario> expires = new MapMaker().softValues().makeMap();

	public MySQLCommands(Main main) {
		m = main;
	}

	public void setPlayerBoxesandXP(UUID uuid) throws SQLException {
		int bronze = 0;
		int prata = 0;
		int ouro = 0;
		int diamante = 0;
		if (Main.con.isClosed()) {
			m.connection.trySQLConnection();
		}
		PreparedStatement sql = Main.con
				.prepareStatement("SELECT * FROM `caixas` WHERE uuid='" + uuid.toString().replace("-", "") + "';");
		ResultSet result = sql.executeQuery();

		if (result.next()) {
			bronze = result.getInt("bronze");
			prata = result.getInt("prata");
			ouro = result.getInt("ouro");
			diamante = result.getInt("diamante");
		}

		result.close();
		sql.close();
		m.box.bronzeBoxes.put(uuid, bronze);
		m.box.prataBoxes.put(uuid, prata);
		m.box.goldBoxes.put(uuid, ouro);
		m.box.diamondBoxes.put(uuid, diamante);
	}

	public void removePlayerBoxes(UUID uuid, BoxType tipo, int qnt) throws SQLException {
		if (tipo == BoxType.BRONZE) {
			Connect.lock.lock();
			if (Main.con.isClosed()) {
				m.connection.trySQLConnection();
			}
			PreparedStatement sql = Main.con.prepareStatement(
					"SELECT bronze FROM `caixas` WHERE uuid='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = sql.executeQuery();

			if (result.next()) {
				int caixas;
				caixas = result.getInt("bronze") - qnt;
				if (caixas < 0) {
					caixas = 0;
				}
				sql.execute("UPDATE `caixas` SET bronze='" + caixas + "' WHERE uuid='"
						+ uuid.toString().replace("-", "") + "';");
			}

			result.close();
			sql.close();
			Connect.lock.unlock();
		} else if (tipo == BoxType.PRATA) {
			Connect.lock.lock();
			if (Main.con.isClosed()) {
				m.connection.trySQLConnection();
			}
			PreparedStatement sql = Main.con.prepareStatement(
					"SELECT prata FROM `caixas` WHERE uuid='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = sql.executeQuery();

			if (result.next()) {
				int caixas;
				caixas = result.getInt("prata") - qnt;
				if (caixas < 0) {
					caixas = 0;
				}
				sql.execute("UPDATE `caixas` SET prata='" + caixas + "' WHERE uuid='" + uuid.toString().replace("-", "")
						+ "';");
			}

			result.close();
			sql.close();
			Connect.lock.unlock();
		} else if (tipo == BoxType.OURO) {
			Connect.lock.lock();
			if (Main.con.isClosed()) {
				m.connection.trySQLConnection();
			}
			PreparedStatement sql = Main.con.prepareStatement(
					"SELECT ouro FROM `caixas` WHERE uuid='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = sql.executeQuery();

			if (result.next()) {
				int caixas;
				caixas = result.getInt("ouro") - qnt;
				if (caixas < 0) {
					caixas = 0;
				}
				sql.execute("UPDATE `caixas` SET ouro='" + caixas + "' WHERE uuid='" + uuid.toString().replace("-", "")
						+ "';");
			}

			result.close();
			sql.close();
			Connect.lock.unlock();
		} else if (tipo == BoxType.DIAMANTE) {
			Connect.lock.lock();
			if (Main.con.isClosed()) {
				m.connection.trySQLConnection();
			}
			PreparedStatement sql = Main.con.prepareStatement(
					"SELECT diamante FROM `caixas` WHERE uuid='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = sql.executeQuery();

			if (result.next()) {
				int caixas;
				caixas = result.getInt("diamante") - qnt;
				if (caixas < 0) {
					caixas = 0;
				}
				sql.execute("UPDATE `caixas` SET diamante='" + caixas + "' WHERE uuid='"
						+ uuid.toString().replace("-", "") + "';");
			}

			result.close();
			sql.close();
			Connect.lock.unlock();
		}
	}

	public void addPlayerBoxes(UUID uuid, BoxType tipo, int qnt) throws SQLException {
		if (tipo == BoxType.BRONZE) {
			Connect.lock.lock();
			if (Main.con.isClosed()) {
				m.connection.trySQLConnection();
			}
			PreparedStatement sql = Main.con.prepareStatement(
					"SELECT bronze FROM `caixas` WHERE uuid='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = sql.executeQuery();
			int caixas;
			if (result.next()) {
				caixas = result.getInt("bronze") + qnt;
				sql.execute("UPDATE `caixas` SET bronze='" + caixas + "' WHERE uuid='"
						+ uuid.toString().replace("-", "") + "';");
			} else {
				sql.execute("INSERT INTO `caixas`(`uuid`, `bronze`, `prata`, `ouro`, `diamante`) VALUES ('"
						+ uuid.toString().replace("-", "") + "', '" + qnt + "', '0', '0', '0');");
			}

			result.close();
			sql.close();
			Connect.lock.unlock();
		} else if (tipo == BoxType.PRATA) {
			Connect.lock.lock();
			if (Main.con.isClosed()) {
				m.connection.trySQLConnection();
			}
			PreparedStatement sql = Main.con.prepareStatement(
					"SELECT prata FROM `caixas` WHERE uuid='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = sql.executeQuery();
			int caixas;
			if (result.next()) {
				caixas = result.getInt("prata") + qnt;
				sql.execute("UPDATE `caixas` SET prata='" + caixas + "' WHERE uuid='" + uuid.toString().replace("-", "")
						+ "';");
			} else {
				sql.execute("INSERT INTO `caixas`(`uuid`, `bronze`, `prata`, `ouro`, `diamante`) VALUES ('"
						+ uuid.toString().replace("-", "") + "', '0', '" + qnt + "', '0', '0');");
			}

			result.close();
			sql.close();
			Connect.lock.unlock();
		} else if (tipo == BoxType.OURO) {
			Connect.lock.lock();
			if (Main.con.isClosed()) {
				m.connection.trySQLConnection();
			}
			PreparedStatement sql = Main.con.prepareStatement(
					"SELECT ouro FROM `caixas` WHERE uuid='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = sql.executeQuery();
			int caixas;
			if (result.next()) {
				caixas = result.getInt("ouro") + qnt;
				sql.execute("UPDATE `caixas` SET ouro='" + caixas + "' WHERE uuid='" + uuid.toString().replace("-", "")
						+ "';");
			} else {
				sql.execute("INSERT INTO `caixas`(`uuid`, `bronze`, `prata`, `ouro`, `diamante`) VALUES ('"
						+ uuid.toString().replace("-", "") + "', '0', '0', '" + qnt + "', '0');");
			}

			result.close();
			sql.close();
			Connect.lock.unlock();
		} else if (tipo == BoxType.DIAMANTE) {
			Connect.lock.lock();
			if (Main.con.isClosed()) {
				m.connection.trySQLConnection();
			}
			PreparedStatement sql = Main.con.prepareStatement(
					"SELECT diamante FROM `caixas` WHERE uuid='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = sql.executeQuery();
			int caixas;
			if (result.next()) {
				caixas = result.getInt("diamante") + qnt;
				sql.execute("UPDATE `caixas` SET diamante='" + caixas + "' WHERE uuid='"
						+ uuid.toString().replace("-", "") + "';");
			} else {
				sql.execute("INSERT INTO `caixas`(`uuid`, `bronze`, `prata`, `ouro`, `diamante`) VALUES ('"
						+ uuid.toString().replace("-", "") + "', '0', '0', '0', '" + qnt + "');");
			}

			result.close();
			sql.close();
			Connect.lock.unlock();
		}
	}

	public void loadExpire(UUID uuid) throws SQLException {
		Connect.lock.lock();
		PreparedStatement stmt = Main.con
				.prepareStatement("SELECT * FROM `kitdiario` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			String kit = result.getString("kit");
			long expire = result.getLong("expire");
			expires.put(uuid, new KitDiario(uuid, expire, kit));
		}
		result.close();
		stmt.close();
		Connect.lock.unlock();
	}

	public KitDiario getKitDiario(UUID uuid) {
		return expires.get(uuid);
	}

	public void removeExpire(UUID uuid) throws SQLException {
		Connect.lock.lock();
		PreparedStatement stmt = Main.con
				.prepareStatement("SELECT * FROM `kitdiario` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("DELETE FROM `kitdiario` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
		}
		result.close();
		stmt.close();
		Connect.lock.unlock();
	}

	public void addExpire(UUID uuid, String kit, long expireLong) throws SQLException {
		KitDiario expire;
		if (expires.containsKey(uuid)) {
			expire = expires.get(uuid);
			expire.setKit(kit);
			expire.addLong(expireLong);
		} else
			expire = new KitDiario(uuid, expireLong, kit);
		expires.put(uuid, expire);
		Connect.lock.lock();
		PreparedStatement stmt = Main.con
				.prepareStatement("SELECT * FROM `kitdiario` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("UPDATE `kitdiario` SET `kit`='" + expire.getKit().toLowerCase() + "', `expire`=" + expireLong
					+ "  WHERE uuid='" + uuid.toString().replace("-", "") + "';");
		} else {
			stmt.execute("INSERT INTO `kitdiario`(`uuid`, `expire`, `kit`) VALUES ('" + uuid.toString().replace("-", "")
					+ "'," + expire.getExpire() + " ,'" + expire.getKit().toLowerCase() + "');");
		}
		result.close();
		stmt.close();
		Connect.lock.unlock();
	}
}
