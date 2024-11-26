package com.example.models;

import java.util.Date;

public class Cuenta {

    private int idCuenta;
    private String numeroCuenta;
    private int clienteId;
    private int saldo;
    private Date fechaCreacion;

    // Constructor, getters y setters
    public Cuenta(int idCuenta, String numeroCuenta, int clienteId, int saldo, Date fechaCreacion) {
        this.idCuenta = idCuenta;
        this.numeroCuenta = numeroCuenta;
        this.clienteId = clienteId;
        this.saldo = saldo;
        this.fechaCreacion = fechaCreacion;
    }

    public Cuenta(int idCuenta, String numeroCuenta, int saldo) {

        this.idCuenta = idCuenta;

        this.numeroCuenta = numeroCuenta;

        this.saldo = saldo;

    }
        // Getters y setters
    public int getIdCuenta() {
        return idCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
