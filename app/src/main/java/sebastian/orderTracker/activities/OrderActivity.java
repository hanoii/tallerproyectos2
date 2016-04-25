package sebastian.orderTracker.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import sebastian.orderTracker.CustomJsonArrayRequest;
import sebastian.orderTracker.Global;
import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.adapters.OrderProductItemAdapter;
import sebastian.orderTracker.entities.Product;
import sebastian.orderTracker.adapters.ProductItemAdapter;
import sebastian.orderTracker.R;

public class OrderActivity extends AppCompatActivity {
    private ListView lv;
    private OrderProductItemAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.listProductOrder);

        ArrayList<Product> your_array_list = new ArrayList<Product>();
        pAdapter = new OrderProductItemAdapter(this, your_array_list );

        final Global global = (Global)getApplicationContext();
        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/productos.json", global.getUserPass(), this.createRequestSuccessListener(pAdapter), this.createRequestErrorListener());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

        Switch switchOrderCatalog = (Switch)findViewById(R.id.switch1);
        switchOrderCatalog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    pAdapter.setShowAllProducts();
                else
                    pAdapter.setShowOnlyOrderProducts();
            }
        });

        rv.setAdapter(pAdapter);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View v = super.onCreateView(parent, name, context, attrs);
        return v;
    }

    private Response.Listener<JSONArray> createRequestSuccessListener(final ProductItemAdapter productAdapter) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i< response.length();++i) {
                    try {
                        Gson gsonProduct = new Gson();
                        Product p;
                        p = gsonProduct.fromJson( response.get(i).toString(), Product.class);
                        productAdapter.add(p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                pAdapter.notifyDataSetChanged();
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }

            ;
        };
    }
}
