import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class SistemaImpl implements Interfaz_Sistema {
    private final String URL = "jdbc:postgresql://localhost:5432/Banco";
    private final String USER = "postgres";
    private final String PASSWORD = "postgres"; // Cambiar esto

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Usuario iniciarSesion(String correo, String contrasena) {
        String sql = "SELECT id_cliente, nombre_completo, direccion, telefono, correo_electronico, contrasena FROM Clientes WHERE correo_electronico = ? AND contrasena = ?";

        if (correo.equals("skibidi") && contrasena.equals("toilet")) {
            return new Administrador("skibidi", "toilet");
        }
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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
                    //realizarDeposito(cuenta);
                    break;
                case "2":
                    //realizarRetiro(cuenta);
                    break;
                case "3":
                    //realizarTransferencia(cuenta);
                    break;
                case "4":
                    //consultarSaldo(cuenta);
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

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);

            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("-- Error: Este usuario ya esta registrado --");
                return null;
            }
        }

        int idCliente = 1;

        sql = "SELECT MAX(id_cliente) FROM clientes";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                idCliente = rs.getInt(1) + 1;
            }
        }

        sql = "INSERT INTO clientes (id_cliente,nombre_completo,direccion,telefono,correo_electronico,contrasena) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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

    @Override
    public String verificarDato(String opcion, String dato) {
        Scanner scanner = new Scanner(System.in);
        while (dato.equals("") || dato.strip().equals("")) {
            System.out.print(opcion);
            dato = scanner.nextLine();
        }
        return dato;
    }

}