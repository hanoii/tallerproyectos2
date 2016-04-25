package sebastian.orderTracker.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sebastian.orderTracker.entities.Order;
import sebastian.orderTracker.holders.OrderProductItemHolder;
import sebastian.orderTracker.entities.Product;
import sebastian.orderTracker.holders.ProductItemHolder;
import sebastian.orderTracker.R;


public class OrderProductItemAdapter extends ProductItemAdapter {


    final OrderProductItemAdapter adapter;
    Order order;
    boolean userInput;

    public OrderProductItemAdapter(Context cont, ArrayList<Product> products) {
        super(cont, products);
        adapter = this;
        order = new Order();
        userInput = true;
    }

    private void setMinusButtonOnClickListener(final OrderProductItemHolder product) {
        product.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order.hasProduct(product.getId())) {
                    if (order.getProductCant(product.getId()) > 0) {
                        order.removeProducts(product.getId(), 1);
                        if(order.getProductCant(product.getId()) == 0)
                            adapter.removeFromOrder(getById(product.getId()));
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setPlusButtonOnClickListener(final OrderProductItemHolder product) {
        product.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!order.hasProduct(product.getId())) {
                    adapter.addToOrder(getById(product.getId()));
                }
                order.addProducts(getById(product.getId()), 1);
                notifyDataSetChanged();
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
                if(userInput) {
                    int cant = Integer.parseInt(s.toString());
                    String id = product.getId();
                    Product productoParaAgregar = adapter.getById(id);
                    if (order.hasProduct(product.getId())) { // si el producto esta en el pedido
                        if (cant > 0) // si es cantidad positiva le agrego la cantidad
                            order.setProductCant(id, cant);
                        else { // sino entonces es 0 y lo saco del pedido
                            order.removeProduct(id);
                            adapter.removeFromOrder(productoParaAgregar);
                        }
                    } else { // si el producto no esta en el pedido
                        if (cant > 0) {
                            order.addProducts(productoParaAgregar, cant); // lo agrego al pedido
                            adapter.addToOrder(productoParaAgregar); // lo agrego al adapter para que se vea
                            notifyDataSetChanged();
                        }
                    }
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
        userInput = false;
        if( p != null && order.getProductCant(p.getId()) > 0) {
            String cantString = "" + order.getProductCant(product.getId());
            product.cant.setText(cantString);
        } else {
            product.cant.setText("0");
        }
        userInput = true;
    }


    private void setClickableAndFocusable(View view) {
        view.setClickable(true);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setEnabled(true);
    }


}
