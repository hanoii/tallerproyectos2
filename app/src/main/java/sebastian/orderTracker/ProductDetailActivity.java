package sebastian.orderTracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import sebastian.orderTracker.sincronizacion.HttpManager;
import sebastian.orderTracker.sincronizacion.product.ProductSyncTask;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((TextView)findViewById(R.id.txt_nombre_producto)).setText(getIntent().getExtras().getString("titulo"));

        final ProductDetailActivity activity = this;

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
