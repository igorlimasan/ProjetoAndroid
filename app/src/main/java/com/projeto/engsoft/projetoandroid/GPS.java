package com.projeto.engsoft.projetoandroid;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by User on 28/08/2016.
 */
public class GPS implements LocationListener {

    private GoogleMap map;
    private double lat;
    private double longt;
    private Local loc;



    public GPS(GoogleMap map, Local loc){

        this.map = map;
        this.loc = loc;


    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        longt=location.getLongitude();
        int cont =0;
        Marker marker = null;


        //Double distance = calc.CalculationByDistance(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(this.otherMarkers.get(0).getLat(), this.otherMarkers.get(0).getLon()));
        //Integer km = distance.intValue();
        map.clear();


        map.addMarker(new MarkerOptions().position(new LatLng(lat, longt)).title("Minha posição"));
        map.addMarker(new MarkerOptions().position(new LatLng(loc.getLat(), loc.getLongt())));






    }


    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public double getLat() {
        return lat;
    }

    public double getLongt() {
        return longt;
    }
}
