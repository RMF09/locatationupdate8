package com.rmf.locationupdateandroid8;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService10 extends Service {
    private boolean requestingLocationUpdates;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Location_Update", "masuk start command");
        if(intent !=null){
            Log.e("Location_Update", "intent tidak kosng");
            String action  = intent.getAction();
            if(action!=null){
                Log.e("Location_Update", "acftion tidak kosong");
                if(action.equals(Constans.ACTION_START_LOCATION_SERVICE)){
                    Log.e("Location_Update", "masuk startlocatiionService di command command");
                    startLocationService();
                }else if(action.equals(Constans.ACTION_STOP_LOCATION_SERVICE)){
                    Log.e("Location_Update", "masuk stoplocationcService command");
                    stopLocationService();
                }
            }
        }else{
            Log.e("Location_Update", "intent kosong");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startLocationService() {
        Log.e("LocationResult","StartLocagtionServices");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.e("LocationResult","null");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longtitude = location.getLongitude();
                    Log.d("Location_Update", latitude + ", " + longtitude);
                    Toast.makeText(getApplicationContext(), "Location_Update" + latitude + ", " + longtitude, Toast.LENGTH_SHORT).show();

                }
            }

            ;
        };

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        requestingLocationUpdates = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (requestingLocationUpdates) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("RMF","tida masuk");
            return;
        }
        Log.e("RMF"," masuk");
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.myLooper());
    }
    private void stopLocationService(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
//        stopForeground(true);
        stopSelf();
    }
}
