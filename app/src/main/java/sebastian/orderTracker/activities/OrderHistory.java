package sebastian.orderTracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sebastian.orderTracker.CustomJsonArrayRequest;
import sebastian.orderTracker.Global;
import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.R;
import sebastian.orderTracker.adapters.OrderHistoryAdapter;
import sebastian.orderTracker.entities.Client;

public class OrderHistory extends AppCompatActivity {

    RecyclerView rv;
    View v;
    RecyclerView.LayoutManager layoutManager;
    OrderHistoryAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        layoutManager = new LinearLayoutManager(this);
        rv = (RecyclerView)findViewById(R.id.order_history_orders);
        rv.setLayoutManager(layoutManager);

        Toolbar titleToolbar = (Toolbar) findViewById(R.id.order_history_toolbar_title);
        setSupportActionBar(titleToolbar);
        getSupportActionBar().setTitle("Estado de Pedidos");

        orderAdapter = new OrderHistoryAdapter();
        final Global global = (Global)getApplicationContext();
        final String date = "16/05/2016";
        String url =  "http://dev-taller2.pantheonsite.io/api/pedidos.json?fecha[value][date]=" + date;

        CustomJsonArrayRequest jsArrayRequest = new CustomJsonArrayRequest(url, global.getUserPass(), this.createRequestSuccessListener(orderAdapter, date), this.createRequestErrorListener());
        NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
        rv.setAdapter(orderAdapter);

    }

    private Response.Listener<JSONArray> createRequestSuccessListener(final OrderHistoryAdapter orderAdapter, final String date) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i< response.length();++i) {
                    try {
                        JSONObject jObj = (JSONObject)response.get(i);
                        if (jObj != null)
                        {
                            String auxName = jObj.getString("Cliente");
                            String name = parseName(auxName)[0] + parseName(auxName)[1];
                            // company = jObj.getString("Compamnia");
                            String total = jObj.getString("Precio");
                            String state = jObj.getString("Estado");
                            orderAdapter.addOrder(name, "sarasa", total, state, date);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }
        };
    }

    private String[] parseName(String s) {
        String[] sub = s.split(">");
        String[] sub2 = sub[1].split("-");
        return sub2;
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            };
        };
    }
}
