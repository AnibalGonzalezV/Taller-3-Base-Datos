package com.example.models;

import java.util.Date;

public class Transaccion {

    private int idTransaccion;
    private int cuentaId;
    private String tipoOperacion;
    private int monto;
    private Date fechaTransaccion;
    private Integer idCuentaDestino; // Puede ser null

    // Constructor, getters y setters
    public Transaccion(int idTransaccion, int cuentaId, String tipoOperacion, int monto, Date fechaTransaccion, Integer idCuentaDestino) {
        this.idTransaccion = idTransaccion;
        this.cuentaId = cuentaId;
        this.tipoOperacion = tipoOperacion;
        this.monto = monto;
        this.fechaTransaccion = fechaTransaccion;
        this.idCuentaDestino = idCuentaDestino;
    }

    // Getters y setters
    public int getIdTransaccion() {
        return idTransaccion;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public int getMonto() {
        return monto;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public Integer getIdCuentaDestino() {
        return idCuentaDestino;
    }
}
