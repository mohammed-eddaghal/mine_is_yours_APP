package com.ensias.mine_is_yoursapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private LatLng latLng;

    private static final int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        client = LocationServices.getFusedLocationProviderClient(this);

        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);

        } else {
            // already permission granted
            getLocation();
            /*LatLng latLng2 = new LatLng(34.0492, -6.7353);
            if(latLng!=null)
            Log.d("testYYY","latitude"+latLng.latitude+"longitude"+ latLng.longitude);
            else
                Log.d("testXX","latLng est null");
*/
            //Log.d("distance","la distance entre les deux point est : " + getDistance(latLng,latLng2));

        }
    }

    public int getDistance(LatLng l1,LatLng l2){
        return (int) SphericalUtil.computeDistanceBetween(l1, l2);
    }

    private LatLng getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }else{
            Log.d("test","test access to methode");
        client.getLastLocation().
        addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d("test2","test access to methode 2");
                if(location!=null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            Log.d("test3","latitude"+location.getLatitude()+"longitude"+ location.getLongitude());
                            //initialize lat lng
                            latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            LatLng latLng2 = new LatLng(34.0686, -6.7480);
                            Log.d("distance","la distance est :" +getDistance(latLng,latLng2)/ 1000);

                            //creat marker potion
                            mMap = googleMap;
                            mMap.addMarker(new MarkerOptions().position(latLng).title("current location"));
                            mMap.addMarker(new MarkerOptions().position(latLng2).title("location de test"));

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                        }
                    });

                }
            }

        });
        return latLng;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == locationRequestCode){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getLocation();
            }
        }
    }
}