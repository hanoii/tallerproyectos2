package sebastian.orderTracker;


import android.annotation.TargetApi;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sebastian.orderTracker.sincronizacion.product.ProductSyncTask;



public class TabProductList extends Fragment {

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private Context activity;
    private ProductSyncTask task;
    private ArrayList<Product> gaggeredList;
    private ProductItemAdapter pAdapter;
    View v;

    @TargetApi(23)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_product_list, container, false);
        this.v = v;
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.catalog);
        recyclerView.setHasFixedSize(true);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        ArrayList<Product> gaggeredList = getListItemData();
        ProductItemAdapter pAdapter = new ProductItemAdapter(getContext(), gaggeredList);

        ////////////////////
        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/productos.json", null, this.createRequestSuccessListener(pAdapter), this.createRequestErrorListener());
        String w = jsObjRequest.toString();

        NetworkManagerSingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
        //////////////////////

        recyclerView.setAdapter(pAdapter);
        return v;
    }

    private ArrayList<Product> getListItemData(){
        ArrayList<Product> listViewItems = new ArrayList<Product>();
        listViewItems.add(new Product("Alkane","asdas", 11,"asdas", 12.2, R.drawable.example));
        return listViewItems;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void updateProducts(ArrayList<Product> products) {
        gaggeredList.clear();
        for(Product product : products)
            gaggeredList.add(product);
        pAdapter.notifyDataSetChanged();
    }

    public ProductSyncTask getTask() {
        return task;
    }

    public void setTask(ProductSyncTask task) {
        this.task = task;
    }

    private Response.Listener<JSONArray> createRequestSuccessListener(final ProductItemAdapter productAdapter) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0; i< response.length();++i) {
                        Gson gsonProduct = new Gson();
                        Product p = gsonProduct.fromJson(((JSONObject)response.get(i)).toString(), Product.class);
                        productAdapter.add(p);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
