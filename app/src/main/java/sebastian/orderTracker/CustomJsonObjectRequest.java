package sebastian.orderTracker;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

public class CustomJsonObjectRequest extends JsonRequest<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomJsonObjectRequest(String url, Map<String, String> params,
                                   Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, " ",reponseListener, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomJsonObjectRequest(int method, String url, Map<String, String> params,
                                   Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url," ",reponseListener, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomJsonObjectRequest(int method, String url, Map<String, String> params, JSONObject json,
                                   Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, json.toString(), reponseListener, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() {
        String creds = String.format("%s:%s",params.get("user"),params.get("pass"));
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }
}
