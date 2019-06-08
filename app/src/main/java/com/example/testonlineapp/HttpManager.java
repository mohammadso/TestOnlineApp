package com.example.testonlineapp;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpManager {

    private static final String TAG = "HttpManagerClass";

    public static String getData(String uri) {

        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "MalformedURL");
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException" );
            return null;

        } finally {

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException  buffer close" );
                    return null;
                }
            }
        }

    }


    public static String getData(String uri, String userName, String password) {

        BufferedReader bufferedReader = null;
        HttpURLConnection httpURLConnection = null;

        byte[] loginByte =  (userName + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginByte, Base64.DEFAULT));


        try {
            URL url = new URL(uri);
            httpURLConnection = (HttpURLConnection) url.openConnection();

//            Log.e(TAG, "UserPass" + loginByte.toString());
//            Log.e(TAG, "UserPass" + loginBuilder.toString());

            httpURLConnection.setRequestProperty("Authorization", loginBuilder.toString());

            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "MalformedURL");
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException" );
            return null;

        } finally {

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException  buffer close" );
                    return null;
                }
            }

            try {
                Log.e(TAG, "ResponseCode " + httpURLConnection.getResponseCode());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
