package sebastian.orderTracker.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;

import sebastian.orderTracker.CustomJsonArrayRequest;

import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.R;
import sebastian.orderTracker.entities.Client;
import sebastian.orderTracker.Global;

//TODO hacer que no scrolee la activity al scrollear el mapa
public class ClientDetails extends AppCompatActivity implements OnMapReadyCallback {

    private Client c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String clientString = getIntent().getExtras().getString(getString(R.string.serializedClientKey));;
        Gson gson = new Gson();
        Client client = gson.fromJson(clientString, Client.class);
        c = client;
        TextView nombre = (TextView)findViewById(R.id.client_details_name);
        TextView compania = (TextView)findViewById(R.id.client_details_company);
        TextView address = (TextView)findViewById(R.id.client_details_address);
        TextView telefonoMovil = (TextView)findViewById(R.id.client_details_mobile_phone_number);
        TextView telefonoFijo = (TextView)findViewById(R.id.client_details_static_phone_number);
        TextView mail = (TextView)findViewById(R.id.client_details_mail);
        NetworkImageView portrait = (NetworkImageView)findViewById(R.id.client_details_portrait);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.client_map_layout);
        mapFragment.getMapAsync(this);

        //TODO agregar manejo de excepcion
        //TODO agregar un viewholder para limpar esto
        nombre.append(client.getNombre());
        compania.append(client.getCompania());
        address.append(client.getDireccion());
        telefonoMovil.append(client.getMobilePhoneNumber());
        telefonoFijo.append(client.getTelefono());
        mail.append(client.getCorreo());
        ImageLoader il = NetworkManagerSingleton.getInstance(this).getImageLoader();
        portrait.setImageUrl(client.getImagen(), il);


        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        ImageLoader mImageLoader = NetworkManagerSingleton.getInstance(this).getImageLoader();
        final ImageView mImageView = new ImageView(this);
        mImageLoader.get(c.getImagen(), new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mImageView.setImageResource(R.drawable.err_image);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    mImageView.setImageBitmap(response.getBitmap());
                    //toolbarLayout.setBackground(mImageView.getDrawable());
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

        findViewById(R.id.fab_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    //start the scanning activity from the com.google.zxing.client.android.SCAN intent
                    Intent intent = new Intent(getString(R.string.action_scan));
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe)
                {
                    showDialog(ClientDetails.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                }
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap map)
    {
        LatLng pos = new LatLng(c.getLat(), c.getLng());
        map.moveCamera(CameraUpdateFactory.newLatLng(pos));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(c.getLat(), c.getLng()))
                .title(c.getNombre()));
    }

    public void startCall(View view) {
        String number = "tel:" + c.getMobilePhoneNumber();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        try {
            startActivity(intent);
        } catch(SecurityException e) {

        }
    }

    public void sendMail(View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , c.getCorreo());
        i.putExtra(Intent.EXTRA_SUBJECT, "[VENDEDOR]");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ClientDetails.this, "No hay aplicación de E-Mails Instalada", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String result = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                CustomJsonArrayRequest jsObjRequest = new CustomJsonArrayRequest("http://dev-taller2.pantheonsite.io/api/clientes.json?args[0]=" + result, ((Global)getApplicationContext()).getUserPass(), this.createRequestSuccessListener(), this.createRequestErrorListener());
                NetworkManagerSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

                Toast toast = Toast.makeText(this, "Content:" + result + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo)
    {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    Toast toast = Toast.makeText(ClientDetails.this, "Error", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    private Response.Listener<JSONArray> createRequestSuccessListener()
    {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                if (response.length() == 0)
                {
                    Toast toast = Toast.makeText(ClientDetails.this, "Cliente inexistente", Toast.LENGTH_LONG);
                    toast.show();
                }
                else
                {
                    try
                    {
                        Toast toast = Toast.makeText(ClientDetails.this, response.getString(0), Toast.LENGTH_LONG);
                        toast.show();

                        Gson gson = new Gson();
                        Client clienteQR;
                        clienteQR = gson.fromJson( response.get(0).toString(), Client.class);

                        // Revisar esta comparacion porque es un asco
                        if (clienteQR.getNombre().compareTo(c.getNombre()) == 0)
                        {
                            Intent newIntent = new Intent(ClientDetails.this, NewOrderActivity.class);
                            Gson gs = new Gson();
                            String clientString = gs.toJson(c);
                            newIntent.putExtra(getString(R.string.serializedClientKey), clientString);
                            startActivity(newIntent);
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener()
    {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        };
    }
}
