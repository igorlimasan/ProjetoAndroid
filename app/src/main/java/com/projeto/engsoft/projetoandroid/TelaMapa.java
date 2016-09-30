package com.projeto.engsoft.projetoandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class TelaMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Local loc;
    private final String USER_AGENT = "Mozilla/5.0";
    private LocationManager lm;
    private Location location;



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
        //ml = new GPS(this);
        if (!checkLocationPermission())
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


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
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener gps = new GPS(mMap,loc);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gps);

        GPS nGps = new GPS(mMap,loc);

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
            View v = getLayoutInflater().inflate(R.layout.info_mapa, null);

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {


                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout


                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude
                TextView nome = (TextView) v.findViewById(R.id.nome);

                // Getting reference to the TextView to set longitude
                TextView endereco = (TextView) v.findViewById(R.id.endereco);



                // Setting the latitude
                if (ActivityCompat.checkSelfPermission(TelaMapa.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TelaMapa.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                if (arg0.getPosition().latitude == location.getLatitude() && arg0.getPosition().longitude == location.getLongitude()) {
                    nome.setText("Minha Posição");
                    endereco.setText("");




                } else {
                    nome.setText(loc.getNome());
                    endereco.setText(loc.getEndereco());
                }


                // Setting the longitude


                // Returning the view containing InfoWindow contents
                return v;

            }

        });

        // Adding and showing marker while touching the GoogleMap


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            View v = getLayoutInflater().inflate(R.layout.info_mapa, null);
            @Override
            public boolean onMarkerClick(Marker marker) {

                marker.showInfoWindow();
                return true;
            }
        });

        LatLng mrk = new LatLng(loc.getLat(), loc.getLongt());
        mMap.addMarker(new MarkerOptions().position(mrk));



        String res = nGps.montarURLRotaMapa(location.getLatitude(),location.getLongitude(),loc.getLat(),loc.getLongt());
        JSONObject rota = nGps.requisicaoHTTP(res);
        nGps.pintarCaminho(rota);

        CameraPosition update = new CameraPosition(mrk,15,0,0);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update),3000,null);
    }







@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

}


