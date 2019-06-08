package com.example.testonlineapp;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.testonlineapp.model.Flower;
import com.example.testonlineapp.parsers.FlowerJSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private Button button;
    private ProgressBar progressBar;
    private List<MyTask> tasks;
    private List<Flower> flowerList;
    public static final String BASE_URL = "http://services.hanselandpetal.com/photos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    MyTask myTask = new MyTask();
                    myTask.execute("http://services.hanselandpetal.com/secure/flowers.json");
                } else {
                    Toast.makeText(MainActivity.this, "Turn On Wifi", Toast.LENGTH_SHORT).show();
                }

            }
        });

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();
    }

    protected void updateDisplay() {

        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_flower, flowerList);
        setListAdapter(adapter);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }

    private class MyTask extends AsyncTask<String, String, List<Flower>> {

        @Override
        protected void onPreExecute() {

            if (tasks.size() == 0) {
                progressBar.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Flower> doInBackground(String... params) {

            String content = HttpManager.getData(params[0], "feeduser", "feedpassword");
            flowerList = FlowerJSONParser.parseFeed(content);

//            for (Flower flower : flowerList) {
//                String imageUrl = BASE_URL + flower.getPhoto();
//                InputStream inputStream = null;
//                try {
//                    inputStream = (InputStream) new URL(imageUrl).getContent();
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                flower.setBitmap(bitmap);
//            }


            return flowerList;
        }

        @Override
        protected void onPostExecute(List<Flower> result) {

            tasks.remove(this);
            if (tasks.size() == 0) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            if (result == null){
                Toast.makeText(MainActivity.this, "Can't connect to web service", Toast.LENGTH_SHORT).show();
                return;
            }

            updateDisplay();
        }
    }


}
