package com.example;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.util.Scanner;
import com.example.database.DatabaseConnection;
import com.example.models.Cliente;
import com.example.models.Cuenta;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Muestra el menú de inicio
            System.out.println("Bienvenido al sistema bancario.");
            System.out.print("Ingrese su correo electrónico: ");
            String correo = scanner.nextLine();
            System.out.print("Ingrese su contraseña: ");
            String contrasena = scanner.nextLine();

            // Verificar si el usuario es cliente o administrador
            if (correo.equals("skibidi") && contrasena.equals("toilet")) {
                // Menú de administrador
                mostrarMenuAdministrador();
            } else {
                // Menú de cliente
                Cliente cliente = Banco.iniciarSesion(correo, contrasena);
                if (cliente != null) {
                    System.out.println("¡Bienvenido, " + cliente.getNombreCompleto() + "!");
                    mostrarMenuCliente(cliente);
                } else {
                    System.out.println("Credenciales incorrectas. ¿Desea registrarse? (s/n)");
                    String respuesta = scanner.nextLine();
                    if (respuesta.equalsIgnoreCase("s")) {
                        // Registrar cliente en el sistema (esto se debería agregar en tu lógica de base de datos)
                        System.out.println("Registro de nuevo cliente.");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }

    // Menú para Cliente
    private static void mostrarMenuCliente(Cliente cliente) throws SQLException {
        int opcion;
        do {
            System.out.println("\n--- Menú Cliente ---");
            System.out.println("1. Depósitos");
            System.out.println("2. Retiros");
            System.out.println("3. Transferencias");
            System.out.println("4. Consulta de Saldo");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            Cuenta cuenta = Banco.obtenerCuenta(cliente.getCorreoElectronico());

            switch (opcion) {
                case 1:
                    realizarDeposito(cuenta);
                    break;
                case 2:
                    realizarRetiro(cuenta);
                    break;
                case 3:
                    realizarTransferencia(cuenta);
                    break;
                case 4:
                    consultarSaldo(cuenta);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 5);
    }

    // Menú para Administrador
    private static void mostrarMenuAdministrador() {
        int opcion;
        do {
            System.out.println("\n--- Menú Administrador ---");
            System.out.println("1. Consultar Historial de Transacciones");
            System.out.println("2. Generar Reportes Financieros");
            System.out.println("3. Vista de Cuentas Inactivas");
            System.out.println("4. Configuración de Usuarios");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    consultarHistorialTransacciones();
                    break;
                case 2:
                    generarReportesFinancieros();
                    break;
                case 3:
                    verCuentasInactivas();
                    break;
                case 4:
                    configuracionDeUsuarios();
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 5);
    }

    // Función para realizar un depósito
    private static void realizarDeposito(Cuenta cuenta) throws SQLException {
        System.out.print("Ingrese el monto a depositar: ");
        int monto = Integer.parseInt(scanner.nextLine());
        Banco.deposito(cuenta, monto);
        System.out.println("Depósito realizado correctamente.");
    }

    // Función para realizar un retiro
    private static void realizarRetiro(Cuenta cuenta) throws SQLException {
        System.out.print("Ingrese el monto a retirar: ");
        int monto = Integer.parseInt(scanner.nextLine());
        Banco.retiro(cuenta, monto);
    }

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

    // Función para consultar saldo
    private static void consultarSaldo(Cuenta cuenta) {
        System.out.println("Saldo actual: " + cuenta.getSaldo());
    }

    // Función de consulta de historial de transacciones para Administrador
    private static void consultarHistorialTransacciones() {
        System.out.println("Historial de transacciones:");
        // Lógica de consulta del historial de transacciones
    }

    // Función para generar reportes financieros para Administrador
    private static void generarReportesFinancieros() {
        System.out.println("Generando reportes financieros...");
        // Lógica para generar reportes financieros
    }

    // Función para ver cuentas inactivas para Administrador
    private static void verCuentasInactivas() {
        System.out.println("Viendo cuentas inactivas...");
        // Lógica para ver cuentas inactivas
    }

    // Función para configuración de usuarios para Administrador
    private static void configuracionDeUsuarios() {
        System.out.println("Configurando usuarios...");
        // Lógica para agregar, editar o eliminar usuarios
    }
}
