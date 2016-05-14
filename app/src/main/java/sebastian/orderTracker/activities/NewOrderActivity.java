package sebastian.orderTracker.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import sebastian.orderTracker.CustomJsonArrayRequest;
import sebastian.orderTracker.CustomJsonObjectRequest;
import sebastian.orderTracker.Global;
import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.OrderItem;
import sebastian.orderTracker.OrderToSubmit;
import sebastian.orderTracker.adapters.NewOrderELAdapter;
import sebastian.orderTracker.adapters.NewOrderNavigationArrayAdapter;
import sebastian.orderTracker.dto.NewOrderNavigationArrayData;
import sebastian.orderTracker.entities.Client;
import sebastian.orderTracker.entities.Discount;
import sebastian.orderTracker.entities.Product;
import sebastian.orderTracker.R;

public class NewOrderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private List<String> listDataHeader;
    private HashMap<String, List<Product>> listDataChild;
    private ArrayList<NewOrderNavigationArrayData> chosenProductsList;
    private ArrayList<Discount> discounts;
    private ExpandableListView eLV;
    private NewOrderELAdapter nOELA;
    private NewOrderNavigationArrayAdapter nONAA;
    private Context context;
    private Activity activity;
    private ListView list;

    private Client c;
    private List<Integer> nodeIds;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_activity);
        this.context = this;
        this.activity = this;
        this.chosenProductsList = new ArrayList<>();
        this.discounts = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String clientString = getIntent().getExtras().getString(getString(R.string.serializedClientKey));;
        Gson gson = new Gson();
        Client client = gson.fromJson(clientString, Client.class);
        c = client;

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.new_order_save_button);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarItemsPedido();
                Snackbar.make(view, R.string.new_order_correct_save, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fabRevert = (FloatingActionButton) findViewById(R.id.new_order_revert_button);
        fabRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenProductsList.clear();
                nOELA.notifyDataSetInvalidated();
                nONAA.notifyDataSetInvalidated();
                notifyOrderChange();
                Snackbar.make(view, R.string.new_order_correct_revert, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_order_cancel_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "";
                for (Discount d : discounts)
                {
                    if (d.isGlobal())
                    {
                        message += d.getTitulo()  + "\n";
                    }
                }
                if (message.length() == 0)
                {
                    message = "No hay descuentos para el periodo actual";
                }
                new AlertDialog.Builder(context)
                        .setTitle("Descuentos globales")
                        .setMessage(message)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /*chosenProductsList.clear();
                                activity.finish();*/
                            }
                        })
                        /*.setNegativeButton(R.string.new_order_discard_negative_option, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })*/
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });

        this.eLV = (ExpandableListView) findViewById(R.id.new_order_list);
        this.nOELA = new NewOrderELAdapter(this);

        final Global global = (Global)getApplicationContext();
        SimpleDateFormat sDF = new SimpleDateFormat("dd/MM/yyyy");
        String date = sDF.format(Calendar.getInstance().getTime());
        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/descuentos.json?fecha[value][date]=" + date, global.getUserPass(), this.createRequestDiscountSuccessListener(), this.createRequestErrorListener());
        NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

        jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/productos.json", global.getUserPass(), this.createRequestSuccessListener(), this.createRequestErrorListener());
        NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.new_order_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.new_order_navigation);
        navigationView.setNavigationItemSelectedListener(this);

        this.list = (ListView) findViewById(R.id.new_order_navigation_list);

        nONAA = new NewOrderNavigationArrayAdapter(context, R.id.new_order_price, this.chosenProductsList);
        list.setAdapter(nONAA);
    }

    private Response.Listener<JSONArray> createRequestDiscountSuccessListener()
    {
        return new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response)
            {
                for(int i=0; i< response.length();++i)
                {
                    try
                    {
                        Gson gsonDiscount = new Gson();
                        Discount discount;
                        discount = gsonDiscount.fromJson(response.get(i).toString(), Discount.class);
                        discounts.add(discount);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private Response.Listener<JSONArray> createRequestSuccessListener()
    {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                ArrayList<Product> prodList = new ArrayList<>();
                listDataHeader = new ArrayList<>();
                listDataChild = new HashMap<>();
                for(int i=0; i< response.length();++i)
                {
                    try
                    {
                        Gson gsonProduct = new Gson();
                        Product p;
                        p = gsonProduct.fromJson( response.get(i).toString(), Product.class);
                        prodList.add(p);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                Collections.sort(prodList, new Comparator<Product>() {
                    @Override
                    public int compare(Product lhs, Product rhs) {
                        return (lhs.getCategoria().compareTo(rhs.getCategoria()) == 0 ? Integer.valueOf(lhs.getId()).compareTo(Integer.valueOf(rhs.getId())) : lhs.getCategoria().compareTo(rhs.getCategoria()));
                    }
                });
                String lastCategory = "";
                ArrayList<Product> tempProdList = null;
                for (Product product : prodList)
                {
                    if (product.getCategoria().compareTo(lastCategory) != 0)
                    {
                        if (lastCategory != "")
                        {
                            listDataChild.put(lastCategory, tempProdList);
                        }
                        lastCategory = product.getCategoria();
                        listDataHeader.add(lastCategory);
                        tempProdList = new ArrayList<>();
                    }
                    tempProdList.add(product);
                }
                listDataChild.put(lastCategory, tempProdList);
                nOELA.setData(listDataHeader, listDataChild);
                eLV.setAdapter(nOELA);
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener()
    {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        };
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.new_order_drawer);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (this.nONAA.getCount() > 0)
        {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.new_order_discard_title)
                    .setMessage(R.string.new_order_discard_message)
                    .setPositiveButton(R.string.new_order_discard_positive_option, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            chosenProductsList.clear();
                            activity.finish();
                        }
                    })
                    .setNegativeButton(R.string.new_order_discard_negative_option, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.prueba, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        /*
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.new_order_drawer);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }

    public void notifyOrderChange()
    {
        this.nOELA.notifyDataSetChanged();
        this.nONAA.notifyDataSetChanged();
        TextView total = (TextView) this.findViewById(R.id.new_order_total);
        Double totalQuantity = this.calculatePrice();
        total.setText("$" + Math.floor(totalQuantity));
    }

    public NewOrderNavigationArrayData getItemNavigation(Product product)
    {
        for(int i = 0; i < this.nONAA.getCount(); i++)
        {
            NewOrderNavigationArrayData data = (NewOrderNavigationArrayData) this.nONAA.getItem(i);
            if (data.getProduct().equals(product))
            {
                return data;
            }
        }
        return null;
    }

    public void addItemNavigation(NewOrderNavigationArrayData data)
    {
        this.nONAA.add(data);
    }

    public void removeItemNavigation(Product product)
    {
        for(int i = 0; i < this.nONAA.getCount(); i++)
        {
            NewOrderNavigationArrayData data = (NewOrderNavigationArrayData) this.nONAA.getItem(i);
            if (data.getProduct().equals(product))
            {
                this.nONAA.remove(data);
                break;
            }
        }
    }

    private void enviarItemsPedido() {
        List<OrderItem> items = new ArrayList<OrderItem>();
        if(nodeIds == null) {
            nodeIds = new ArrayList<>();
        }
        String url = "http://dev-taller2.pantheonsite.io/api/node.json";
        for(int i = 0; i < chosenProductsList.size(); ++i) {
            OrderItem item = new OrderItem(chosenProductsList.get(i).getProduct(), chosenProductsList.get(i).getQuantity());
            Gson gson = new Gson();
            String jsonString = gson.toJson(item);
            JSONObject json = null;
            try {
                json = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final Global global = (Global)getApplicationContext();
            CustomJsonObjectRequest jsObjRequest = new CustomJsonObjectRequest(Request.Method.POST, url, global.getUserPass(), json, this.createItemSubmitSuccessListener(), this.createRequestErrorListener());
            NetworkManagerSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        }
    }

    private Response.Listener<JSONObject> createItemSubmitSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null)
                {
                    try {
                        nodeIds.add(Integer.parseInt((String)response.get("nid")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(nodeIds.size() == chosenProductsList.size()) {
                        enviarPedido();
                    }
                }

            }
        };
    }

    private double calculatePrice() {
        Double plainPrice = 0.0;
        Double adjustedPrice = 0.0;
        Integer totalQuantities = 0;
        Integer porcentajeGlobal = 0;
        for(int i = 0; i < chosenProductsList.size(); ++i) {
            Double p = Double.parseDouble(chosenProductsList.get(i).getProduct().getPrecio());
            Double cant = chosenProductsList.get(i).getQuantity().doubleValue();
            plainPrice += p * cant;
            totalQuantities += chosenProductsList.get(i).getQuantity();
            Integer porcentaje = 0;
            Discount discount = getDiscountForProduct(chosenProductsList.get(i).getProduct());
            if (discount != null)
            {
                porcentaje += discount.getPorcentaje();
            }
            discount = getDiscountForCategory(chosenProductsList.get(i).getProduct().getCategoria());
            if (discount != null)
            {
                porcentaje += discount.getPorcentaje();
            }
            adjustedPrice += (p*cant)*(100 - porcentaje)/100;
        }

        for (Discount d : discounts)
        {
            if (d.isGlobal())
            {
                if (d.getValor() != 0.0 && plainPrice >= d.getValor())
                {
                    porcentajeGlobal += d.getPorcentaje();
                }
                if (d.getCantidad() != 0 && totalQuantities >= d.getCantidad())
                {
                    porcentajeGlobal += d.getPorcentaje();
                }
            }
        }
        adjustedPrice -= plainPrice * porcentajeGlobal/100;
        return adjustedPrice;
    }

    private void enviarPedido() {
        Gson gson = new Gson();
        Calendar currentTime = Calendar.getInstance();
        Integer day = currentTime.get(Calendar.DAY_OF_MONTH);
        Integer month = currentTime.get(Calendar.MONTH);
        String daystr = day.toString();
        String monthstr = month.toString();
        if(day < 10) {
            daystr = "0"+daystr;
        }
        if(month < 10) {
            monthstr = "0"+monthstr;
        }
        String date = "" + daystr + "/" +
                monthstr + "/" + currentTime.get(Calendar.YEAR);
        OrderToSubmit order = new OrderToSubmit(calculatePrice(), date, nodeIds, c.getId());
        String jsonString = gson.toJson(order);
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Global global = (Global)getApplicationContext();
        String url = "http://dev-taller2.pantheonsite.io/api/node.json";
        CustomJsonObjectRequest jsObjRequest = new CustomJsonObjectRequest(Request.Method.POST, url, global.getUserPass(), json, this.createOrderSubmitSuccessListener(), this.createRequestErrorListener());
        NetworkManagerSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void actualizarStocks()
    {
        final Global global = (Global)getApplicationContext();
        for (NewOrderNavigationArrayData data : this.chosenProductsList)
        {
            Gson gson = new Gson();
            data.getProduct().setStock("" + (Integer.valueOf(data.getProduct().getStock()) - data.getQuantity()));
            String jsonString = gson.toJson(data.getProduct());
            JSONObject json = null;
            try {
                json = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomJsonObjectRequest jsObjRequest = new CustomJsonObjectRequest(Request.Method.POST,
                    "http://dev-taller2.pantheonsite.io/api/node/" + data.getProduct().getId(),
                    global.getUserPass(),
                    json,
                    this.createStockSubmitSuccessListener(),
                    this.createRequestErrorListener());
            //NetworkManagerSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        }
    }

    private Response.Listener<JSONObject> createOrderSubmitSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null)
                {
                    actualizarStocks();
                }

            }
        };
    }

    private Response.Listener<JSONObject> createStockSubmitSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null)
                {
                    int i = 6;
                }

            }
        };
    }

    public Discount getDiscountForCategory(String category)
    {
        if (category == null) return null;
        for(Discount d : this.discounts)
        {
            for (String categoria : d.getCategorias())
            {
                if (category.compareTo(categoria) == 0)
                {
                    return d;
                }
            }
        }
        return null;
    }

    public Discount getDiscountForProduct(Product product)
    {
        if (product == null) return null;
        for(Discount d : this.discounts)
        {
            for (Integer productId : d.getProductos())
            {
                if (product.getId().compareTo(productId.toString()) == 0)
                {
                    return d;
                }
            }
        }
        return null;
    }
}
