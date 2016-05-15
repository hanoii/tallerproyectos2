package sebastian.orderTracker;

import android.app.Application;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Senastian on 16/04/2016.
 */
public class Global extends Application {


    public Map<String, String> getUserPass() {
        Map<String, String> userPass = new HashMap<String, String>();
        userPass.put("user", username);
        userPass.put("pass", password);
        return userPass;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private String username;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    public String tempUrl;

    public String getDate() {
        Calendar currentTime = Calendar.getInstance();
        String date = (currentTime.get(Calendar.DAY_OF_MONTH) + "/" + currentTime.get(Calendar.MONTH)
                + "/" + currentTime.get(Calendar.YEAR));
        return date;
    }

}
