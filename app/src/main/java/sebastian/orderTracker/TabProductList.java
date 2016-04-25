package sebastian.orderTracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import sebastian.orderTracker.adapters.ProductItemAdapter;
import sebastian.orderTracker.entities.Product;


public class TabProductList extends Fragment {

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private ProductItemAdapter pAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_product_list, container, false);
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.catalog);
        recyclerView.setHasFixedSize(true);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        pAdapter = new ProductItemAdapter(getContext(), new ArrayList<Product>());

        final Global global = (Global)getActivity().getApplicationContext();

        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/productos.json", global.getUserPass(), this.createRequestSuccessListener(pAdapter), this.createRequestErrorListener());
        String w = jsObjRequest.toString();
        NetworkManagerSingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
        recyclerView.setAdapter(pAdapter);
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
