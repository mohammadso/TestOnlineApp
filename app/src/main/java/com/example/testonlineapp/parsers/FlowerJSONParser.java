package com.example.testonlineapp.parsers;

import com.example.testonlineapp.model.Flower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FlowerJSONParser {

    public static List<Flower> parseFeed(String content) {

        List<Flower> flowerList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Flower flower = new Flower();

                flower.setProductId(jsonObject.getInt("productId"));
                flower.setCategory(jsonObject.getString("category"));
                flower.setName(jsonObject.getString("name"));
                flower.setInstructions(jsonObject.getString("instructions"));
                flower.setPrice(jsonObject.getDouble("price"));
                flower.setPhoto(jsonObject.getString("photo"));
                flowerList.add(flower);
            }

            return flowerList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
