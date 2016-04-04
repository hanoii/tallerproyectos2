package sebastian.orderTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Client {


    private String name;
    private String company;
    private String imgSrc;
    private String mobilePhoneNumber;
    private String staticPhoneNumber;
    private String mailAdress;
    private String adress;

    public Client() {

    }

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
            //this.company = (String)jsonClient.get();
            this.company = "Farafasdas S.A.";
            this.adress = (String)((JSONObject)jsonClient.get("direccion")).get("address");
            this.mobilePhoneNumber = (String)jsonClient.get("telefono");
            //this.staticPhoneNumber = (String)jsonClient.get();
            this.staticPhoneNumber = "21321321";
            //this.mailAdress = (String)jsonClient.get();
            this.mailAdress = "asdsa@asdads.com";
            this.imgSrc = (String)jsonClient.get("imagen");
        } catch(JSONException e) {
            ArrayList a = null;
            a.add(1);
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
