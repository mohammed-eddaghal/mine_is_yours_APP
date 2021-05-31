package com.ensias.mine_is_yoursapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.ensias.mine_is_yoursapp.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SearchView.OnQueryTextListener, GoogleMap.OnMarkerClickListener {

    static final String[] tolsName={"Bolt","Nail","Screwdriver","Bradawl","Handsaw","Nut","Screw","Wrench","Hammer","Hacksaw"};

    SupportMapFragment mapFragment;
    List<Item> itemList;
    List<Item> listItemMap = new ArrayList<>();
    List<Tool> tools=new ArrayList<>();

    List<Marker> markers = new ArrayList<>();

    Marker marker;

    private GoogleMap mMap;
    String userPath="users", toolPath="types";
    final static FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
    final static String keyUser = userFirebase.getUid();

    double lang,lat;

    String searchQuery;

    FusedLocationProviderClient fusedLocationProviderClient;


    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    //instance of user classe
    User user;

    SearchView editsearch;

    String key;
    private MarkerOptions mapMarker;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        user = new User();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        //check permission
        if(ActivityCompat.checkSelfPermission(MapsActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this
                , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            // if both permissions garanted => call methode
            getCurrentLocation();


        }else{
            //when permission is not garanted
            //request permission

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            },100);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //FireBaseTraitement.getListOfTools();

        editsearch = findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);


/*
        lang = getIntent().getDoubleExtra("langitud",1);
        lat = getIntent().getDoubleExtra("latitude",1);
*/
        new LoadData().execute();



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    public void mapContent(){
        if(user.getLastName()!=null){
            Log.d("tagTestxx","xxx ||| "+user.getLastName()+"/ "+user.getLantitude()+"/ "+user.getLangitude());
            //lang = user.getLangitude();
            //lat = user.getLantitude();
        }else{
            Log.d("tagTestxx","xxx |||");
            //lang = getIntent().getDoubleExtra("langitud",1);
            //lat = getIntent().getDoubleExtra("latitude",1);
        }

        // Add a marker in Sydney and move the camera

        LatLng mark = new LatLng(lat, lang);


        mMap.addMarker(new MarkerOptions().position(mark).title(user.getLastName()));




        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 16));

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery=query;
        setItemsList();
        for (Marker marker:markers){
            marker.remove();
        }
        markers.clear();

        int x=0;
        //Toast.makeText(MapsActivity.this,searchQuery,Toast.LENGTH_LONG).show();
        Toast.makeText(MapsActivity.this,
                lat+" || "+lang,
                Toast.LENGTH_LONG).show();
        for(Item item:itemList){

            if((item.getTool().getToolName().toLowerCase()).compareTo(query.toLowerCase())==0){
                drawMarker(new LatLng(item.getUser().getLantitude(),item.getUser().getLangitude()),
                        BitmapDescriptorFactory.HUE_BLUE, item);
            }
        }

        if(tools.size()==0)Log.d("testOne","tools list is umpty");
        for(Tool tool:tools){
            Log.d("testZero",tool.getToolName());
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

    public void setItemsList(){

        int z=0;
        itemList = new ArrayList<>();
        for(int i=0;i<50;i++){
            double x = Math.random()/100;
            double y = Math.random()/100;
            User user= new User(key,lang+x,lat+y, null, null,null, null);
            itemList.add(new Item(user,new Tool(tolsName[z++])));
            if(z==10)z=0;

        }
    }

    private MarkerOptions drawMarker(LatLng point,float id,Item item) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(id));

        // Adding marker on the Google Map
        marker = mMap.addMarker(markerOptions.title(item.getUser().getLastName()+"--"+item.getTool().getToolName()));
        markers.add(marker);

        mMap.setOnMarkerClickListener(this);

        mapMarker=markerOptions;
        return markerOptions;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getPosition().longitude!=lang&&marker.getPosition().latitude!=lat){
            Toast.makeText(MapsActivity.this,"latitude"+marker.getPosition().latitude+" | longitude"
                    +marker.getPosition().longitude,Toast.LENGTH_LONG).show();
        }
        return false;
    }



    public void getUser(@NonNull DataSnapshot snapshot) throws InterruptedException {
        //User userx = FireBaseTraitement.findUserByID(keyUser,snapshot,MapsActivity.this);

        User userx= FireBaseTraitement.getUserByID(keyUser,databaseReference);
        Log.d("tagTest","firebase traitment"+userx.getLastName()+"/ "+userx.getLantitude()+"/ "+userx.getLangitude());

        user.setId(userx.getId());
        user.setLastName(userx.getLastName());
        user.setLangitude(userx.getLangitude());
        user.setLantitude(userx.getLantitude());
        Log.d("tagTestDDD","firebase traitment"+user.getLastName()+"/ "+user.getLantitude()+"/ "+user.getLantitude());

        mapContent();
    }

    public class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            firebaseDatabase = FirebaseDatabase.getInstance();
            // below line is used to get reference for our database.
            databaseReference = firebaseDatabase.getReference(userPath);
            Log.d("test","log test 0");

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // below line is used to get the
            // instance of our FIrebase database.

            try {
                user.cloneUser(FireBaseTraitement.getUserByID(keyUser,databaseReference));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //databaseReference.addValueEventListener(MapsActivity.this);

            Log.d("test","log test 1"+user.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            //key = databaseReference.push().getKey();
            //FireBaseTraitement.getUserByID(keyUser,databaseReference);
            mapContent();
            Log.d("test","log test 2"+user.toString());
            //mapContent();
        }
    }
    /*public void x(){
        final User user3=new User();
        databaseReference.child(keyUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    // user3.setId(((User)((HashMap)task.getResult().getValue()).entrySet().iterator().next()).getId());


                    Log.d("firebase", task.getResult().getValue(User.class).toString());

                }
            }
        });
    }*/
    /*public void setToolsList(){
        databaseReference = firebaseDatabase.getReference(toolPath);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    //toolList.add(dataSnapshot.getValue(Tool.class));
                    tools.add(dataSnapshot.getValue(Tool.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //initialize LocationManger
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //check condition
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //when location service is enabled
            //get Location
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //initialize Location
                    Location location = task.getResult();
                    //check condition
                    if (location != null){
                        lang= location.getLongitude();
                        lat= location.getLatitude();
                        //Modify User Longitude And Latitude
                        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/").getReference("users");
                        System.out.println(keyUser);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    User user = snapshot1.getValue(User.class);
                                    System.out.println(user.getLastName());

                                    if((user.getId()).equals(keyUser)){
                                        user.setLangitude(lang);
                                        user.setLantitude(lat);
                                        String key=snapshot1.getKey();
                                        databaseReference.child(key).setValue(user);
                                        break;
                                    }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else{
                        //when location result is null
                        //initialization location request

                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //initilize Location call back
                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                //Inisilize location
                                Location location1 = locationResult.getLastLocation();

                            }
                        };

                        //request location update
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        }else {
            //when location service is not innabled
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }

}