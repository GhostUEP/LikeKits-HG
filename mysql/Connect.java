package me.ghost.hg.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import me.ghost.hg.Main;

public class Connect {
	public static ReentrantLock lock = new ReentrantLock(true);
	private Main m;

	public Connect(Main m) {
		this.m = m;
	}

	public synchronized void trySQLConnection() {
		if (!m.sql) {
			m.getLogger().info("MySQL Desativado!");
			return;
		}
		try {
			m.getLogger().info("Conectando ao MySQL");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String conn = "jdbc:mysql://" + m.host + ":" + m.port + "/" + m.database;
			Main.con = DriverManager.getConnection(conn, m.user, m.password);
		} catch (ClassNotFoundException ex) {
			m.getLogger().warning("MySQL Driver nao encontrado!");
			m.sql = false;
		} catch (SQLException ex) {
			m.getLogger().warning("Erro enquanto tentava conectar ao Mysql!");
			m.sql = false;
		} catch (Exception ex) {
			m.getLogger().warning("Erro desconhecido enquanto tentava conectar ao MySQL.");
			m.sql = false;
		}
	}

	public static void SQLdisconnect() {
		lock.lock();
		try {
			if (Main.con != null && !Main.con.isClosed()) {
				Main.con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lock.unlock();
	}

	public void SQLQuery(final String sql) {
		if (!m.sql)
			return;
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Thread(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					Statement stmt = Main.con.createStatement();
					stmt.executeUpdate(sql);
					stmt.close();
				} catch (SQLException e) {
					m.getLogger().info("Erro ao tentar executar Query");
					m.getLogger().info(sql);
					m.getLogger().info(e.getMessage());
				}
				lock.unlock();
			}
		}));
		executor.shutdown();
	}

	public void SQLQuerySync(final String sql) {
		if (!m.sql)
			return;
		lock.lock();
		try {
			Statement stmt = Main.con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			m.getLogger().info("Erro ao tentar executar Query");
			m.getLogger().info(sql);
			m.getLogger().info(e.getMessage());
		}
		lock.unlock();
	}

	public void SQLQuerySyncNoLock(final String sql) {
		if (!m.sql)
			return;
		lock.lock();
		try {
			Statement stmt = Main.con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			m.getLogger().info("Erro ao tentar executar Query");
			m.getLogger().info(sql);
			m.getLogger().info(e.getMessage());
		}
		lock.unlock();
	}
}
