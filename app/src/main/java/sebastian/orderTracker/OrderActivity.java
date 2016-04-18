package sebastian.orderTracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private ListView lv;
    private ProductItemAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // RecyclerView rv = (RecyclerView) findViewById(R.id.catalog);

        RecyclerView rv = (RecyclerView) findViewById(R.id.listProductOrder);

/*        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager); */

        ArrayList<Product> your_array_list = new ArrayList<Product>();
        pAdapter = new OrderProductItemAdapter(this, your_array_list );

        final Global global = (Global)getApplicationContext();
        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/productos.json", global.getUserPass(), this.createRequestSuccessListener(pAdapter), this.createRequestErrorListener());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        rv.setAdapter(pAdapter);

        //rv.setNestedScrollingEnabled(true);


        //lv = (ListView) findViewById(R.id.listProductOrder);
        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        /* List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("foo");
        your_array_list.add("bar");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter); */
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
