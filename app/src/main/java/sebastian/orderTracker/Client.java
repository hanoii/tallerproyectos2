package sebastian.orderTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class Client{


    private String name;
    private String company;
    private String imgSrc;
    private String mobilePhoneNumber;
    private String staticPhoneNumber;
    private String mailAdress;
    private String adress;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    //TODO convertir en objeto coherente de geolocalizacion
    private double lat;
    private double lng;


    public String getAdress() {
        return adress;
    }


    public String getMailAdress() {
        return mailAdress;
    }

    public String getStaticPhoneNumber() {
        return staticPhoneNumber;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getCompany() {
        return company;
    }


    public Client(JSONObject jsonClient) {
        try {
            this.name = (String)jsonClient.get("nombre") + " " + (String)jsonClient.get("apellido");
            this.company = (String)jsonClient.get("compania");
            this.adress = (String)((JSONObject)jsonClient.get("direccion")).get("address");
            this.mobilePhoneNumber = (String)jsonClient.get("telefono");
            this.staticPhoneNumber = "47518974";
            //this.mailAdress = (String)jsonClient.get("correo");
            this.mailAdress = "asdsasad";
            this.imgSrc = (String)jsonClient.get("imagen");
            this.lat = ((JSONObject) jsonClient.get("direccion")).getDouble("lat");
            this.lng = ((JSONObject) jsonClient.get("direccion")).getDouble("lng");
        } catch(Exception e) {
            this.mailAdress = "malber@gmail.com";
        }
    }

    public Client(String n, String c, String add, String i, String mpn, String spn, String mA) {
        this.name = n;
        this.company = c;
        this.imgSrc = i;
        this.mobilePhoneNumber = mpn;
        this.staticPhoneNumber = spn;
        this.mailAdress = mA;
        this.adress = add;
    }

    public String getName() {
        return name;
    }


}
