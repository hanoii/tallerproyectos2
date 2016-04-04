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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class TabClientList extends ListFragment {

    ClientRowAdapter clientAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_client_list, container, false);
        ListView lv = (ListView) v.findViewById(android.R.id.list);

        final String s = new String();

        clientAdapter = new ClientRowAdapter(getContext(), new ArrayList<Client>());

        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/clientes.json", null, this.createRequestSuccessListener(clientAdapter), this.createRequestErrorListener());
        String w = jsObjRequest.toString();

        NetworkManagerSingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
            }
        });
        EditText edt = (EditText)v.findViewById(R.id.tab_client_list_client_search);
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

    private Response.Listener<JSONArray> createRequestSuccessListener(final ClientRowAdapter clientAdapter) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0; i< response.length();++i) {
                        Client c = new Client((JSONObject)response.get(i));
                        clientAdapter.add(c);
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
