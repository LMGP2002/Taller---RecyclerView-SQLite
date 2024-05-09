package com.example.restauranteventas;

public class ProductosItems {
    private Integer id;
    private String nombre;
    private String precio;
    private Integer imagen;
    private Integer cantidad;


    public ProductosItems(Integer id, String nombre, String precio, Integer imagen, Integer cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.cantidad = cantidad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Integer getImagen() {
        return imagen;
    }

    public void setImagen(Integer imagen) {
        this.imagen = imagen;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
