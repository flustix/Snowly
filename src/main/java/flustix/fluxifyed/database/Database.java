package flustix.fluxifyed.database;

import com.google.gson.JsonObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import flustix.fluxifyed.Main;

import java.sql.Connection;
import java.sql.ResultSet;

public class Database {
    static HikariDataSource dataSource;

    public static void initializeDataSource() {
        JsonObject config = Main.getConfig().getAsJsonObject("database");

        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.addDataSourceProperty("serverName", config.get("host").getAsString());
            hikariConfig.addDataSourceProperty("databaseName", config.get("database").getAsString());
            hikariConfig.addDataSourceProperty("port", 3306);
            hikariConfig.setUsername(config.get("user").getAsString());
            hikariConfig.setPassword(config.get("pass").getAsString());
            hikariConfig.setIdleTimeout(10000);
            hikariConfig.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
            dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception ex) {
            Main.LOGGER.error("Error while initializing the database", ex);
        }
    }

    public static ResultSet executeQuery(String query) {
        try {
            return dataSource.getConnection().createStatement().executeQuery(query);
        } catch (Exception ex) {
            Main.LOGGER.error("Failed to execute query: " + query, ex);
            return null;
        }
    }
}
