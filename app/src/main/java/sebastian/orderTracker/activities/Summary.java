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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.gms.common.api.BooleanResult;
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
    int totalPackagesSold;

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
        allClients = new ArrayList<>();
        clients = new HashMap<String, List<Client>>();
        String url = "http://dev-taller2.pantheonsite.io/api/clientes.json" + "?" + "fecha" + "[value][date]=" + global.getDate();
        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest(url,
                global.getUserPass(), this.createAllClientsRequestSuccessListener(), this.createAllClientsRequestErrorListener());
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
        String url =  "http://dev-taller2.pantheonsite.io/api/pedidos.json?fecha[value][date]=" + global.getDate();
        //String url =  "http://dev-taller2.pantheonsite.io/api/pedidos.json?fecha[value][date]=" + "02/04/2016";
        return url;
    }

    private Response.Listener<JSONArray> createRequestSuccessListener(final HashMap<String, List<Client>> clients) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Boolean> isAsigned = new ArrayList<>();
                List<String> clientIds = new ArrayList<>();
                for(int i=0; i< response.length();++i) {
                    try {
                        JSONObject jObj = (JSONObject)response.get(i);
                        if (jObj != null)
                        {
                            clientIds.add(jObj.getString("cliente_nid"));
                            String precio = jObj.getString("Precio");
                            precio = precio.replace("$", "");
                            if(jObj.get("Vendedor").equals(((Global)getApplicationContext()).getUsername())) {
                                isAsigned.add(true);
                            } else {
                                isAsigned.add(false);
                            }
                            totalSold += Double.valueOf(precio);
                            try {
                                totalPackagesSold += Double.valueOf(jObj.getString("cantidad"));
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                TextView total = (TextView)findViewById(R.id.total_money);
                TextView totalPackages = (TextView)findViewById(R.id.total_packages);
                total.setText("Monto total vendido: " + "$" + totalSold);
                totalPackages.setText("Cantidad de Bultos vendidos:" + totalPackagesSold);
                requestClients(clientIds, isAsigned);
            }
        };
    }


    private void requestClients(List<String> clientNodeIds, List<Boolean> clientIsAsigned) {
        for(int i=0; i < clientNodeIds.size(); ++i) {
            String nId = clientNodeIds.get(i);
            String url = "http://dev-taller2.pantheonsite.io/api/clientes.json?args[0]=" + nId;
            Global global = (Global)getApplicationContext();
            String category = getString(R.string.summary_offroute_visited_clients_text);
            for(Client c : allClients) {
                if(nId.equals(c.getId()))
                    category = getString(R.string.summary_visited_clients_text);
            }
            CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest(url, global.getUserPass(),
                    this.createClientRequestSuccessListener(category, clientIsAsigned.get(i)), this.createRequestErrorListener());
            NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        }

    }

    private Response.Listener<JSONArray> createClientRequestSuccessListener(final String category, final boolean isMine) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i< response.length();++i) {
                    try {
                        JSONObject jObj = (JSONObject)response.get(i);
                        if (jObj != null)
                        {
                            Client c = new Client(jObj);
                            sAdapter.addChild(category, c, isMine);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            sAdapter.notifyDataSetChanged();
            }
        };
    }
/*
    private String getId(String nId) {
        String[] sub = nId.split("-");
        return sub[1];
    }*/

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            };
        };
    }

    private Response.ErrorListener createAllClientsRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            };
        };
    }


}
