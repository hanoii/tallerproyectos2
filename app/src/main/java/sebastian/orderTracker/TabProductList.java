package sebastian.orderTracker;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;

/**
 * Created by Senastian on 30/03/2016.
 */
public class TabProductList extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_product_list, container, false);
        ListView lv = (ListView) v.findViewById(android.R.id.list);

        // Hashmap de ejemplo hardcodeado
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("Alto Guiso", "$15");
        values.put("Paty", "$15");

        // Seteo el adaptar para poblar cada item de la viewlist
        final ProductRowAdapter adapter = new ProductRowAdapter(getContext(),values);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
            }

        });

        return v;
    }
}
