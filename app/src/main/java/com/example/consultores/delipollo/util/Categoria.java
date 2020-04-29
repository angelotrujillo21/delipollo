package com.example.consultores.delipollo.util;

public class Categoria {

    private String title;
    private int categoryId;
    private String description;
    private String imagen;
    private String UrlImagen;

    public String getUrlImagen() {
        return UrlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        UrlImagen = urlImagen;
    }

    public Categoria() {
        super();
    }

    public Categoria(int categoryId, String title, String description, String imagen) {
        super();
        this.title = title;
        this.description = description;
        this.imagen = imagen;
        this.categoryId = categoryId;
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

    public String getImage() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCategoryId(){return categoryId;}

    public void setCategoryId(int categoryId){this.categoryId = categoryId;}

}