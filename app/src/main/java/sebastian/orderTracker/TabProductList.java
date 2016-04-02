package sebastian.orderTracker;


import android.annotation.TargetApi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Senastian on 30/03/2016.
 */
public class TabProductList extends Fragment {

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;

    @TargetApi(23)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_product_list, container, false);


        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.catalog);
        recyclerView.setHasFixedSize(true);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);



        //ArrayList<Product> gaggeredList = getListItemData();
        ArrayList<Product> gaggeredList = getListItemData();


        ProductItemAdapter pAdapter = new ProductItemAdapter(getContext(), gaggeredList);
        recyclerView.setAdapter(pAdapter);
        return v;
    }

    private ArrayList<Product> getListItemData(){
        ArrayList<Product> listViewItems = new ArrayList<Product>();
        listViewItems.add(new Product("Alkane","asdas", 11,"asdas", 12.2, R.drawable.example));
        listViewItems.add(new Product("Alsadsadqwee","aqweqwes", 13,"asdas", 11.0, R.drawable.example));
        listViewItems.add(new Product("321354ne","azcxzcx", 16,"asdas", 11.2,R.drawable.example));
        listViewItems.add(new Product("321354ne","azcxzcx", 16,"asdas", 11.2,R.drawable.ordertracker));
        listViewItems.add(new Product("321354ne","azcxzcx", 16,"asdas", 11.2, R.drawable.example));
        return listViewItems;
    }
}
