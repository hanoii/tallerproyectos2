package sebastian.orderTracker;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;


public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemHolder>{
    private ArrayList<Product> products;
    private ArrayList<Product> filteredProducts;
    protected Context context;

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
        holder.setProduct(products.get(position));
        holder.name.setText(products.get(position).getName());
        //holder.image.setImageResource(products.get(position).getImgId());
        holder.codigo.setText("codigo: " + products.get(position).getId());
        holder.precio.setText(products.get(position).getPrecio());

        ImageLoader mImageLoader = NetworkManagerSingleton.getInstance(context).getImageLoader();
        holder.image.setImageUrl(products.get(position).getImagen(), mImageLoader);
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

}
