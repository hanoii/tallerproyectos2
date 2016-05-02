package sebastian.orderTracker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Senastian on 02/05/2016.
 */
public class OrderToSubmit {
    private String type;
    private FieldPrecio field_precio;
    private FieldEstado field_estado;
    private FieldFecha field_fecha;
    private FieldItems field_items;
    private FieldCliente field_cliente;

    public OrderToSubmit(double price, String fecha, List<Integer> nodeIds, String clientId) {
        type = "pedido";
        field_precio = new FieldPrecio(price);
        field_estado = new FieldEstado("Solicitado");
        field_fecha = new FieldFecha(fecha);
        field_items = new FieldItems(nodeIds);
        field_cliente = new FieldCliente(clientId);
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

    private class FieldEstado {
        public FieldEstado(String estado) {
            und = estado;
        }
        private String und;
    }

    private class FieldFecha {
        public FieldFecha(String fecha) {
            und = new ArrayList<>();
            Fecha f = new Fecha(fecha);
            und.add(f);
        }
        List<Fecha> und;
    }

    private class Fecha {
        public Fecha(String f) {
            ParFecha fecha = new ParFecha(f);
            value = fecha;
        }
        ParFecha value;
    }

    private class ParFecha {
        public ParFecha(String d) {
            date = d;
        }
        String date;
    }

    private class FieldItems {
        public FieldItems(List<Integer> nodeIds) {
            und = new ArrayList<>();
            Iterator it = nodeIds.iterator();
            while(it.hasNext()) {
                Item item = new Item((Integer)it.next());
                und.add(item);
            }
        }

        List<Item> und;
    }

    private class Item {
        public Item(Integer id) {
            target_id = " (" + id.toString() + ")";
        }
        String target_id;
    }

    private class FieldCliente {
        public FieldCliente(String id) {
            und = new Cliente(id);
        }
        public Cliente und;
    }

    private class Cliente {
        public Cliente(String u) {
            und = u;
        }
        public String und;
    }
}
