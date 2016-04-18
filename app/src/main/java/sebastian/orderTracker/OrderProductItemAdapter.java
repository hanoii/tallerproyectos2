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
        final OrderProductItemHolder product = new OrderProductItemHolder(layoutView);
        product.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer total = Integer.parseInt(product.cant.getText().toString()) - 1;
                product.cant.setText(total.toString());
            }
        });
        product.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer total = Integer.parseInt(product.cant.getText().toString()) + 1;
                product.cant.setText(total.toString());
            }
        });
        return product;
    }

    @Override
    public void onBindViewHolder(ProductItemHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final OrderProductItemHolder product = (OrderProductItemHolder) holder;
        product.minusButton.setClickable(true);
        product.minusButton.setEnabled(true);
        product.minusButton.setFocusable(true);
        product.minusButton.setFocusableInTouchMode(true);
        product.plusButton.setClickable(true);
        product.plusButton.setEnabled(true);
        product.plusButton.setFocusable(true);
        product.plusButton.setFocusableInTouchMode(true);
    }
}
