package com.emergency.joy;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;


public class LocationService extends Service {
    public static LocationManager locationManager;
    public static String latlongString;
    public static LocationProvider locationProvider;
    public Context context1 = MainActivity.context1;

    static String Gprovider = LocationManager.GPS_PROVIDER;
    public static String provider;

    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        LocationUpdate();
        super.onCreate();
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
               // updateFirebaseLocation();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

                //App.HideDialog();
                LocInit();
                //updateFirebaseLocation();

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onProviderDisabled(String s) {
            //App.showErrorDialog();
        }
    };

    public  void LocationUpdate() {
        LocInit();
    }
    
    public  void LocInit() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setBearingRequired(true);
        criteria.setAltitudeRequired(true);
        criteria.setCostAllowed(true);

        locationManager = (LocationManager) context1.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        provider = locationManager.getBestProvider(criteria, false);

        //assert locationManager != null;
        if (ActivityCompat.checkSelfPermission(context1, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context1, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        //updateWithLocation(location);
        locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);

    }

/*
    public void updateWithLocation(Location location) {

        FusedLocationProviderClient mFusedLocation;
        mFusedLocation = LocationServices.getFusedLocationProviderClient(context1);

        if (ActivityCompat.checkSelfPermission(context1, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context1, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context1, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context1, "You must enable Location Permission to use Elli", Toast.LENGTH_LONG).show();
            }
        */
/*    ActivityCompat.requestPermissions((Activity) context1, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            App.editPreference.putString(App.MyLocationLatitude, String.valueOf(location.getLatitude()));
            App.editPreference.putString(App.MyLocationLongitude, String.valueOf(location.getLatitude()));
            App.editPreference.commit();*//*

            return;
        }
        mFusedLocation.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                         latlongString = latitude + " " + longitude;

                       */
/* App.editPreference.putString(App.MyLocationLatitude, String.valueOf(latitude));
                        App.editPreference.putString(App.MyLocationLongitude, String.valueOf(longitude));
                        App.editPreference.commit();*//*

                    }
            }
        });
    }

*/




    public Location getLastKnowLocation() {
        if (ActivityCompat.checkSelfPermission(context1, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context1, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
        }
        return locationManager.getLastKnownLocation(Gprovider);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
