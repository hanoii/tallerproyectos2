package sebastian.orderTracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

public class ClientDetails extends AppCompatActivity {

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

        TextView nombre = (TextView)findViewById(R.id.client_details_name);
        TextView compania = (TextView)findViewById(R.id.client_details_company);
        TextView address = (TextView)findViewById(R.id.client_details_address);
        TextView telefonoMovil = (TextView)findViewById(R.id.client_details_mobile_phone_number);
        TextView telefonoFijo = (TextView)findViewById(R.id.client_details_static_phone_number);
        TextView mail = (TextView)findViewById(R.id.client_details_mail);
        //TODO agregar manejo de excepcion
        nombre.append(client.getName());
        compania.append(client.getCompany());
        address.append(client.getAdress());
        telefonoMovil.append(client.getMobilePhoneNumber());
        telefonoFijo.append(client.getStaticPhoneNumber());
        mail.append(client.getMailAdress());
    }
}
