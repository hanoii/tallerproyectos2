package sebastian.orderTracker;

/**
 * Created by Senastian on 01/04/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;


        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class ProductItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    public NetworkImageView image;
    public TextView codigo;
    public TextView precio;
    public TextView descripcion;

    public void setProduct(Product product) {
        this.product = product;
    }

    private Product product;


    public ProductItemHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        name = (TextView) itemView.findViewById(R.id.product_name);
        image = (NetworkImageView) itemView.findViewById(R.id.product_image);
        codigo = (TextView) itemView.findViewById(R.id.product_code);
        precio = (TextView) itemView.findViewById(R.id.product_price);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
        Gson gs = new Gson();
        String productString = gs.toJson(product);
        intent.putExtra(view.getContext().getString(R.string.serializedProductKey), productString);
        view.getContext().startActivity(intent);
    }
}
