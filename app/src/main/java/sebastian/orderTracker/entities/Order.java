package sebastian.orderTracker.entities;

import java.util.HashMap;

/**
 * Created by Senastian on 23/04/2016.
 */
public class Order {
    HashMap<String, Product> products;
    HashMap<String, Integer> quantity;
    int orderId;

    public Order() {
        products = new HashMap<String, Product>();
        quantity = new HashMap<String, Integer>();
    }

    public void addProducts(Product p, int cant) {
        if(products.containsKey(p.getId())) {
            quantity.put(p.getId(), quantity.get(p.getId()) + cant);
        } else {
            products.put(p.getId(), p);
            quantity.put(p.getId(), cant);
        }
    }

    public void setProductCant(String productId, int cant) {
        quantity.put(productId, cant);
    }

    public boolean hasProduct(String productId) {
        return products.containsKey(productId);
    }

    public void removeProducts(String productId, int cant) {
        if(quantity.get(productId) <= cant) {
            quantity.remove(productId);
            products.remove(productId);
        } else {
            quantity.put(productId, quantity.get(productId) - cant);
        }
    }

    public void removeProduct(String productId) {
        products.remove(productId);
        quantity.remove(productId);
    }

    public int getProductCant(String productId) {
        if(products.containsKey(productId))
            return quantity.get(productId);
        else
            return 0;
    }
}
