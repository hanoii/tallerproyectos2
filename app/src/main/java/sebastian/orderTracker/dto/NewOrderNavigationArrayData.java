package sebastian.orderTracker.dto;

import sebastian.orderTracker.entities.Product;

/**
 * Created by matse on 24/4/2016.
 */
public class NewOrderNavigationArrayData
{
    private Product product;
    private Integer quantity;

    public NewOrderNavigationArrayData(Product product, Integer quantity)
    {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
