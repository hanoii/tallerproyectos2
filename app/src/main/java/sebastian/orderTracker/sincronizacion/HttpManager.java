package sebastian.orderTracker.sincronizacion;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.util.Log;

public class HttpManager {

    /*public static String get(String URL) throws MalformedURLException, IOException {
        URL reqURL = new URL(URL); //the URL we will send the request to
        HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
        request.setRequestMethod("GET");
        request.connect();
        return request.getResponseMessage();
    }

    public static void get() throws MalformedURLException, IOException {
        URL reqURL = new URL("http://posttestserver.com/post.php"); //the URL we will send the request to
        HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
        String get = "this will be the get data that you will send";
        request.setDoOutput(true);
        request.addRequestProperty("Content-Length", Integer.toString(get.length())); //add the content length of the get data
        request.addRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //add the content type of the request, most get data is of this type
        request.setRequestMethod("POST");
        request.connect();
        OutputStreamWriter writer = new OutputStreamWriter(request.getOutputStream()); //we will write our request data here
        writer.write(get);
        writer.flush();
    }*/


    public  static final String ERROR_CODE = "ERROR";
    private static final int SYNCRONIZATION_TIMEOUT = 30000;
    private static final int CONNECTION_TIME_OUT = 90000;
    private static HttpManager instance;
    private Context context;

    private HttpManager() {}

    public static HttpManager getInstance(Context context) {
        if (instance == null) {
            instance = new HttpManager();
            instance.context = context;
        }

        return instance;
    }

    public void cosa() {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://www.mysite.se/index.asp?data=99");
            urlConnection = (HttpURLConnection) url
                    .openConnection();



            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                System.out.print(current);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public String get(String url) throws IOException {
        String result = "";
        URL connectionURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) connectionURL.openConnection();
        try {
        InputStream in = connection.getInputStream();
        InputStreamReader isw = new InputStreamReader(in);

        int data = isw.read();
        while (data != -1) {
            char current = (char) data;
            data = isw.read();
            System.out.print(current);
            result += current;
        }
        } catch (Exception e) {
                result = ERROR_CODE;
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }
}