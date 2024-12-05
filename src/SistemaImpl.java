import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaImpl implements Interfaz_Sistema {
    private final String URL = "jdbc:postgresql://localhost:5432/Banco";
    private final String USER = "postgres";
    private final String PASSWORD = "minombre2"; // Cambiar esto

    private Connection connection;
    
    public SistemaImpl() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.err.println("Conexión a la base de datos exitosa");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }	

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Usuario iniciarSesion(String correo, String contrasena) {
        String sql = "SELECT id_cliente, nombre_completo, direccion, telefono, correo_electronico, contrasena FROM Clientes WHERE correo_electronico = ? AND contrasena = ?";
    
        if (correo.equals("skibidi") && contrasena.equals("toilet")) {
            return new Administrador("skibidi", "toilet");
        }
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, correo);
            pstmt.setString(2, contrasena);
    
            ResultSet rs = pstmt.executeQuery();
            
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
            return null;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void menuAdministrador(Usuario administrador) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Administrador Administrador = (Administrador) administrador;
        System.out.println("Bienvenido " + Administrador.getCorreoElectronico());
        String opcion;
        do {
            System.out.println("\n--- Menú Administrador ---");
            System.out.println("1. Consultar Historial de Transacciones");
            System.out.println("2. Generar Reportes Financieros");
            System.out.println("3. Vista de Cuentas Inactivas");
            System.out.println("4. Configuración de Usuarios");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    //consultarHistorialTransacciones();
                    break;
                case "2":
                    //generarReportesFinancieros();
                    break;
                case "3":
                    //verCuentasInactivas();
                    break;
                case "4":
                    //configuracionDeUsuarios();
                    break;
                case "5":
                    System.out.println("--- Saliendo ---");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (!opcion.equals("5"));
    }

    @Override
    public void menuCliente(Usuario cliente) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Cliente Cliente = (Cliente) cliente;
        System.out.println("-- Bienvenido " + Cliente.getNombreCompleto() + " --");
        String opcion;
        do {
            System.out.println("\n--- Menú Cliente ---");
            System.out.println("1. Depósitos");
            System.out.println("2. Retiros");
            System.out.println("3. Transferencias");
            System.out.println("4. Consulta de Saldo");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    realizarDeposito(Cliente.getIdCliente());
                    break;
                case "2":
                    realizarRetiro(Cliente.getIdCliente());
                    break;
                case "3":
                    realizarTransferencia(Cliente.getIdCliente());
                    break;
                case "4":
                    consultarSaldo(Cliente.getIdCliente());
                    break;
                case "5":
                    System.out.println("--- Saliendo ---");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (!opcion.equals("5"));
    }

    @Override
    public String verificarDato(String opcion, String dato) {
        Scanner scanner = new Scanner(System.in);
        while (dato.equals("") || dato.strip().equals("")) {
            System.out.print(opcion);
            dato = scanner.nextLine();
        }
        return dato;
    }

    @Override
    public Usuario registrarCliente() throws SQLException {
        Scanner scanner = new Scanner(System.in);
    
        String correo = "", contrasena = "", nombreCompleto = "", direccion = "", telefono = "";
        
        System.out.println("---Registrar usuario---");
        correo = verificarDato("Correo electrónico: ", correo);
        contrasena = verificarDato("Contraseña: ", contrasena);
        nombreCompleto = verificarDato("Nombre completo: ", nombreCompleto);
        direccion = verificarDato("Dirección: ", direccion);
        telefono = verificarDato("Teléfono: ", telefono);
    
        String sql = "SELECT correo_electronico FROM Clientes WHERE correo_electronico = ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
    
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("-- Error: Este usuario ya está registrado --");
                return null;
            }
        }
    
        int idCliente = 1;
    
        sql = "SELECT MAX(id_cliente) FROM clientes";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                idCliente = rs.getInt(1) + 1;
            }
        }
    
        sql = "INSERT INTO clientes (id_cliente, nombre_completo, direccion, telefono, correo_electronico, contrasena) VALUES (?, ?, ?, ?, ?, ?)";
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCliente);
            pstmt.setString(2, nombreCompleto);
            pstmt.setString(3, direccion);
            pstmt.setString(4, telefono);
            pstmt.setString(5, correo);
            pstmt.setString(6, contrasena);
    
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("--- Usuario Registrado ---\n");
                return new Cliente(
                        idCliente,
                        nombreCompleto,
                        direccion,
                        telefono,
                        correo,
                        contrasena
                    );
            }
    
            System.out.println("No se pudo registrar el usuario");
            return null;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }    

    public void realizarTransferencia(int clienteId) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        List<Cuenta> cuentasOrigen = obtenerCuentasCliente(clienteId);
        
        if (cuentasOrigen.isEmpty()) {
            System.out.println("No tiene cuentas asociadas para realizar transferencias.");
            return;
        }
    
        // Mostrar las cuentas y permitir que el usuario seleccione una
        System.out.println("\n--- Cuentas disponibles para transferencias ---");
        for (int i = 0; i < cuentasOrigen.size(); i++) {
            Cuenta cuenta = cuentasOrigen.get(i);
            System.out.printf("%d. Cuenta: %s | Saldo: $%d\n", i + 1, cuenta.getNumeroCuenta(), cuenta.getSaldo());
        }
    
        System.out.print("\nSeleccione el número de la cuenta origen: ");
        int seleccion;
        try {
            seleccion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Selección inválida.");
            return;
        }
        
        if (seleccion < 1 || seleccion > cuentasOrigen.size()) {
            System.out.println("Selección fuera de rango.");
            return;
        }
        
        Cuenta cuentaOrigen = cuentasOrigen.get(seleccion - 1);
    
        // Continuar con el proceso de transferencia
        System.out.print("Ingrese el número de cuenta destino: ");
        String numeroCuentaDestino = scanner.nextLine();
        Cuenta cuentaDestino = verificarCuentaDestino(numeroCuentaDestino);
        if (cuentaDestino == null) {
            System.out.println("La cuenta destino no existe.");
            return;
        }
    
        System.out.print("Ingrese el monto a transferir: ");
        int monto;
        try {
            monto = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Monto inválido.");
            return;
        }
    
        if (monto <= 0) {
            System.out.println("Monto inválido, ingrese un monto mayor a cero.");
            return;
        }
        if(monto > cuentaOrigen.getSaldo()) {
            System.out.println("Saldo insuficiente para realizar la transferencia.");
            return;
        }
    
        transferirFondos(cuentaOrigen, cuentaDestino, monto);
         
    }

    private List<Cuenta> obtenerCuentasCliente(int clienteId) throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT id_cuenta, numero_cuenta, saldo FROM cuentas WHERE cliente_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cuenta cuenta = new Cuenta(
                        rs.getInt("id_cuenta"),
                        rs.getString("numero_cuenta"),
                        rs.getInt("saldo")
                    );
                    cuentas.add(cuenta);
                }
            }
        }
        return cuentas;
    }

    private Cuenta verificarCuentaDestino(String numeroCuentaDestino) throws SQLException {
        String sql = "SELECT id_cuenta, numero_cuenta, saldo FROM cuentas WHERE numero_cuenta = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numeroCuentaDestino);
            ResultSet rs = pstmt.executeQuery();
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

    private void transferirFondos(Cuenta cuentaOrigen, Cuenta cuentaDestino, int monto) {
        String callSql = "CALL transferir_fondos(?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(callSql)) {
            stmt.setInt(1, cuentaOrigen.getIdCuenta());
            stmt.setInt(2, cuentaDestino.getIdCuenta());
            stmt.setInt(3, monto);

            stmt.execute();
            System.out.println("Transferencia realizada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al realizar la transferencia: " + e.getMessage());
        }
    }

    private void consultarSaldo(int clienteId) {
        String sql = "SELECT c.numero_cuenta, c.saldo " +
                     "FROM cuentas c " +
                     "WHERE c.cliente_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setInt(1, clienteId);
        
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n--- Consulta de Saldo ---");
                boolean hayCuentas = false;
        
                while (rs.next()) {
                    hayCuentas = true;
                    String numeroCuenta = rs.getString("numero_cuenta");
                    int saldo = rs.getInt("saldo");
                    System.out.printf("Número de Cuenta: %s | Saldo: $%d\n", numeroCuenta, saldo);
                }
        
                if (!hayCuentas) {
                    System.out.println("No se encontraron cuentas asociadas a este cliente.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el saldo.");
            e.printStackTrace();
        }
    }

    private void realizarDeposito(int clienteId) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        List <Cuenta> cuentas = obtenerCuentasCliente(clienteId);

        if (cuentas.isEmpty()) {
            System.out.println("No tiene cuentas asociadas para realizar depósitos.");
            return;
        }

        System.out.println("\n--- Cuentas disponibles para depósitos ---");
        for (int i = 0; i < cuentas.size(); i++) {
            Cuenta cuenta = cuentas.get(i);
            System.out.printf("%d. Cuenta: %s | Saldo: $%d\n", i + 1, cuenta.getNumeroCuenta(), cuenta.getSaldo());
        }

        System.out.println("\nSeleccione el número de la cuenta origen: ");
        int seleccion;

        try {
            seleccion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Selección inválida.");
            return;
        }

        if (seleccion < 1 || seleccion > cuentas.size()) {
            System.out.println("Selección fuera de rango.");
            return;
        }

        Cuenta cuentaSelecionada = cuentas.get(seleccion - 1);

        System.out.print("Ingrese el monto a depositar: ");
        int monto;
        try {
            monto = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Monto inválido.");
            return;
        }

        if (monto <= 0) {
            System.out.println("Ingrese un monto mayor que cero.");
            return;
        }

        registrarTransaccion(cuentaSelecionada, monto, "deposito");
    }

    private void realizarRetiro(int clienteId) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        List <Cuenta> cuentas = obtenerCuentasCliente(clienteId);

        if (cuentas.isEmpty()) {
            System.out.println("No tiene cuentas asociadas para realizar retiros.");
            return;
        }

        System.out.println("\n--- Cuentas disponibles para retiros ---");
        for (int i = 0; i < cuentas.size(); i++) {
            Cuenta cuenta = cuentas.get(i);
            System.out.printf("%d. Cuenta: %s | Saldo: $%d\n", i + 1, cuenta.getNumeroCuenta(), cuenta.getSaldo());
        }

        System.out.println("\nSeleccione el número de la cuenta origen: ");

        int seleccion;

        try {
            seleccion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Selección inválida.");
            return;
        }

        if (seleccion < 1 || seleccion > cuentas.size()) {
            System.out.println("Selección fuera de rango.");
            return;
        }  

        Cuenta cuentaSelecionada = cuentas.get(seleccion - 1);

        System.out.println("Ingrese el monto a retirar: ");
        int monto;
        try {
            monto = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Monto inválido.");
            return;
        } 
        if (monto <= 0) {
            System.out.println("Ingrese un monto mayor que cero.");
            return;
        }
        if (monto > cuentaSelecionada.getSaldo()) {
            System.out.println("Saldo insuficiente para realizar el retiro.");
            return;
        }

        registrarTransaccion(cuentaSelecionada, monto, "retiro");
    }


    private void registrarTransaccion(Cuenta cuentaOrigen, int monto, String tipo_Operacion) {
        String sql = "INSERT INTO transacciones (cuenta_id, tipo_operacion, monto) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cuentaOrigen.getIdCuenta());
            pstmt.setString(2, tipo_Operacion);
            pstmt.setInt(3, monto);

            pstmt.executeUpdate();
            System.out.println(tipo_Operacion.substring(0, 1).toUpperCase() + tipo_Operacion.substring(1) + " registrado correctamente.");
            
        } catch (SQLException e) {
            System.out.println("Error al registrar la transacción." + e.getMessage());
        }
    }
}