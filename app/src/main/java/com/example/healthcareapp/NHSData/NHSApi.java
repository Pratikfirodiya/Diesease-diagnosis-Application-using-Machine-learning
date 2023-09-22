package com.example.healthcareapp.NHSData;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthcareapp.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NHSApi {
    public Context context;
    String BaseURL="https://api.nhs.uk/conditions/allergies";
    String APIKey="d4becc1bd0d649d28ab33d08a2338102";
    private RequestQueue requestQueue;
    public NHSApi(Context context)
    {
        this.context=context;
    }
public  void getdataforSpecificationcondition(String condition) {

//    RequestQueue queue = Volley.newRequestQueue(context);
//    JsonObjectRequest
//            jsonObjectRequest
//            = new JsonObjectRequest(
//            Request.Method.GET,
//         BaseURL,
//            null,
//            new Response.Listener() {
//                @Override
//                public void onResponse(Object response) {
//                    Toast.makeText(context, "Data added to API"+response.toString(), Toast.LENGTH_SHORT).show();
//
//                }
//
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error)
//                {
//                    Toast.makeText(context, "Fail to get response = " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }){
//        @Override
//        public Map<String, String> getHeaders(){
//
//            Map<String, String> API = new HashMap<String, String>();
//            API.put("subscription-key", APIKey);
//            return API;
//        }
//    };
//    queue.add(jsonObjectRequest);
    requestQueue = Volley.newRequestQueue(context);
    String url = "https://api.nhs.uk/conditions/allergies/";
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONObject hasPartObject = response.getJSONObject("hasPart");

                        JSONArray treatmentsArray = hasPartObject.getJSONArray("hasPart");

                        for (int i = 0; i < treatmentsArray.length(); i++) {
                            JSONObject treatmentObject = treatmentsArray.getJSONObject(i);
                            String headline = treatmentObject.getString("headline");
                            String text = treatmentObject.getJSONObject("text").getString("text");

                            Log.d("Treatment Headline", headline);
                            Log.d("Treatment Text", text);
                        }

                    } catch (JSONException e) {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
        }
    }){
        @Override
        public Map<String, String> getHeaders() {

            Map<String, String> API = new HashMap<String, String>();
            API.put("subscription-key", APIKey);
            return API;
        }
        };

    requestQueue.add(jsonObjectRequest);
}

}
