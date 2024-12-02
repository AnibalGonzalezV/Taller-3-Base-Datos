import java.sql.SQLException;

public interface Interfaz_Sistema {
    
    public Usuario iniciarSesion(String correo, String contrasena);

    public void menuCliente(Usuario cliente) throws SQLException;

    public void menuAdministrador(Usuario administrador) throws SQLException;

    public Usuario registrarCliente() throws SQLException;

    public String verificarDato(String opcion, String dato); 
}