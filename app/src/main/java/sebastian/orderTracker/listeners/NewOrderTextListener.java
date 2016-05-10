package sebastian.orderTracker.listeners;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Pattern;

import sebastian.orderTracker.activities.NewOrderActivity;
import sebastian.orderTracker.dto.NewOrderNavigationArrayData;
import sebastian.orderTracker.entities.Product;

/**
 * Created by matse on 8/5/2016.
 */
public class NewOrderTextListener implements TextWatcher
{

    private NewOrderActivity context;
    private Integer stock;
    private Product product;
    private EditText quantity;
    private String prevText;

    public NewOrderTextListener(Context context, Integer stock, Product product, EditText quantity)
    {
        this.context = (NewOrderActivity) context;
        this.stock = stock;
        this.product = product;
        this.quantity = quantity;
        this.prevText = quantity.getText().toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        if (s.length() != 0)
        {
            NewOrderNavigationArrayData data = this.context.getItemNavigation(this.product);
            if (Integer.valueOf(s.toString()) > this.stock)
            {
                this.quantity.setError("Valor incorrecto");
                this.quantity.setText(this.prevText);
            }
            else
            {
                this.prevText = s.toString();
            }
            if (data == null && this.prevText.compareTo("0") != 0)
            {
                data = new NewOrderNavigationArrayData(this.product, Integer.valueOf(this.prevText));
                this.context.addItemNavigation(data);
            }
            if (data != null)
            {
                data.setQuantity(Integer.valueOf(this.prevText));
            }
        }
    }
}
