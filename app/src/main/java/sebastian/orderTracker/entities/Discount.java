package sebastian.orderTracker.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by matse on 14/5/2016.
 */

public class Discount implements Serializable
{
    private String titulo;
    private Integer porcentaje;
    private List<String> categorias;
    private List<Integer> productos;
    private Integer cantidad;
    private Double valor;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Integer porcentaje) {
        this.porcentaje = porcentaje;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public List<Integer> getProductos() {
        return productos;
    }

    public void setProductos(List<Integer> productos) {
        this.productos = productos;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Boolean isGlobal()
    {
        return (this.valor != 0.0 || this.cantidad != 0);
    }
}
