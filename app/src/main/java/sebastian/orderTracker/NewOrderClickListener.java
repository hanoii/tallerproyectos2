package sebastian.orderTracker;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

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
    private Context context;
    private String productID;
    private HashMap<String, Integer> chosenProductsMap;

    public NewOrderClickListener(Context context, Integer type, Integer stock, String productID, EditText quantity, HashMap<String, Integer> chosenProductsMap)
    {
        this.context = context;
        this.type = type;
        this.stock = stock;
        this.productID = productID;
        this.quantity = quantity;
        this.chosenProductsMap = chosenProductsMap;
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
        Integer chosenQuantity = this.chosenProductsMap.get(this.productID);
        int intQuantity;
        if (chosenQuantity != null)
        {
            intQuantity = chosenQuantity.intValue();
        }
        else
        {
            intQuantity = Integer.valueOf(this.quantity.getText().toString());
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
                this.chosenProductsMap.put(this.productID, intQuantity);
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
                this.chosenProductsMap.put(this.productID, intQuantity);
                this.quantity.setText("" + intQuantity);
            }
        }
    }
}
