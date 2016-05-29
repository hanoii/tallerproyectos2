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
        String date = (currentTime.get(Calendar.DAY_OF_MONTH) + "/" + (currentTime.get(Calendar.MONTH) +1)
                + "/" + currentTime.get(Calendar.YEAR));
        return date;
    }

    public int getCurrentMonth() {
        Calendar currentTime = Calendar.getInstance();
        return currentTime.get(Calendar.MONTH)+1;
    }

    public int getCurrentYear() {
        Calendar currentTime = Calendar.getInstance();
        return currentTime.get(Calendar.YEAR);
    }

    public int getCurrentDay() {
        Calendar currentTime = Calendar.getInstance();
        return currentTime.get(Calendar.DAY_OF_MONTH);
    }


    public int getMaxiumDay(int month, int year) {
        if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        }
        if(month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2 && (year % 4) == 0) {
            return 29;
        }
        if(month == 2 && (year % 4 != 0)) {
            return 28;
        }
        return -1;
    }


}
