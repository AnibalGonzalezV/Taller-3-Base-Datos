package com.example.models;

public class Cliente {

    private int idCliente;
    private String nombreCompleto;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private String contrasena;

    // Constructor, getters y setters
    public Cliente(int idCliente, String nombreCompleto, String direccion, String telefono, String correoElectronico, String contrasena) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }

        // Getters y setters
        public int getIdCliente() {
            return idCliente;
        }
    
        public String getCorreoElectronico() {
            return correoElectronico;
        }
    
        public String getContrasena() {
            return contrasena;
        }

        
}
