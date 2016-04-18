package sebastian.orderTracker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Senastian on 18/04/2016.
 */
public class OrderProductItemHolder extends ProductItemHolder {

    public OrderProductItemHolder(View itemView) {
        super(itemView);
        plusButton = (ImageView)itemView.findViewById(R.id.imgMas);
        minusButton = (ImageView)itemView.findViewById(R.id.imgMenos);
        cant = (TextView)itemView.findViewById(R.id.txtCantidadOrder);
    }

    @Override
    public void onClick(View view) {

    }

    public ImageView plusButton;
    public ImageView minusButton;
    public TextView cant;

    public int getCantInteger() {
        return cantInteger;
    }

    public void setCantInteger(int cantInteger) {
        this.cantInteger = cantInteger;
    }

    //TODO Esto debería ir en una nueva clase Order
    private int cantInteger;

}
