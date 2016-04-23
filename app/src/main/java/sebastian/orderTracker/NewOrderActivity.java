package sebastian.orderTracker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class NewOrderActivity extends AppCompatActivity
{

    List<String> listDataHeader;
    HashMap<String, List<Product>> listDataChild;
    HashMap<String, Integer> chosenProductsMap;
    ExpandableListView eLV;
    NewOrderELAdapter nOELA;
    Context context;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_activity);
        this.context = this;
        this.activity = this;
        this.chosenProductsMap = new HashMap<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.new_order_save_button);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenProductsMap = nOELA.getChosenProductsMap();
                Snackbar.make(view, R.string.new_order_correct_save, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fabRevert = (FloatingActionButton) findViewById(R.id.new_order_revert_button);
        fabRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenProductsMap.clear();
                nOELA.notifyDataSetInvalidated();
                Snackbar.make(view, R.string.new_order_correct_revert, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_order_cancel_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.new_order_discard_title)
                        .setMessage(R.string.new_order_discard_message)
                        .setPositiveButton(R.string.new_order_discard_positive_option, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                chosenProductsMap.clear();
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
        });

        this.eLV = (ExpandableListView) findViewById(R.id.new_order_list);
        this.nOELA = new NewOrderELAdapter(this);

        final Global global = (Global)getApplicationContext();
        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/productos.json", global.getUserPass(), this.createRequestSuccessListener(), this.createRequestErrorListener());
        NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private Response.Listener<JSONArray> createRequestSuccessListener() {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                ArrayList<Product> prodList = new ArrayList<>();
                listDataHeader = new ArrayList<>();
                listDataChild = new HashMap<>();
                for(int i=0; i< response.length();++i) {
                    try {
                        Gson gsonProduct = new Gson();
                        Product p;
                        p = gsonProduct.fromJson( response.get(i).toString(), Product.class);
                        prodList.add(p);
                    } catch (Exception e) {
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
