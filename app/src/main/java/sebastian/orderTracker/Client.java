package sebastian.orderTracker;

/**
 * Created by Senastian on 03/04/2016.
 */
public class Client {


    private String name;
    private String company;
    private String imgSrc;
    private String mobilePhoneNumber;
    private String staticPhoneNumber;
    private String mailAdress;
    private String adress;

    public String getAdress() {
        return adress;
    }

    private String adresss;

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
