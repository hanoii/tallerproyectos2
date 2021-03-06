package sebastian.orderTracker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sebastian.orderTracker.entities.Client;

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
    private FieldCantidadPedido field_cantidad_pedido;

    public OrderToSubmit(double price, String fecha, List<Integer> nodeIds, String clientId, int cant) {
        type = "pedido";
        field_precio = new FieldPrecio(price);
        field_estado = new FieldEstado("Solicitado");
        field_fecha = new FieldFecha(fecha);
        field_items = new FieldItems(nodeIds);
        field_cliente = new FieldCliente(Integer.valueOf(clientId));
        field_cantidad_pedido = new FieldCantidadPedido(cant);
    }

    private class Precio {
        public Precio(Double p) {
            value = p;
        }
        public Double value;
    }

    private class CantidadPedido {
        public CantidadPedido(int c) { value = c;}
        public int value;
    }

    private class FieldPrecio {
        public FieldPrecio(double p) {
            und = new ArrayList<>();
            Precio precio = new Precio(p);
            und.add(precio);
        }
        public List<Precio> und;
    }

    private class FieldCantidadPedido {
        public FieldCantidadPedido(int c) {
            und = new ArrayList<>();
            CantidadPedido cant = new CantidadPedido(c);
            und.add(cant);
        }
        public List<CantidadPedido> und;
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
        public FieldCliente(int clientId) {
            und = new ArrayList<>();
            Cliente c = new Cliente(clientId);
            und.add(c);
        }
        List<Cliente> und;
    }

    private class Cliente {
        public Cliente(Integer u) {
            target_id = " (" + u.toString() + ")";
        }
        String target_id;
    }
}
