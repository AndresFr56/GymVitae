package gym.vitae.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility para obtener conexiones JDBC a la base de datos MySQL.
 * Intenta leer variables de entorno y si existe un archivo .env en la raíz del proyecto
 * lo parsea y usa sus valores como fallback.
 * !NOTA: Esta implementacion se hizo, porque no sabiamos si podiamos utilizar librerias externas.
 * Incluso no sabemos si podemos utilizar una ORM como Hibernate.
 */
public class DatabaseConnector {

	private static final Map<String, String> envCache = new HashMap<>();

	private static void loadDotEnvIfPresent() {
		if (!envCache.isEmpty()) return;
		Path p = Paths.get(System.getProperty("user.dir"), ".env");
		if (!Files.exists(p)) return;
		try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#")) continue;
				int idx = line.indexOf('=');
				if (idx <= 0) continue;
				String k = line.substring(0, idx).trim();
				String v = line.substring(idx + 1).trim();
				// Remove optional quotes
				if ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'"))) {
					v = v.substring(1, v.length() - 1);
				}
				envCache.put(k, v);
			}
		} catch (IOException ignored) {
		}
	}

	private static String getEnv(String key, String defaultVal) {
		String v = System.getenv(key);
		if (v != null && !v.isEmpty()) return v;
		loadDotEnvIfPresent();
		v = envCache.get(key);
		return (v != null && !v.isEmpty()) ? v : defaultVal;
	}

	public static Connection getConnection() throws SQLException {
		String host = getEnv("MYSQL_HOST", "localhost");
		String port = getEnv("MYSQL_PORT", "3306");
		String database = getEnv("MYSQL_DATABASE", "gym_system");
		String user = getEnv("MYSQL_USER", "gym_admin");
		String password = getEnv("MYSQL_PASSWORD", "gym_pass_2025");
		String jdbcUrl = getEnv("MYSQL_URL", String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC", host, port, database));

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException ignored) {
			// JDBC 4+ drivers load automatically when on classpath, ignore if not found here.
		}

		return DriverManager.getConnection(jdbcUrl, user, password);
	}
}
