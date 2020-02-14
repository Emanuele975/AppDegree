package com.example.user.appdegree.utility;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RequestService extends IntentService {

    public static final int REQUEST_DOWNLOAD = 0;

    public static final String REQUEST_ACTION = "action";
    public static final String FILTER_REQUEST_DOWNLOAD = "filter_request_download";
    public static final String EXTRA_SERVER_RESPONSE = "extra_server_response";

    public RequestService() {
        super("RequestService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent == null) return;
        int action = intent.getIntExtra(REQUEST_ACTION, -1);
        switch (action){
            case REQUEST_DOWNLOAD:
                uploadData();
                break;
        }
    }

    private void uploadData(){

        String addressweb = "http://localhost:8080/prova";

        System.out.println(addressweb);
        // Check if the address is an URL
        URL url = null;
        try {
            url = new URL(addressweb);
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
        if(url == null) return;

        // Do the GET Request
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // Read the response from the right stream
            int code = connection.getResponseCode();
            //System.out.println("CODE : " + code);
            /*
            int count = 0;
            if(code == HttpURLConnection.HTTP_OK){

                InputStream is = connection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                String[] x;
                while((line = br.readLine()) != null){
                    if(count < 2)System.out.println(line);
                    count++;
                    if(count > 2) {
                        x = line.split(";");
                        if(x.length > 19) {

                            Pharmacy pharmacy = new Pharmacy();
                            pharmacy.setName(x[3]);
                            pharmacy.setAddress(x[2]);
                            String str = x[18];
                            if (!str.equals("-"))
                            {
                                str = str.replace (',', '.');
                                double lat = Double.parseDouble(str);
                                pharmacy.setLatitude(lat);
                            }
                            String strr = x[19];
                            if (!strr.equals("-")) {
                                strr = strr.replace(',', '.');
                                double lon = Double.parseDouble(strr);
                                pharmacy.setLongitude(lon);
                            }
                            pharmacy.setCity(x[7]);
                            savedatadb(pharmacy);
                        }
                    }
                }
                    // Send the response in broadcast to all components of only this application.
                    boolean y=true;
                    Intent intent = new Intent(FILTER_REQUEST_DOWNLOAD);
                    intent.putExtra(EXTRA_SERVER_RESPONSE, y);
                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .sendBroadcast(intent);
                            */
            } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //Intent openMainActivity = new Intent(getApplicationContext(), InputActivity.class);
        //openMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(openMainActivity);

    }

}
