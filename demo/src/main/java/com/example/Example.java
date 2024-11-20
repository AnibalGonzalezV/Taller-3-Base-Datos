package com.example;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Este es un ejemplo de como podrian ejecutar el
 * procedimiento almacenado dentro de java
 */
public class Example {

    public void registrarTransferencia(int cuentaOrigen, int cuentaDestino, double monto) {
    String sqlCall = "{CALL realizarTransferencia(?, ?, ?)}";
    
    DatabaseConnection dbConnection = new DatabaseConnection();

    try (Connection conn = dbConnection.getConnection()) {
        conn.setAutoCommit(false); // Desactivar auto-commit para control de transacción desde Java

        try (CallableStatement stmt = conn.prepareCall(sqlCall)) {
            // Configurar parámetros del procedimiento
            stmt.setInt(1, cuentaOrigen);
            stmt.setInt(2, cuentaDestino);
            stmt.setDouble(3, monto);

            // Ejecutar el procedimiento almacenado
            stmt.execute();

            conn.commit(); // Confirmar la transacción en Java después de ejecutar el procedimiento
            System.out.println("Transferencia realizada exitosamente.");
        } catch (SQLException e) {
            conn.rollback(); // Revertir la transacción si el procedimiento falla
            System.out.println("Error al realizar la transferencia: " + e.getMessage());
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
