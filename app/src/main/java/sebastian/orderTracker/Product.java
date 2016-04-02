package sebastian.orderTracker;

/**
 * Created by Senastian on 01/04/2016.
 */
public class Product {
    private String name;
    private String marca;
    private Integer codigo;
    private String imgSrc;
    private Double precio;
    private int imgId;

    public Product(String name, String marca, int codigo, String imgSrc, double precio, int imgId) {
        this.name = name;
        this.marca = marca;
        this.codigo = codigo;
        this.imgSrc = imgSrc;
        this.precio = precio;
        this.imgId = imgId;
    }

    public void setName(String name) {
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

    public String getName() {
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

    public String getImgSrc() {
        return imgSrc;
    }

    public Double getPrecio() {
        return precio;
    }


}
