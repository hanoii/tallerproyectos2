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

public class TabClientList extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_client_list, container, false);
        ListView lv = (ListView) v.findViewById(android.R.id.list);

        HashMap<String, String> values = new HashMap<String, String>();
        values.put("Juan Carlos Pascual", "Avenida Paseo Colón 850");
        values.put("Diego Manuel Silvano", "Cerrito 4332");

       /* final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }*/

        final ClientRowAdapter adapter = new ClientRowAdapter(getContext(),values);
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
