package sebastian.orderTracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import sebastian.orderTracker.adapters.ClientRowAdapter;
import sebastian.orderTracker.entities.Client;


public class TabClientList extends Fragment {

    private ClientRowAdapter clientAdapter;
    private LinearLayoutManager layoutManager;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = createView(savedInstanceState);
        //retains fragment instance across Activity re-creation
        setRetainInstance(true);
    }

    private View createView(Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.tab_client_list, null, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_client_list, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.client_list);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        int day = toCalendarConvention(getArguments().getInt("day"));
        clientAdapter = createAdapter(getContext(), new ArrayList<Client>(), day);
        final Global global = (Global)getActivity().getApplicationContext();
        CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest(getFormattedUrl(day), global.getUserPass(), this.createRequestSuccessListener(clientAdapter), this.createRequestErrorListener());
        NetworkManagerSingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
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
        rv.setAdapter(clientAdapter);
        return v;
    }

    protected ClientRowAdapter createAdapter(Context context, ArrayList<Client> list, int day) {
        return new ClientRowAdapter(context, list, day);
    }


    private Response.Listener<JSONArray> createRequestSuccessListener(final ClientRowAdapter clientAdapter) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i< response.length();++i) {
                    try {
                        JSONObject jObj = (JSONObject)response.get(i);
                        if (jObj != null)
                        {
                            Client c = new Client(jObj);
                            clientAdapter.add(c);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                clientAdapter.notifyDataSetChanged();
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

    // Para Calendar el domingo es 1, Lunes 2, etc
    private int toCalendarConvention(int day) {
        if(day == 6)
            return 1;
        else
            return day + 2;
    }

    protected String getFormattedUrl(int day) {
        String url = "http://dev-taller2.pantheonsite.io/api/clientes.json";
        Calendar currentTime = Calendar.getInstance();
        int dif = day - currentTime.get(Calendar.DAY_OF_WEEK);
        if(day == 1 && currentTime.get(Calendar.DAY_OF_WEEK) != 1) {
            dif = 7+dif; // El domingo esta como primer dia para Calendar, pero nosotros lo queremos como ultimo
        }
        currentTime.add(Calendar.DAY_OF_YEAR, dif);
        String month = "" + (currentTime.get(Calendar.MONTH)+1); // porque para java empieza de 0
        if(month.length() < 2) {
            month = "0" + month;
        }
        String date = (currentTime.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + currentTime.get(Calendar.YEAR));
        Log.d("Date=",date);
        url = url.concat("?" + "fecha" + "[value][date]=" + date);
        return url;
    }

}
