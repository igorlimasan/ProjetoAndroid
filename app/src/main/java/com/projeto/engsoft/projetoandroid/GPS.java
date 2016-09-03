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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by User on 28/08/2016.
 */
public class GPS implements LocationListener {

    private GoogleMap map;



    public GPS(GoogleMap map){

        this.map = map;

    }

    @Override
    public void onLocationChanged(Location location) {
        double lat=location.getLatitude();
        double longt=location.getLongitude();


        //Double distance = calc.CalculationByDistance(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(this.otherMarkers.get(0).getLat(), this.otherMarkers.get(0).getLon()));
        //Integer km = distance.intValue();
        map.addMarker(new MarkerOptions().position(new LatLng(lat, longt)).title("Minha posição"));

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
}
