package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que se encarga de establecer la conexión con la base de datos PostgreSQL.
 */
public class DatabaseConnection {

    private static Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "minombre2";

    public static Connection getConnection() throws SQLException {
        // Si la conexión aún no ha sido creada, se crea
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos: " + e.getMessage());
                throw e;  // Lanza la excepción para ser manejada más arriba
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
