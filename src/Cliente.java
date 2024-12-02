public class Cliente extends Usuario {
    private int idCliente;
    private String nombreCompleto;
    private String direccion;
    private String telefono;
    
    public Cliente(int idCliente, String nombreCompleto, String direccion, String telefono, String correoElectronico, String contraseña)  {
        super(correoElectronico, contraseña);
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

}
