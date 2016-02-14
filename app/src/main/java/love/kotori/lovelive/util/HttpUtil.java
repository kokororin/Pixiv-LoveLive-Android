package love.kotori.lovelive.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class HttpUtil {

    private static RequestQueue mQueue;

    public static void httpRequest(Context context, final String address, final HttpUtilCallBack httpUtilCallBack) {
        if (mQueue == null) mQueue = Volley.newRequestQueue(context);

        Response.Listener<String> requestListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                httpUtilCallBack.onFinish(response);
            }
        };
        Response.ErrorListener requestErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                httpUtilCallBack.onError(error);
            }
        };

        StringRequest stringRequest = new StringRequest(address, requestListener, requestErrorListener);

        mQueue.add(stringRequest);
    }


    public interface HttpUtilCallBack {
        void onFinish(String response);

        void onError(Exception e);
    }
}
