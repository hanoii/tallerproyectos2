package sebastian.orderTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
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

        Client client = new Client("Juan Carlos Pascual", "Ford Argentina S.A.","Paseo Colon 850", "sarasa", "1563533357", "46887149", "sarasa@gmail.com");
        Client c2 = new Client("Carlos Alberto Calvo", "Ford Argentina S.A.","Independencia 2313" ,"sarasa", "1563533357", "46887149", "sarasa@gmail.com");

        ArrayList<Client> alc = new ArrayList<Client>();
        alc.add(client);
        alc.add(c2);

       /* final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }*/

        final ClientRowAdapter clientAdapter = new ClientRowAdapter(getContext(),alc);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                /*view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        */
                Log.d("test", "clicked");
                Intent intent = new Intent(getContext(), ClientDetails.class);
                startActivity(intent);
            }
        });
        EditText edt = (EditText)v.findViewById(R.id.tab_client_list_client_search);
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text [" + s + "]");
                clientAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lv.setAdapter(clientAdapter);


        return v;
    }
}
