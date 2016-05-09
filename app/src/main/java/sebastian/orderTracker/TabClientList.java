package sebastian.orderTracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.Frame;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sebastian.orderTracker.adapters.ClientRowAdapter;
import sebastian.orderTracker.entities.Client;


public class TabClientList extends Fragment implements OnMapReadyCallback {

    private ClientRowAdapter clientAdapter;
    private LinearLayoutManager layoutManager;
    View v;
    RecyclerView rv;
    GoogleMap map;
    private SupportMapFragment mapFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = createView(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        //retains fragment instance across Activity re-creation
    }

    private View createView(Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.tab_client_list, null, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_client_list, container, false);
        rv = (RecyclerView) v.findViewById(R.id.client_list);
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

        setRetainInstance(true);
        addShowHideListener(R.id.map_button, mapFragment);

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
                TextView emptyView = (TextView) v.findViewById(R.id.empty_view);
                if(response.length() > 0 && clientAdapter.size() > 0) {
                    if (map != null)
                        map.moveCamera(CameraUpdateFactory.newLatLng(clientAdapter.getByPosition(0).getDireccionAsLatLng()));
                    clientAdapter.notifyDataSetChanged();
                    drawRoute();
                    rv.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                } else {
                    rv.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private Response.Listener<JSONObject> createMapRequestSuccessListener(final ClientRowAdapter clientAdapter) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray routes = response.getJSONArray("routes");
                    JSONObject route = routes.getJSONObject(0);
                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONArray steps = leg.getJSONArray("steps");
                    JSONObject routeSteps = steps.getJSONObject(0);
                    JSONObject poly = route.getJSONObject("overview_polyline");
                    String Polyline = poly.getString("points");
                    List<LatLng> coordinates = PolyUtil.decode(Polyline);
                    updateMap(coordinates);
                    drawMarkers();
                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public void updateMap(List<LatLng> coordinates) {
        if(clientAdapter.size() > 0)
                map.moveCamera(CameraUpdateFactory.newLatLng(clientAdapter.getByPosition(0).getDireccionAsLatLng()));
        PolylineOptions rectOptions = new PolylineOptions().addAll(coordinates).color(R.color.darkRed);
        map.addPolyline(rectOptions);
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
        //url = url.concat("?" + "fecha" + "[value][date]" + "13/04/2016");
        return url;
    }

    @Override
    public void onMapReady(GoogleMap map) {
       // LatLng pos = new LatLng(20, 20);
        this.map = map;
        map.moveCamera(CameraUpdateFactory.zoomTo(9));
      //  map.moveCamera(CameraUpdateFactory.newLatLng(pos));
        mapFragment.onResume();
    }

    private void addShowHideListener(int buttonId, final Fragment fragment) {
        ImageButton button = (ImageButton)v.findViewById(buttonId);
        final FrameLayout mapView = (FrameLayout)v.findViewById(R.id.map);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ScaleAnimation anim;
                if (fragment.isHidden()) {
                    ft.show(fragment);
                    anim = new ScaleAnimation(1, 1, 0, 1);
                    mapView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.clients_route_height);

                } else {
                    ft.hide(fragment);
                    anim = new ScaleAnimation(1, 1, 1, 0);
                    mapView.getLayoutParams().height = 0;

                }
                anim.setDuration(1000);
                mapView.startAnimation(anim);
                mapView.getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
                mapView.requestLayout();
                ft.commit();
            }
        });
    }

    private void drawRoute() {
        Map<String, String> params = generateMapRequestParams();
        String url = generateMapRequestUrl(params);
        final Global global = (Global)getActivity().getApplicationContext();
        params.putAll(global.getUserPass());
        CustomJsonObjectRequest jsObjRequest = new CustomJsonObjectRequest(url, params, this.createMapRequestSuccessListener(clientAdapter), this.createRequestErrorListener());
        NetworkManagerSingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
     }

    public ArrayList<LatLng> getAllLocations() {
        ArrayList<LatLng> locations = new ArrayList<LatLng>();
        for(int i = 0; i < clientAdapter.size()-1; ++i) {
            locations.add(clientAdapter.getByPosition(i).getDireccionAsLatLng());
        }
        return locations;
    }

    public ArrayList<LatLng> drawMarkers() {
        ArrayList<LatLng> locations = new ArrayList<LatLng>();
        for(int i = 0; i < clientAdapter.size(); ++i) {
            Client c = clientAdapter.getByPosition(i);
            locations.add(c.getDireccionAsLatLng());
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(c.getLat(), c.getLng()))
                    .title(c.getNombre()));
        }
        return locations;
    }

    public Map<String, String> generateMapRequestParams() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("origin", "" + clientAdapter.getByPosition(0).getLat() + "," + clientAdapter.getByPosition(0).getLng());
        params.put("destination", "" + clientAdapter.getByPosition(clientAdapter.size()
                - 1).getLat() + "," + clientAdapter.getByPosition(clientAdapter.size() - 1).getLng());
        return params;
    }

    private String generateMapRequestUrl(Map<String, String> params) {
        String url = "http://maps.googleapis.com/maps/api/directions/json?";
        url = url + "origin=" + params.get("origin") + "&" + "destination=" + params.get("destination");
        url = addWaypoints(url);
        return url;
    }

    private String addWaypoints(String url) {
        ArrayList<LatLng> locations = getAllLocations();
        url = url + "&waypoints=optimize:true";
        if(locations.size() > 2) {
            for (int i = 1; i < locations.size() - 2; ++i) {
                url += "|" + locations.get(i).latitude + "," + locations.get(i).longitude;
            }
        } else {
            url += "|" + locations.get(0).latitude + "," + locations.get(0).longitude + "|" +
                    locations.get(locations.size()-1).latitude + "," + locations.get(locations.size()-1).longitude;
        }
        return url;
    }


}
