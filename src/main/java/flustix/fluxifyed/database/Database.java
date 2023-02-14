package flustix.fluxifyed.database;

import com.google.gson.JsonObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import flustix.fluxifyed.Main;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class Database {
    static HikariDataSource dataSource;

    public static void initializeDataSource() {
        JsonObject config = Main.getConfig().getAsJsonObject("database");

        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
            hikariConfig.setJdbcUrl("jdbc:mysql://foxes4life.net:3306/fluxifyed");
            hikariConfig.setUsername(config.get("user").getAsString());
            hikariConfig.setPassword(config.get("pass").getAsString());
            dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception ex) {
            Main.LOGGER.error("Error while initializing the database", ex);
        }
    }

    public static ResultSet executeQuery(@Language("mysql") String query) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            connection.close(); // no, hikari does not auto close them, i tested it
            return rs;
        } catch (Exception ex) {
            Main.LOGGER.error("Failed to execute query: " + query, ex);
            return null;
        }
    }

    public static void executeQuery(@Language("mysql") String query, QueryCallback callback) {
        callback.onResult(executeQuery(query));
    }

    public static void executeQuery(@Language("mysql") String query, String[] replaceable, QueryCallback callback) {
        query = replaceQuery(query, replaceable);
        callback.onResult(executeQuery(query));
    }

    public static ResultSet executeQuery(@Language("mysql") String query, List<Object> replaceables) {
        for (Object replaceable : replaceables) {
            query = query.replaceFirst("\\?", replaceable.toString());
        }

        return executeQuery(query);
    }

    public static ResultSet executeQuery(@Language("mysql") String query, Object... replaceables) {
        return executeQuery(query, Arrays.stream(replaceables).toList());
    }

    public static String escape(String str) {
        return str.replace("\"", "\\\"")
                .replace("'", "\\'")
                .replace("`", "\\`")
                .replace("=", "\\=");
    }

    private static String replaceQuery(String query, String[] replaceable) {
        for (String s : replaceable)
            query = query.replaceFirst("\\?", s);

        return query;
    }

    public static int connectionCount() {
        return dataSource.getHikariPoolMXBean().getActiveConnections();
    }
}
