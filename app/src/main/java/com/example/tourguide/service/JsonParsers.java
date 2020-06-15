package com.example.tourguide.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParsers {
    private HashMap<String,String> parseJsonObject(JSONObject object){
        //Initialize hash map
        HashMap<String,String> dataList = new HashMap<>();

        try {

            String name = object.getString("name");
            String latitude = object.getJSONObject("geometry")
                    .getJSONObject("location")
                    .getString("lat");
            //GetLongitude from object
            String longitude = object.getJSONObject("geometry")
                    .getJSONObject("location")
                    .getString("lng");
            dataList.put("name",name);
            dataList.put("lat",latitude);
            dataList.put("lng",longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Return HashMap
        return dataList;
    }
    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray){
        //Initiaize hash map list
        List<HashMap<String,String>> dataList = new ArrayList<>();
        for (int i = 0;i<jsonArray.length();i++){
            try {
                //Initialize hash map
                HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                //Add data in hash map list
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    public List<HashMap<String,String>> parseResult(JSONObject object){
        //Initialize json Array
        JSONArray jsonArray = null;
//get results array
        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  parseJsonArray(jsonArray);
    }
}
