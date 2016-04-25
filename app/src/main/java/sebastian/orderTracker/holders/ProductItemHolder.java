package sebastian.orderTracker.holders;

/**
 * Created by Senastian on 01/04/2016.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;


import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import sebastian.orderTracker.R;
import sebastian.orderTracker.activities.ProductDetailActivity;
import sebastian.orderTracker.entities.Product;

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

    public String getId() {
        return ProductItemHolder.stripNonDigits(codigo.getText().toString());
    }

    // TODO mover a utils
    private static String stripNonDigits(
            final CharSequence input /* inspired by seh's comment */){
        final StringBuilder sb = new StringBuilder(
                input.length() /* also inspired by seh's comment */);
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58){
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
