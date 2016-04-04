package sebastian.orderTracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Senastian on 01/04/2016.
 */
public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemHolder>{
    private ArrayList<Product> products;
    private Context context;

    public ProductItemAdapter(Context cont, ArrayList<Product> products) {
        this.products = products;
        this.context = cont;
    }

    @Override
    public ProductItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, null);
        ProductItemHolder rcv = new ProductItemHolder(layoutView);
        return rcv;
    }

    public void add(Product p) {
        products.add(p);
    }

    @Override
    public void onBindViewHolder(ProductItemHolder holder, int position) {
        holder.name.setText(products.get(position).getName());
        holder.image.setImageResource(products.get(position).getImgId());
        holder.codigo.setText(products.get(position).getId());
        holder.precio.setText(products.get(position).getPrecio());
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }


}
