package com.projeto.engsoft.projetoandroid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class telaMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Local loc;
    private final String USER_AGENT = "Mozilla/5.0";
    private MinhaLocalizacao ml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        loc = (Local) intent.getSerializableExtra("valor");
        //ml = new MinhaLocalizacao(this);




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        double latitude=0;
//        double longitude=0;
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
       }
             mMap.setMyLocationEnabled(true);

//        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(loc).title("Minha Posição"));
//
//            }
//        };
//        mMap.setOnMyLocationChangeListener(myLocationChangeListener);


        LatLng mrk = new LatLng(loc.getLat(), loc.getLongt());
        mMap.addMarker(new MarkerOptions().position(mrk).title(loc.getNome()));


        CameraPosition update = new CameraPosition(mrk,15,0,0);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update),3000,null);
    }


//    private String montarURLRotaMapa(double latOrigen, double lngOrigen, double latDestino, double lngDestino){
//        //Base da URL
//        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=";
//        //Local de origem
//        url += latOrigen + "," + lngOrigen;
//        url += "&destination=";
//        //Local de destino
//        url += latDestino + "," + lngDestino;
//        //Outros parametros
//        url += "&sensor=false&mode=driving&alternatives=true";
//
//        return url;
//    }
//
//    public JSONObject requisicaoHTTP(String url) {
//            JSONObject resultado = new JSONObject();
//         try {
//             URL obj = new URL(url);
//             HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//             // optional default is GET
//             con.setRequestMethod("GET");
//
//             //add request header
//             con.setRequestProperty("User-Agent", USER_AGENT);
//
//             int responseCode = con.getResponseCode();
//             System.out.println("\nSending 'GET' request to URL : " + url);
//             System.out.println("Response Code : " + responseCode);
//
//             BufferedReader in = new BufferedReader(
//                     new InputStreamReader(con.getInputStream()));
//             String inputLine;
//             StringBuffer response = new StringBuffer();
//
//             while ((inputLine = in.readLine()) != null) {
//                 response.append(inputLine);
//             }
//             in.close();
//             resultado = new JSONObject(response.toString());
//
//
//         }
//         catch (JSONException e)
//         {
//             e.printStackTrace();
//         } catch (ProtocolException e) {
//             e.printStackTrace();
//         } catch (MalformedURLException e) {
//             e.printStackTrace();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//        return resultado;
//
//        }
//
//
//    public void pintarCaminho(JSONObject json) {
//        try {
//            //Recupera a lista de possíveis rotas
//            JSONArray listaRotas = json.getJSONArray("routes");
//            //Para efeito de aprendizado iremos utilizar apenas a primeira opção
//            JSONObject rota = listaRotas.getJSONObject(0);
//            //Recuperamos os pontos a serem pintados para que surga a 'linha' no mapa
//            String pontosPintar = rota.getJSONObject("overview_polyline").getString("points");
//            //Recuperamos a lista de latitudes e longitudes para sabermos exatamente onde pintar
//            List<LatLng> listaCordenadas = extrairLatLngDaRota(pontosPintar);
//
//            //Percorremos por cada cordenada obtida
//            for(int ponto = 0; ponto < listaCordenadas.size()-1 ; ponto++){
//                //Definimos o ponto atual como origem
//                LatLng pontoOrigem= listaCordenadas.get(ponto);
//                //Definimos o próximo ponto como destino
//                LatLng pontoDestino= listaCordenadas.get(ponto + 1);
//                //Criamos um objeto do tipo PolylineOption para adicionarmos os pontos de origem e destino
//                PolylineOptions opcoesDaLinha = new PolylineOptions();
//                //Adicionamos os pontos de origem e destino da linha que vamos traçar
//                opcoesDaLinha.add(new LatLng(pontoOrigem.latitude, pontoOrigem.longitude),
//                        new LatLng(pontoDestino.latitude,  pontoDestino.longitude));
//                //Criamos a linha de acordo com as opções que configuramos acima e adicionamos em nosso mapa
//                Polyline line = mMap.addPolyline(opcoesDaLinha);
//                //Mudamos algumas propriedades da linha que acabamos de adicionar em nosso mapa
//                line.setWidth(5);
//                line.setColor(Color.rgb(158, 0, 0));
//                line.setGeodesic(true);
//            }
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//    private List<LatLng> extrairLatLngDaRota(String pontosPintar) {
//        List<LatLng> listaResult = new ArrayList<LatLng>();
//        int index = 0, len = pontosPintar.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//
//            int b, shift = 0, result = 0;
//            do {
//                b = pontosPintar.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = pontosPintar.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng( (((double) lat / 1E5)),
//                    (((double) lng / 1E5) ));
//            listaResult.add(p);
//        }
//
//        return listaResult;
//    }

}


