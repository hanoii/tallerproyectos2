package sebastian.orderTracker.listeners;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sebastian.orderTracker.dto.NewOrderNavigationArrayData;
import sebastian.orderTracker.entities.Product;
import sebastian.orderTracker.activities.NewOrderActivity;

/**
 * Created by matse on 22/4/2016.
 */

public class NewOrderClickListener implements View.OnClickListener
{
    public static final Integer PLUS_BUTTON_TYPE = 0;
    public static final Integer MINUS_BUTTON_TYPE = PLUS_BUTTON_TYPE + 1;
    private EditText quantity;
    private Integer stock;
    private Integer type;
    private NewOrderActivity context;
    private Product product;

    public NewOrderClickListener(Context context, Integer type, Integer stock, Product product, EditText quantity)
    {
        this.context = (NewOrderActivity) context;
        this.type = type;
        this.stock = stock;
        this.product = product;
        this.quantity = quantity;
    }

    public EditText getQuantity() {
        return quantity;
    }

    public void setQuantity(EditText quantity) {
        this.quantity = quantity;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getType() { return type; }

    public void setType(Integer type) { this.type = type; }

    @Override
    public void onClick(View v)
    {
        NewOrderNavigationArrayData data = this.context.getItemNavigation(this.product);
        int intQuantity = 0;
        if (data != null)
        {
             intQuantity = data.getQuantity().intValue();
        }
        if (this.type == PLUS_BUTTON_TYPE)
        {
            if (intQuantity + 1 > this.stock)
            {
                Toast.makeText(this.context, "No hay stock para mas unidades", Toast.LENGTH_SHORT).show();
            }
            else
            {
                intQuantity++;
                if (data == null)
                {
                    data = new NewOrderNavigationArrayData(this.product, intQuantity);
                    this.context.addItemNavigation(data);
                }
                data.setQuantity(intQuantity);
                this.quantity.setText("" + intQuantity);
            }
        }
        else if (this.type == MINUS_BUTTON_TYPE)
        {
            if (intQuantity - 1 < 0)
            {
                Toast.makeText(this.context, "La cantidad debe ser mayor o igual a 0", Toast.LENGTH_SHORT).show();
            }
            else
            {
                intQuantity--;
                if (data == null)
                {
                    data = new NewOrderNavigationArrayData(this.product, intQuantity);
                    this.context.addItemNavigation(data);
                }
                data.setQuantity(intQuantity);
                this.quantity.setText("" + intQuantity);
            }
        }
        if (intQuantity == 0)
        {
            this.context.removeItemNavigation(data.getProduct());
        }
        this.context.notifyOrderChange();
    }
}
