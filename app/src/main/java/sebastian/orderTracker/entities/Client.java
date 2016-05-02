package sebastian.orderTracker.entities;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;


public class Client{


    private String nombre;
    private String compania;
    private String imagen;
    private String mobilePhoneNumber;
    private String telefono;
    private String correo;
    private String direccion;
    private boolean visited;

    public String getId() {
        return id;
    }

    private String id;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    //TODO convertir en objeto coherente de geolocalizacion
    private double lat;
    private double lng;


    public String getDireccion() {
        return direccion;
    }

    public LatLng getDireccionAsLatLng() {
        LatLng latlng = new LatLng(lat, lng);
        return latlng;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getImagen() {
        return imagen;
    }

    public String getCompania() {
        return compania;
    }

    public boolean hasBeenVisited() {
        return visited;
    }

    public Client(JSONObject jsonClient) {
        try {
            this.nombre = (String)jsonClient.get("nombre") + " " + (String)jsonClient.get("apellido");
            this.compania = (String)jsonClient.get("compania");
            this.direccion = (String)((JSONObject)jsonClient.get("direccion")).get("address");
            this.mobilePhoneNumber = (String)jsonClient.get("telefono");
            this.telefono = "47518974";
            this.correo = (String)jsonClient.get("correo");
            //this.correo = "asdsasad";
            this.imagen = (String)jsonClient.get("imagen");
            this.lat = ((JSONObject) jsonClient.get("direccion")).getDouble("lat");
            this.lng = ((JSONObject) jsonClient.get("direccion")).getDouble("lng");
            this.id = (String)jsonClient.get("id");
            visited = false;
        } catch(JSONException e) {
            e.printStackTrace();
            this.correo = "malber@gmail.com";
        }
    }

    public Client(String n, String c, String add, String i, String mpn, String spn, String mA) {
        this.nombre = n;
        this.compania = c;
        this.imagen = i;
        this.mobilePhoneNumber = mpn;
        this.telefono = spn;
        this.correo = mA;
        this.direccion = add;
    }

    public String getNombre() {
        return nombre;
    }


}
