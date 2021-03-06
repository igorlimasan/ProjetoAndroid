package com.projeto.engsoft.projetoandroid;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Connection{
    private static Connection uniqueInstance;



    private List<Local> found = new LinkedList<Local>();
    private List<String> tipos = new LinkedList<String>();
    private List<String> comidas = new LinkedList<String>();

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
                if(dataSnapshot != null || found.isEmpty()) {
                    found.clear();
                    tipos.clear();
                    comidas.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.i("NOME",ds.child("nome").toString());

                        found.add(new Local(ds.child("nome").getValue().toString(), ds.child("tipo").getValue().toString(), toLista(ds.child("comidas")), Double.valueOf(ds.child("lat").getValue().toString()), Double.valueOf(ds.child("long").getValue().toString()), ds.child("endereco").getValue().toString()));
                        if (!tipos.contains(ds.child("tipo").getValue().toString()))
                            tipos.add(ds.child("tipo").getValue().toString());

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FIREBASE ERRO","Erro: "+databaseError.getMessage());

            }
        });

        return found;
    }

public double[] findAllItems(JSONObject response) {

        double[] found = new double[2];

        try {


            for (int i = 0; i < response.getJSONArray("results").length(); i++) {
                JSONObject obj = response.getJSONArray("results").getJSONObject(i);
                found[0] = obj.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Log.i("POSICAO LAT", String.valueOf(found[0]));
                found[1] = obj.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                Log.i("POSICAO LNG", String.valueOf(found[1]));
            }


        } catch (JSONException e) {
            // handle exception
        }

        return found;
    }
    public List<String> toLista(DataSnapshot ds)
    {
        List<String> list = new LinkedList<String>();
            for (DataSnapshot d : ds.getChildren()){
                //Log.d("COMIDAS",d.getValue().toString());

                list.add(d.getValue().toString());
                if(!comidas.contains(d.getValue().toString())) comidas.add(d.getValue().toString());



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

    public List<String> getTipos() {
        return tipos;
    }

    public List<String> getComidas() {
        return comidas;
    }

    public void inserirLocal(Local local){
        String[] comidas = new String[local.getComidas().size()];
        comidas = local.getComidas().toArray(comidas);

        Map<String, Object> newLocal = new HashMap<String, Object>();
        newLocal.put("nome", local.getNome());
        newLocal.put("tipo", local.getTipo());
        newLocal.put("comidas", local.getComidas());
        newLocal.put("lat", local.getLat());
        newLocal.put("long", local.getLongt());
        newLocal.put("endereco", local.getEndereco());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fireDb = database.getReference();
        DatabaseReference insertFireDb = fireDb.child("locais").push();
        insertFireDb.setValue(newLocal);


       /* try {
            sendGet();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

    public double[] getLocalLatLong(String endereco)  {
        String url = null;
        double[] latlng = new double[2];
        try {
            url = "http://maps.google.com/maps/api/geocode/json?address="+ URLEncoder.encode(endereco, "UTF-8") +"&components=country:br&sensor=false";
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

            latlng = findAllItems(new JSONObject(response.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return latlng;



    }


}
