package com.projeto.engsoft.projetoandroid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Connection{
    private static Connection uniqueInstance;

    public static Connection getInstance() {
        if(uniqueInstance == null)
            uniqueInstance = new Connection();

        return uniqueInstance;

    }

    private final String USER_AGENT = "Mozilla/5.0";


    // HTTP GET request
    public List<Local> sendGet() throws Exception {

        //https://api.myjson.com/bins/3kpyw
        //http://api.flickr.com/services/feeds/photos_public.gne?tags=beatles&format=json&jsoncallback=?
        String url = "https://api.myjson.com/bins/4aj31";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //System.out.println(response.toString());

        List<Local> found = findAllItems(new JSONObject(response.toString()));

        return found;
    }

    public List<Local> findAllItems(JSONObject response) {

        List<Local> found = new LinkedList<Local>();

        try {


            for (int i = 0; i < response.getJSONArray("locais").length(); i++) {
                JSONObject obj = response.getJSONArray("locais").getJSONObject(i);
                found.add(new Local(obj.getString("nome").toString(), obj.getString("tipo").toString(), toLista(obj.getJSONArray("comidas"))));
            }

        } catch (JSONException e) {
            // handle exception
        }

        return found;
    }
    public List<String> toLista(JSONArray json)
    {
        List<String> list = new LinkedList<String>();
        if (json != null) {
            int len = json.length();
            for (int i=0;i<len;i++){
                try {
                    list.add(json.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


}
