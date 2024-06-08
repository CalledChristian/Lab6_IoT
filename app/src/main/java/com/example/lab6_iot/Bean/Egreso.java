package com.example.lab6_iot.Bean;

import java.io.Serializable;
import java.util.Date;

public class Egreso implements Serializable {
    private String idEgreso;

    private String titulo;

    private float monto;

    private String descripcion;

    private Date fecha;

    private String idUsuario;



    public Egreso(){
        //constructor para firestore
    }


    public Egreso(String idEgreso, String titulo, float monto, String descripcion, Date fecha, String idUsuario) {
        this.idEgreso = idEgreso;
        this.titulo = titulo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idUsuario = idUsuario;

    }

    public String getIdEgreso() {
        return idEgreso;
    }

    public void setIdEgreso(String idEgreso) {
        this.idEgreso = idEgreso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
