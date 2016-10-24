package com.projeto.engsoft.projetoandroid;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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



    List<Local> found = new LinkedList<Local>();

    public static Connection getInstance() {
        if(uniqueInstance == null)
            uniqueInstance = new Connection();

        return uniqueInstance;

    }

    private final String USER_AGENT = "Mozilla/5.0";


    // HTTP GET request
    public List<Local> sendGet() throws Exception {
        found.clear();

        //https://api.myjson.com/bins/3kpyw
        //http://api.flickr.com/services/feeds/photos_public.gne?tags=beatles&format=json&jsoncallback=?
        //String url = "https://api.myjson.com/bins/4aj31";
        //String url = "https://api.myjson.com/bins/4ag9o";
        //String url = "https://api.myjson.com/bins/4kgoo";
       /* String url = "https://api.myjson.com/bins/567eg";

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

        found = findAllItems(new JSONObject(response.toString()));*/

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fireDb = database.getReference();
        fireDb.child("locais").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    found.add(new Local(ds.child("nome").getValue().toString(),ds.child("tipo").getValue().toString(),toLista(ds.child("comidas")),Double.valueOf(ds.child("lat").getValue().toString()),Double.valueOf(ds.child("long").getValue().toString()),ds.child("endereco").getValue().toString()));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FIREBASE ERRO","Erro: "+databaseError.getMessage());

            }
        });

        return found;
    }

/*    public List<Local> findAllItems(JSONObject response) {

        List<Local> found = new LinkedList<Local>();

        try {


            for (int i = 0; i < response.getJSONArray("locais").length(); i++) {
                JSONObject obj = response.getJSONArray("locais").getJSONObject(i);
            found.add(new Local(obj.getString("nome").toString(), obj.getString("tipo").toString(), toLista(obj.getJSONArray("comidas")),obj.getDouble("lat"),obj.getDouble("long"),obj.getString("endereco")));
            }

        } catch (JSONException e) {
            // handle exception
        }

        return found;
    }*/
    public List<String> toLista(DataSnapshot ds)
    {
        List<String> list = new LinkedList<String>();
            for (DataSnapshot d : ds.getChildren()){
                //Log.d("COMIDAS",d.getValue().toString());

                    list.add(d.getValue().toString());

            }

        return list;
    }

    public List<String> returnNames(List<Local> locais)
    {
        List<String> nomes = new LinkedList<String>();
        for(Local l:locais)
        {
            nomes.add(l.getNome());
        }
        return nomes;
    }
    public List<Local> getFound() {
        return found;
    }

    public  int numeroLugares(){
        return found.size();
    }



}
