package com.example.consultores.delipollo.util;

import android.graphics.drawable.Drawable;

public class Platillo {

    private String title;
    private int platilloId;
    private String description;
    private String detalle;



    private Drawable imagen;
    private  String  precio ;
    private  String  urlImagen ;

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Platillo() {
        super();
    }

    public Platillo(int platilloId, String title, String description, Drawable imagen) {
        super();
        this.title = title;
        this.description = description;
        this.imagen = imagen;
        this.platilloId = platilloId;
    }


    public String getTitle() {
        return title;
    }

    public void setTittle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drawable getImage() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }

    public int getplatilloId(){return platilloId;}

    public void setplatilloId(int platilloId){this.platilloId = platilloId;}


    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

}