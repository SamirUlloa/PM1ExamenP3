package com.aplicacion.pm1examenp3.Clases;

import java.io.Serializable;

public class Medicamentos implements Serializable {

    private Integer id;
    private String descripcion;
    private String cantidad;
    private String tiempo;
    private String periocidad;
    private String pathImage;
    private byte[] image;

    public Medicamentos(){

    }

    public Medicamentos(Integer id, String descripcion, String cantidad, String tiempo, String periocidad, String pathImage, byte[] image) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.tiempo = tiempo;
        this.periocidad = periocidad;
        this.pathImage = pathImage;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getPeriocidad() {
        return periocidad;
    }

    public void setPeriocidad(String periocidad) {
        this.periocidad = periocidad;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
