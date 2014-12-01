package com.example.prashantpandey.prashant_app;

/**
 * Created by Prashant.Pandey on 2014-09-24.
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TaskHandler {

    private HttpEntity entity;
    private String streamurl;
    private String result;

    public String execute(String url) {
        //HttpParams httpParameters = new BasicHttpParams();
        //int timeoutConnection = 1000;
        //HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        //int timeoutSocket = 1000;
        //HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        String responseString = null;
        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream stream = entity.getContent();
                responseString = convertStreamToString(stream);
                Log.d("TAGHANDLER", "INHANDLERR");
                try {
                    JSONObject jobject = new JSONObject(responseString);
                    //result=jobject.getString("stream_url");
                    //streamurl=result+"?consumer_key=55705d5e4806d3c0cec8c670e0c8bd8a&filter=all&order=created_at&order=original&allow_redirects=True";

                    //Log.d("TAGSTRAMM",streamurl);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}




