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
import java.util.Iterator;


public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemHolder>{
    private ArrayList<Product> products;
    protected Context context;
    private ArrayList<Product> productsInOrder;
    private ArrayList<Product> productsToShow;

    public ProductItemAdapter(Context cont, ArrayList<Product> products) {
        this.products = products;
        this.productsToShow = this.products;
        this.context = cont;
        this.productsInOrder = new ArrayList<Product>();
    }

    public void setShowOnlyOrderProducts() {
        productsToShow = productsInOrder;
        notifyDataSetChanged();
    }

    public void setShowAllProducts() {
        productsToShow = products;
        notifyDataSetChanged();
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

    public void addToOrder(Product product) {
        productsInOrder.add(product);
    }

    @Override
    public void onBindViewHolder(ProductItemHolder holder, int position) {
        holder.setProduct(productsToShow.get(position));
        holder.name.setText(productsToShow.get(position).getName());
        holder.codigo.setText("codigo: " + productsToShow.get(position).getId());
        holder.precio.setText(productsToShow.get(position).getPrecio());
        ImageLoader mImageLoader = NetworkManagerSingleton.getInstance(context).getImageLoader();
        holder.image.setImageUrl(productsToShow.get(position).getImagen(), mImageLoader);
    }

    @Override
    public int getItemCount() {
        return this.productsToShow.size();
    }


    public Product getById(String id) {
        Iterator<Product> it = productsToShow.iterator();
        while(it.hasNext()) {
            Product p = it.next();
            if(id.compareTo(p.getId()) == 0) {
                return p;
            }
        }
        return null;
    }
}
