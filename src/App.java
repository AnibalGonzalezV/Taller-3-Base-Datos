import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Interfaz_Sistema sistema = new SistemaImpl();
        Usuario usuario;

        System.out.println("Bienvenido al sistema bancario.");
        System.out.print("Ingrese su correo electrónico: ");
        String correo = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        usuario = sistema.iniciarSesion(correo, contrasena);

        if (usuario == null) {
            String opcion = "";
            
            while (!opcion.toLowerCase().equals("s") && !opcion.toLowerCase().equals("n")) {
                System.out.print("¿Desea registrar su usuario? (s/n): ");
                opcion = scanner.nextLine();
            }
            
            if (opcion.toLowerCase().equals("s")) {
                usuario = sistema.registrarCliente();
            }
        }

        if (usuario instanceof Administrador) {
            sistema.menuAdministrador(usuario);
        }
        else if (usuario instanceof Cliente) {
            sistema.menuCliente(usuario);
        }
        else {
            System.out.println("No registrado");
        }
    }
}
