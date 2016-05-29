package sebastian.orderTracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sebastian.orderTracker.CustomJsonArrayRequest;
import sebastian.orderTracker.Global;
import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.R;
import sebastian.orderTracker.adapters.OrderHistoryAdapter;
import sebastian.orderTracker.entities.Client;

public class OrderHistory extends AppCompatActivity {

    RecyclerView rv;
    View v;
    RecyclerView.LayoutManager layoutManager;
    OrderHistoryAdapter orderAdapter;
    int day;
    int month;
    int year;
    boolean useMonthly = false;
    int successRequestCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Global global = (Global)getApplicationContext();
        setContentView(R.layout.activity_order_history);
        layoutManager = new LinearLayoutManager(this);
        rv = (RecyclerView)findViewById(R.id.order_history_orders);
        rv.setLayoutManager(layoutManager);
        day = global.getCurrentDay(); month = global.getCurrentMonth(); year = global.getCurrentYear();
        Toolbar titleToolbar = (Toolbar) findViewById(R.id.order_history_toolbar_title);
        setSupportActionBar(titleToolbar);
        getSupportActionBar().setTitle("Estado de Pedidos");
        final Spinner daySpinner = (Spinner) findViewById(R.id.order_history_day_spinner);
        Switch switchMode = (Switch)findViewById(R.id.order_history_day_month_switch);
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    useMonthly = true;
                    daySpinner.setEnabled(false);
                } else {
                    useMonthly = false;
                    daySpinner.setEnabled(true);
                }
            }
        });

        Spinner monthsSpinner = (Spinner) findViewById(R.id.order_history_months_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array_numerical, android.R.layout.simple_spinner_item);

        monthsSpinner.setAdapter(adapter);
        int monthPosition = adapter.getPosition(Integer.toString(month));
        monthsSpinner.setSelection(monthPosition);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String url =  getUrl(day, month, year);
        monthsSpinner.setOnItemSelectedListener(new requestSuccessListener("month"));
        List<CharSequence> daysArray = new ArrayList<>();
        for(int i = 0; i < global.getMaxiumDay(global.getCurrentMonth(), global.getCurrentYear()); ++i) {
            daysArray.add("" + (i+1));
        }

        ArrayAdapter<CharSequence> dayAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, daysArray);
        daySpinner.setAdapter(dayAdapter);
        int dayPosition = dayAdapter.getPosition(Integer.toString(day));
        daySpinner.setSelection(dayPosition);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setOnItemSelectedListener(new requestSuccessListener("day"));
        orderAdapter = new OrderHistoryAdapter();
        rv.setAdapter(orderAdapter);
    }

    private class requestSuccessListener implements AdapterView.OnItemSelectedListener {
        String type;
        public requestSuccessListener(String t) {
            super();
            type = t;
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final Global global = (Global)getApplicationContext();
            CharSequence selectedDay = (CharSequence)parent.getItemAtPosition(position);
            if(type == "day")
                day = Integer.valueOf(selectedDay.toString());
            else
                month = Integer.valueOf(selectedDay.toString());
            orderAdapter.clearData();
            if(!useMonthly) {
                String date = "" + day + "/" + month + "/" + year;
                CustomJsonArrayRequest jsArrayRequest = new CustomJsonArrayRequest(getUrl(day, month, year), global.getUserPass(), createDailyRequestSuccessListener(orderAdapter, date), createRequestErrorListener());
                NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
            } else {
                int totalDays = global.getMaxiumDay(global.getCurrentMonth(),global.getCurrentYear());
                orderAdapter.clearData();
                for(int i = 1; i <= totalDays; ++i) {
                    String date = "" + i + "/" + month + "/" + year;
                    CustomJsonArrayRequest jsArrayRequest = new CustomJsonArrayRequest(getUrl(i, month, year), global.getUserPass(), createMonthlyRequestSuccessListener(orderAdapter, date), createRequestErrorListener());
                    NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
                }
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    public String getUrl(int day, int month, int year) {
        String date = "" + day + "/";
        if(month < 10) {
            date = date + "0" + month + "/" + year;
        } else {
            date = date + month + "/" + year;
        }
        return "http://dev-taller2.pantheonsite.io/api/pedidos.json?fecha[value][date]=" + date;
    }

    private Response.Listener<JSONArray> createDailyRequestSuccessListener(final OrderHistoryAdapter orderAdapter, final String date) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); ++i) {
                    try {
                        JSONObject jObj = (JSONObject) response.get(i);
                        if (jObj != null) {
                            String auxName = jObj.getString("Cliente");
                            String name = parseName(auxName)[0] + parseName(auxName)[1];
                            String company = jObj.getString("compania");
                            String total = jObj.getString("Precio");
                            String state = jObj.getString("Estado");
                            orderAdapter.addOrder(name, company, total, state, date);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }
        };
    }

    private Response.Listener<JSONArray> createMonthlyRequestSuccessListener(final OrderHistoryAdapter orderAdapter, final String date) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); ++i) {
                    try {
                        JSONObject jObj = (JSONObject) response.get(i);
                        if (jObj != null) {
                            String auxName = jObj.getString("Cliente");
                            String name = parseName(auxName)[0] + parseName(auxName)[1];
                            String company = jObj.getString("compania");
                            String total = jObj.getString("Precio");
                            String state = jObj.getString("Estado");
                            orderAdapter.addOrder(name, company, total, state, date);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                    orderAdapter.notifyDataSetChanged();
            }
        };
    }

    private String[] parseName(String s) {
        String[] sub = s.split("-");
        return sub;
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            };
        };
    }

}
