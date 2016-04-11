package sebastian.orderTracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import sebastian.orderTracker.sincronizacion.HttpManager;
import sebastian.orderTracker.sincronizacion.product.ProductSyncTask;

public class ProductDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String productString = getIntent().getExtras().getString(getString(R.string.serializedProductKey));
        Gson gson = new Gson();
        Product p = gson.fromJson(productString, Product.class);

        ((TextView)findViewById(R.id.txt_nombre_producto)).setText(p.getName());
        ((TextView)findViewById(R.id.txt_codigo_producto)).setText(p.getId());
        ((TextView)findViewById(R.id.txt_precio_producto)).setText(p.getPrecio());


        final ProductDetailActivity activity = this;

        NetworkImageView portrait = (NetworkImageView)findViewById(R.id.product_details_portrait);
        ImageLoader il = NetworkManagerSingleton.getInstance(this).getImageLoader();
        portrait.setImageUrl(p.getImagen(), il);
//////////////////////////////
        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.product_detail_toolbar_layout);
        ImageLoader mImageLoader = NetworkManagerSingleton.getInstance(this).getImageLoader();
        final ImageView mImageView = new ImageView(this);
        mImageLoader.get(p.getImagen(),new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mImageView.setImageResource(R.drawable.err_image);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    mImageView.setImageBitmap(response.getBitmap());
                  //  toolbarLayout.setBackground(mImageView.getDrawable());
                }
            }
        });

        ////////////////////////////////////////


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab!=null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Snackbar.make(view, "Producto comprado", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (Exception e) {
                        Snackbar.make(view, "Error", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
    }

    public void setDetailText(String text) {
        ((TextView)this.findViewById(R.id.txt_descripcion_producto)).setText(text);
    }
}
