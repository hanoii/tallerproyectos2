package sebastian.orderTracker;

import android.util.Base64;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sebastian on 17/04/2016.
 */
public class CustomStringRequest extends StringRequest {

    public int getStatusCode() {
        return statusCode;
    }

    private int statusCode;
    private Map<String, String> params;


    public CustomStringRequest(int method, String url, Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public CustomStringRequest(String url, Map<String, String> params,Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
        this.params = params;
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        this.statusCode = response.statusCode;
        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() {
        if(params != null) {
            String creds = String.format("%s:%s", params.get("user"), params.get("pass"));
            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
            params.put("Authorization", auth);
            return params;
        } else {
            params = new HashMap<String, String>();
            return params;
        }
    }
}
