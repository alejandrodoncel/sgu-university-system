package com.universidad.sgu.config;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {
        try (FileReader reader = new FileReader("db.properties")) {
            properties.load(reader);
            // Opcional: Registrar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            // En una aplicación real, esto debería ser manejado con un logger
            // y posiblemente terminar la aplicación si la DB es esencial.
            System.err.println("Error al cargar la configuración de la base de datos o el driver.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url") + "?useUnicode=true&characterEncoding=UTF-8";
        return DriverManager.getConnection(
                url,
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}
