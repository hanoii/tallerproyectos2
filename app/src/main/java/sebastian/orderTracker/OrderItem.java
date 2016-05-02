package sebastian.orderTracker;

import java.util.ArrayList;
import java.util.List;

import sebastian.orderTracker.entities.Product;

/**
 * Created by Senastian on 02/05/2016.
 */
public class OrderItem {
    private String type;
    private FieldProducto field_producto;
    private FieldCantidad field_cantidad;
    private FieldPrecio field_precio;


    public OrderItem(Product p, int cant) {
        type = "pedido_item";
        field_producto = new FieldProducto();
        field_precio = new FieldPrecio(Double.parseDouble(p.getPrecio()));
        field_producto.und = new Codigo(Integer.parseInt(p.getId()));
        field_cantidad = new FieldCantidad(cant);
    }
    private class FieldProducto {
        public Codigo und;
    }

    private class Codigo {
        public Codigo(int u) {
            und = u;
        }
        public int und;
    }

    private class Cantidad {
        public Cantidad(int cant) {
            value = cant;
        }
        public int value;
    }

    private class FieldCantidad {
        public FieldCantidad(int cant) {
            und = new ArrayList<Cantidad>();
            Cantidad c = new Cantidad(cant);
            und.add(c);
        }
       public List<Cantidad> und;
    }

    private class Precio {
        public Precio(Double p) {
            value = p;
        }
        public Double value;
    }

    private class FieldPrecio {
        public FieldPrecio(double p) {
            und = new ArrayList<>();
            Precio precio = new Precio(p);
            und.add(precio);
        }
        public List<Precio> und;
    }

}