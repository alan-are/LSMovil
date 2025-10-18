package com.example.lsmovil;

public class Numero {
    private String numero;
    private String descripcion;
    private int imagenResId; // Resource ID de la imagen (drawable)

    public Numero(String numero, String descripcion, int imagenResId) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public void setImagenResId(int imagenResId) {
        this.imagenResId = imagenResId;
    }
}
