package sebastian.orderTracker.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.gson.Gson;

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
import sebastian.orderTracker.adapters.ClientRowAdapter;
import sebastian.orderTracker.adapters.NewOrderELAdapter;
import sebastian.orderTracker.adapters.NewOrderNavigationArrayAdapter;
import sebastian.orderTracker.adapters.SummaryELAdapter;
import sebastian.orderTracker.entities.Client;

/**
 * Created by Senastian on 12/05/2016.
 */
public class Summary extends AppCompatActivity {
    private ExpandableListView eLV;
    private Context context;
    private SummaryELAdapter sAdapter;
    float totalSold;

    List<Client> allClients;
    View v;

    HashMap<String, List<Client>> clients;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        v = getLayoutInflater().inflate(R.layout.tab_client_list, null, false);
        this.context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.eLV = (ExpandableListView) findViewById(R.id.summary_list);
        this.sAdapter = new SummaryELAdapter(this);
        final Global global = (Global)getApplicationContext();
        this.eLV.setAdapter(sAdapter);
        clients = new HashMap<String, List<Client>>();
        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/clientes.json",
                global.getUserPass(), this.createAllClientsRequestSuccessListener(), this.createRequestErrorListener());
        NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        requestOrders();
    }

    private Response.Listener<JSONArray> createAllClientsRequestSuccessListener() {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Client> responseClients = new ArrayList<>();
                for(int i=0; i< response.length();++i) {
                    try {
                        JSONObject jObj = (JSONObject)response.get(i);
                        if (jObj != null)
                        {
                            responseClients.add(new Client(jObj));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                allClients = responseClients;
            }
        };
    }

    private void requestOrders() {
        final Global global = (Global)this.getApplicationContext();
        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest(generateRequestUrl(), global.getUserPass(), this.createRequestSuccessListener(clients), this.createRequestErrorListener());
        NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private String generateRequestUrl() {
        Global global = (Global)getApplicationContext();
        // TODO descomentar y borrar
        //String url =  "http://dev-taller2.pantheonsite.io/api/pedidos.json?fecha[value][date]=" + global.getDate();
        String url =  "http://dev-taller2.pantheonsite.io/api/pedidos.json?fecha[value][date]=" + "02/04/2016";
        return url;
    }

    private Response.Listener<JSONArray> createRequestSuccessListener(final HashMap<String, List<Client>> clients) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Pair<String, String>> clientIds = new ArrayList<>();
                for(int i=0; i< response.length();++i) {
                    try {
                        JSONObject jObj = (JSONObject)response.get(i);
                        if (jObj != null)
                        {
                            clientIds.add(new Pair<String, String>(getId(jObj.getString("Cliente")), jObj.getString("Fecha")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                requestClients(clientIds);
            }
        };
    }

    private void requestClients(List<Pair<String, String>> clientNodeIds) {
        for(Pair<String, String> nId: clientNodeIds) {
            String url = "http://dev-taller2.pantheonsite.io/api/clientes.json?args[0]=" + nId.first;
            Global global = (Global)getApplicationContext();
            String category;
            if(nId.second.compareTo(global.getDate()) == 0) {
                category = getString(R.string.summary_visited_clients_text);
            } else {
                category = getString(R.string.summary_offroute_visited_clients_text);
            }
            CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest(url, global.getUserPass(),
                    this.createClientRequestSuccessListener(category), this.createRequestErrorListener());
            NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        }
    }

    private Response.Listener<JSONArray> createClientRequestSuccessListener(final String category) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i< response.length();++i) {
                    try {
                        JSONObject jObj = (JSONObject)response.get(i);
                        if (jObj != null)
                        {
                            Client c = new Client(jObj);
                            sAdapter.addChild(category, c, allClients.contains(c));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
    }

    private String getId(String nId) {
        String[] sub = nId.split("/");
        String[] sub2 = sub[2].split("\"");
        return sub2[0];
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
