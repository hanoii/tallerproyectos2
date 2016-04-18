package sebastian.orderTracker;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;


public class OrderProductItemAdapter extends ProductItemAdapter {


    final OrderProductItemAdapter adapter;

    public OrderProductItemAdapter(Context cont, ArrayList<Product> products) {
        super(cont, products);
        adapter = this;
    }

    private void setMinusButtonOnClickListener(final OrderProductItemHolder product) {
        product.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer total = Integer.parseInt(product.cant.getText().toString()) - 1;
                product.cant.setText(total.toString());
            }
        });
    }

    private void setPlusButtonOnClickListener(final OrderProductItemHolder product) {
        product.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer total = Integer.parseInt(product.cant.getText().toString()) + 1;
                product.cant.setText(total.toString());
            }
        });
    }

    private void setCantEntryOnClickListener(final OrderProductItemHolder product) {
        product.cant.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int cant = Integer.parseInt(s.toString());
                if(cant > 0) {
                    String id = product.getId();
                    Product productoParaAgregar = adapter.getById(id);
                    adapter.addToOrder(productoParaAgregar);
                    product.setCantInteger(cant);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public ProductItemHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_row, null);
        OrderProductItemHolder product = new OrderProductItemHolder(layoutView);
        setPlusButtonOnClickListener(product);
        setMinusButtonOnClickListener(product);
        setCantEntryOnClickListener(product);
        return product;
    }

    @Override
    public void onBindViewHolder(ProductItemHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        OrderProductItemHolder product = (OrderProductItemHolder) holder;
        setClickableAndFocusable(product.minusButton);
        setClickableAndFocusable(product.plusButton);
        Product p = getById(holder.getId());
        String holderCantString = product.cant.getText().toString();
        if( p != null && product.getCantInteger() > 0 && Integer.parseInt(holderCantString) == 0) {
            String cantString = "" + product.getCantInteger();
            product.cant.setText(cantString);
        }
    }


    private void setClickableAndFocusable(View view) {
        view.setClickable(true);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setEnabled(true);
    }


}
