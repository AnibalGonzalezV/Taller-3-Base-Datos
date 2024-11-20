package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    
    /**
     * Este es un ejemplo de cómo se puede conectar a una base de datos PostgreSQL
     * y realizar operaciones básicas como crear una tabla, insertar registros y
     * consultar registros.
     */
    public static void main(String[] args) throws Exception {
        
        // Establece la conexión con la base de datos PostgreSQL
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        
        // Crea una tabla llamada 'test' si no existe
        String sql = "Create table if not exists test (id serial primary key, name varchar(100))";
        Statement statement = connection.createStatement();
        statement.execute(sql);
        
        // Inserta dos registros en la tabla 'test'
        String insertSql = "INSERT INTO test (name) VALUES ('John Doe'), ('Jane Doe')";
        statement.executeUpdate(insertSql);
        
        // Consulta todos los registros de la tabla 'test'
        ResultSet resultSet = statement.executeQuery("SELECT * FROM test");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println("ID: " + id + ", Name: " + name);
        }
        
        // Cierra el ResultSet, Statement y la conexión
        resultSet.close();
        statement.close();
        connection.close();
    }
}
