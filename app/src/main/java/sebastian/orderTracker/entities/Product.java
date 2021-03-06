package sebastian.orderTracker.entities;

import android.graphics.Bitmap;

import java.io.Serializable;

import sebastian.orderTracker.R;

/**
 * Created by Senastian on 01/04/2016.
 */
public class Product implements Serializable {

    private String id;
    private String titulo;
    private String precio;
    private String imagen;
    private Descripcion descripcion;
    private String categoria;
    private String stock;

    public String getMarca() {
        return marca;
    }

    private String marca;


    public void setDrawableBitmap(Bitmap drawableBitmap) {
        this.drawableBitmap = drawableBitmap;
    }

    private Bitmap drawableBitmap;

    public class Descripcion {
        private String value;
        private String summary;
        private String format;
        private String safe_value;
        private String safe_summary;

        public String toString() {
            return value + " " + summary + " " + format;
        }
    }
/*
    public Product(String name, String marca, int codigo, String imgSrc, double precio, int imgId, String stock) {
        this.titulo = name + " " + marca;
        this.id = ""+codigo;
        this.imagen = imgSrc;
        this.precio = ""+precio;
        this.stock = "" + stock;
        this.marca = marca;
    }*/

    public boolean equals(Product p) {
        return id.equals(p.getId());
    }
    /*
    public Product(JSONObject jsonProduct){
        try {
            this.titulo = (String)jsonProduct.get("nombre") + " " + (String)jsonProduct.get("apellido");
            this.precio = (String)jsonProduct.get("compania");
            this.adress = (String)((JSONObject)jsonProduct.get("direccion")).get("address");
            this.mobilePhoneNumber = (String)jsonProduct.get("telefono");
            this.staticPhoneNumber = "21321321";
            this.mailAdress = "asdsa@asdads.com";
            this.imgSrc = (String)jsonProduct.get("imagen");
            this.lat = ((JSONObject) jsonProduct.get("direccion")).getDouble("lat");
            this.lng = ((JSONObject) jsonProduct.get("direccion")).getDouble("lng");
        } catch(JSONException e) {
            ArrayList a = null;
            a.add(1);
        }
    }*/

 /*   public void setName(String name) {
        this.name = name;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return name;
    }

    public String getMarca() {
        return marca;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public int getImgId() {
        return imgId;
    }

    public String getImagen() {
        return imgSrc;
    }

    public Double getPrecio() {
        return precio;
    }*/

    public int getImgId() {
        return R.drawable.example;
    }

    public String getName() {
        return titulo;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Descripcion getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(Descripcion descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {return this.categoria;}

    public void setCategoria(String categoria) {this.categoria = categoria;}

    public String getStock() {return this.stock;}

    public void setStock(String stock) {this.stock = stock;}

    @Override
    public boolean equals(Object p)
    {
        if (!(p instanceof Product)) return false;
        if (p == this) return true;
        return (((Product) p).getId().compareTo(this.id) == 0);
    }
}
