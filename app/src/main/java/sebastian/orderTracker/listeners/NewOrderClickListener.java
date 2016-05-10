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

public class NewOrderClickListener implements View.OnClickListener, View.OnLongClickListener
{
    public static final Integer PLUS_BUTTON_TYPE = 0;
    public static final Integer MINUS_BUTTON_TYPE = PLUS_BUTTON_TYPE + 1;
    private EditText quantity;
    private Integer stock;
    private Integer type;
    private NewOrderActivity context;
    private Product product;
    private Integer step;

    public NewOrderClickListener(Context context, Integer type, Integer stock, Product product, EditText quantity)
    {
        this.context = (NewOrderActivity) context;
        this.type = type;
        this.stock = stock;
        this.product = product;
        this.quantity = quantity;
        this.step = new Integer(1);
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
            if (intQuantity + this.step > this.stock)
            {
                intQuantity = this.stock;
                Toast.makeText(this.context, "No hay stock para mas unidades", Toast.LENGTH_SHORT).show();
            }
            else
            {
                intQuantity += this.step;
            }
            if (data == null)
            {
                data = new NewOrderNavigationArrayData(this.product, intQuantity);
                this.context.addItemNavigation(data);
            }
            data.setQuantity(intQuantity);
            this.quantity.setText("" + intQuantity);
        }
        else if (this.type == MINUS_BUTTON_TYPE)
        {
            if (intQuantity - this.step < 0)
            {
                intQuantity = 0;
                Toast.makeText(this.context, "La cantidad debe ser mayor o igual a 0", Toast.LENGTH_SHORT).show();
            }
            else
            {
                intQuantity -= this.step;
            }
            if (data == null)
            {
                data = new NewOrderNavigationArrayData(this.product, intQuantity);
                this.context.addItemNavigation(data);
            }
            data.setQuantity(intQuantity);
            this.quantity.setText("" + intQuantity);
        }
        if (intQuantity == 0 && data != null)
        {
            this.context.removeItemNavigation(data.getProduct());
        }
        this.context.notifyOrderChange();
    }

    @Override
    public boolean onLongClick(View v) {
        NewOrderNavigationArrayData data = this.context.getItemNavigation(this.product);
        if (data != null)
        {
            this.step = 3;
            this.onClick(v);
        }
        return true;
    }
}
