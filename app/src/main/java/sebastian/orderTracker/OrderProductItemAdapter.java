package sebastian.orderTracker;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;


public class OrderProductItemAdapter extends ProductItemAdapter {

    public OrderProductItemAdapter(Context cont, ArrayList<Product> products) {
        super(cont, products);
    }

    @Override
    public ProductItemHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_row, null);
        ProductItemHolder rcv = new ProductItemHolder(layoutView);
        return rcv;
    }

    public void setListenerCantidad(View v, final TextView tv, final int incremento) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = Integer.getInteger(tv.getText().toString());
                tv.setText("" + (cantidad + incremento));
            }
        });
    }
}
