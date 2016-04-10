package sebastian.orderTracker;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.w3c.dom.Text;
//TODO hacer que no scrolee la activity al scrollear el mapa
public class ClientDetails extends AppCompatActivity implements OnMapReadyCallback {

    private Client c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String clientString = getIntent().getExtras().getString(getString(R.string.serializedClientKey));
        //String clientString = getIntent().getExtras().getString("a");
        Gson gson = new Gson();
        Client client = gson.fromJson(clientString, Client.class);
        c = client;
        TextView nombre = (TextView)findViewById(R.id.client_details_name);
        TextView compania = (TextView)findViewById(R.id.client_details_company);
        TextView address = (TextView)findViewById(R.id.client_details_address);
        TextView telefonoMovil = (TextView)findViewById(R.id.client_details_mobile_phone_number);
        TextView telefonoFijo = (TextView)findViewById(R.id.client_details_static_phone_number);
        TextView mail = (TextView)findViewById(R.id.client_details_mail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.client_map_layout);
        mapFragment.getMapAsync(this);

        //TODO agregar manejo de excepcion
        //TODO agregar un viewholder para limpar esto
        nombre.append(client.getName());
        compania.append(client.getCompany());
        address.append(client.getAdress());
        telefonoMovil.append(client.getMobilePhoneNumber());
        telefonoFijo.append(client.getStaticPhoneNumber());
        mail.append(client.getMailAdress());

        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        ImageLoader mImageLoader = NetworkManagerSingleton.getInstance(this).getImageLoader();
        final ImageView mImageView = new ImageView(this);
        mImageLoader.get(c.getImgSrc(), new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mImageView.setImageResource(R.drawable.err_image);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    mImageView.setImageBitmap(response.getBitmap());
                    toolbarLayout.setBackground(mImageView.getDrawable());
                }
            }
        });

        ImageView transparentImageView = (ImageView)findViewById(R.id.transparent_imageview);
        final NestedScrollView mainScrollView = (NestedScrollView)findViewById(R.id.client_details_layout);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap map) {
        LatLng pos = new LatLng(c.getLat(), c.getLng());
        map.moveCamera(CameraUpdateFactory.newLatLng(pos));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(c.getLat(), c.getLng()))
                .title(c.getName()));
    }



}
