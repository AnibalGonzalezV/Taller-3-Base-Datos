public class Usuario {
    private String correoElectronico;
    private String contraseña;

    public Usuario(String correoElectronico, String contraseña) {
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
    }

    protected String getCorreoElectronico() {
        return this.correoElectronico;
    }

    protected String getContraseña() {
        return this.contraseña;
    }

}
