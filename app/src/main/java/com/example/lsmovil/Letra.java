package com.example.lsmovil;

public class Letra {
    private String letra;
    private String descripcion;
    private int imagenResId; // Resource ID de la imagen (drawable)

    public Letra(String letra, String descripcion, int imagenResId) {
        this.letra = letra;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
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
