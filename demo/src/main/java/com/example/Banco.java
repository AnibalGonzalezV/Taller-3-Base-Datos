package com.example;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.database.DatabaseConnection;
import com.example.models.Cliente;
import com.example.models.Cuenta;

public class Banco {

    // Método para iniciar sesión del cliente
    public static Cliente iniciarSesion(String correo, String contrasena) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE correo_electronico = ? AND contrasena = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre_completo"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico"),
                        rs.getString("contrasena")
                );
            }
        }
        return null; // FALTA AGREGAR EL REGISTRO DE NUEVO CLIENTE
    }

    // Método para obtener la cuenta por número de cuenta
    public static Cuenta obtenerCuenta(String numeroCuenta) throws SQLException {
        String sql = "SELECT * FROM cuentas WHERE numero_cuenta = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeroCuenta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Cuenta(
                        rs.getInt("id_cuenta"),
                        rs.getString("numero_cuenta"),
                        rs.getInt("saldo")
                );
            }
        }
        return null;
    }

    // Método para realizar un depósito
    public static void deposito(Cuenta cuenta, int monto) throws SQLException {
        String sql = "INSERT INTO transacciones (cuenta_id, tipo_operacion, monto) VALUES (?, 'deposito', ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cuenta.getIdCuenta());
            ps.setInt(2, monto);
            ps.executeUpdate();

            // Actualizar saldo de la cuenta
            cuenta.setSaldo(cuenta.getSaldo() + monto);
            actualizarSaldoCuenta(cuenta);
        }
    }

    // Método para realizar un retiro
    public static void retiro(Cuenta cuenta, int monto) throws SQLException {
        if (cuenta.getSaldo() >= monto) {
            String sql = "INSERT INTO transacciones (cuenta_id, tipo_operacion, monto) VALUES (?, 'retiro', ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, cuenta.getIdCuenta());
                ps.setInt(2, monto);
                ps.executeUpdate();

                // Actualizar saldo de la cuenta
                cuenta.setSaldo(cuenta.getSaldo() - monto);
                actualizarSaldoCuenta(cuenta);
            }
        } else {
            System.out.println("Saldo insuficiente.");
        }
    }

    // Método para realizar una transferencia
    // Función para realizar una transferencia
private static void realizarTransferencia(Cuenta cuentaOrigen) throws SQLException {
    System.out.print("Ingrese el número de cuenta destino: ");
    String numeroCuentaDestino = scanner.nextLine();
    Cuenta cuentaDestino = Banco.obtenerCuenta(numeroCuentaDestino);

    if (cuentaDestino != null) {
        System.out.print("Ingrese el monto a transferir: ");
        int monto = Integer.parseInt(scanner.nextLine());

        // Llamar al procedimiento almacenado para realizar la transferencia
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Llamada al procedimiento almacenado transferir_fondos
            CallableStatement stmt = conn.prepareCall("{CALL transferir_fondos(?, ?, ?)}");
            stmt.setInt(1, cuentaOrigen.getIdCuenta());  // Cuenta origen
            stmt.setInt(2, cuentaDestino.getIdCuenta()); // Cuenta destino
            stmt.setInt(3, monto);  // Monto de la transferencia

            // Ejecutar el procedimiento
            stmt.execute();
            System.out.println("Transferencia realizada correctamente.");
        } catch (SQLException e) {
            // Captura excepciones lanzadas desde el procedimiento almacenado (por ejemplo, saldo insuficiente)
            System.out.println("Error al realizar la transferencia: " + e.getMessage());
        }
    } else {
        System.out.println("Cuenta destino no encontrada.");
    }
}

    // Método para actualizar el saldo en la base de datos
    private static void actualizarSaldoCuenta(Cuenta cuenta) throws SQLException {
        String sql = "UPDATE cuentas SET saldo = ? WHERE id_cuenta = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cuenta.getSaldo());
            ps.setInt(2, cuenta.getIdCuenta());
            ps.executeUpdate();
        }
    }
}
