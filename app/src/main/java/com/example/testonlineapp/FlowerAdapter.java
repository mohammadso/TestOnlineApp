package com.example.testonlineapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testonlineapp.model.Flower;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FlowerAdapter extends ArrayAdapter<Flower> {

    private Context context;
    private List<Flower> flowerList;

    public FlowerAdapter(Context context, int resource, List<Flower> objects){
        super(context, resource, objects);
        this.context = context;
        this.flowerList = objects;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_flower, parent, false);

        Flower flower = flowerList.get(position);
        TextView tv = view.findViewById(R.id.textView1);
        tv.setText(flower.getName());

        if (flower.getBitmap() != null) {
            ImageView imageView = view.findViewById(R.id.imageView1);
            imageView.setImageBitmap(flower.getBitmap());
        } else {

            FlowerAndView container = new FlowerAndView();
            container.flower = flower;
            container.view = view;

            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(container);
        }
        return view;
    }

    private class FlowerAndView{
        public Flower flower;
        public View view;
    }

    private class ImageLoader extends AsyncTask<FlowerAndView ,Void, FlowerAndView>{

        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {
            FlowerAndView container = params[0];
            Flower flower = container.flower;
            Bitmap bitmap = null;

            try {
                String imageUrl = MainActivity.BASE_URL + flower.getPhoto();
                InputStream stream = (InputStream) new URL(imageUrl).getContent();
                bitmap = BitmapFactory.decodeStream(stream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            flower.setBitmap(bitmap);

            return container;
        }

        @Override
        protected void onPostExecute(FlowerAndView container) {
            ImageView imageView = container.view.findViewById(R.id.imageView1);
            imageView.setImageBitmap(container.flower.getBitmap());        }
    }
}
