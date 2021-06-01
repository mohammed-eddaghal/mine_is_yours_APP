package com.ensias.mine_is_yoursapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ensias.mine_is_yoursapp.Item;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.Tool;
import com.ensias.mine_is_yoursapp.model.Outil;
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


public class AcceuilFragment extends Fragment  implements OnMapReadyCallback, SearchView.OnQueryTextListener, GoogleMap.OnMarkerClickListener {
    static final String[] tolsName = {"Bolt", "Nail", "Screwdriver", "Bradawl", "Handsaw", "Nut", "Screw", "Wrench", "Hammer", "Hacksaw"};

    SupportMapFragment mapFragment;
    List<Item> itemList;
    List<Item> listItemMap = new ArrayList<>();

    List<Outil> tools=new ArrayList<>();


    List<Marker> markers = new ArrayList<>();

    Marker marker;

    private GoogleMap mMap;

    String userPath="users", toolPath="outils";

    final static FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
    final static String keyUser = userFirebase.getUid();

    double lang, lat;

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


    public AcceuilFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acceuil, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        user = new User();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*mapFragment = (SupportMapFragment)( getActivity().getSupportFragmentManager())
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);


        //FireBaseTraitement.getListOfTools();

        editsearch = view.findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

/*
        lang = getActivity().getIntent().getDoubleExtra("langitud",1);
        lat = getActivity().getIntent().getDoubleExtra("latitude",1);
*/
        AcceuilFragment.LoadData loadData = new AcceuilFragment.LoadData();
        loadData.execute();


        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapContent();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapContent() {
        System.out.println("Hello World From Map");
        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/").getReference("users");//.child(user.getUid());
        ValueEventListener listener = new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);
                    System.out.println(user.getLastName());
                    if((user.getId()).equals(keyUser)) {
                        LatLng mark = new LatLng(user.getLantitude(), user.getLangitude());
                        System.out.println("Latitude"+user.getLantitude());
                        System.out.println("Langitude"+user.getLangitude());
                        System.out.println("Last Name"+user.getLastName());
                        mMap.addMarker(new MarkerOptions().position(mark).title("markerTest"));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 11));

                        mMap.getUiSettings().setCompassEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);

                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(listener);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        DatabaseReference databaseReference = firebaseDatabase.getReference(toolPath);
        final List<Outil> tools= new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot contact : snapshot.getChildren()) {
                    Outil c = contact.getValue(Outil.class);
                    //Log.d("contact:: ", c.toString());
                    if( (c.getTitre().toLowerCase()).compareTo(query.toLowerCase())==0 && (c.getEtat().toLowerCase()).compareTo(("available").toLowerCase())==0)
                    {
                        i++;
                        Log.d("contact HiHiHi:: ", "zzzzzz "+c.toString());

                        User userHandel= new User();
                        DatabaseReference databaseReference = firebaseDatabase.getReference(userPath);
                        databaseReference.child(c.getIdOwner()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {

                                if (!task.isSuccessful()) {
                                    Log.e("firebase static", "Error getting data", task.getException());
                                }
                                else {
                                    userHandel.cloneUser(task.getResult().getValue(User.class));
                                    //user3.setLastName(task.getResult().getValue(User.class).getLastName());
                                    Log.d("firebase test 00", userHandel.toString());
                                    Log.d("firebase test 11", task.getResult().getValue(User.class).toString());
                                    drawMarker(new LatLng(userHandel.getLantitude(),userHandel.getLangitude()),BitmapDescriptorFactory.HUE_CYAN,c);
                                }
                            }
                        });

                        //userHandel.cloneUser(FireBaseTraitement.getUserByID(c.getIdOwner(),userPath,firebaseDatabase) );
                        //Log.d("contact KhKhKh:: ", FireBaseTraitement.getUserByID(c.getIdOwner(),userPath,firebaseDatabase) .toString());
                        //drawMarker(new LatLng(userHandel.getLantitude(),userHandel.getLangitude()),BitmapDescriptorFactory.HUE_RED,c);


                       // Toast.makeText(getActivity(),""+c.getId(),Toast.LENGTH_LONG).show();
                    }
                    tools.add(c);
                }

                if(i==0)Toast.makeText(getActivity(), query +" not found",Toast.LENGTH_LONG).show();


            }


            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }


    private MarkerOptions drawMarker(LatLng point,float id,Outil item) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(id));

        // Adding marker on the Google Map
        marker = mMap.addMarker(markerOptions.title(item.getId()));
        markers.add(marker);

        mMap.setOnMarkerClickListener(this);

        mapMarker = markerOptions;
        return markerOptions;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getPosition().longitude != lang
                && marker.getPosition().latitude != lat) {

            DatabaseReference databaseReference = firebaseDatabase.getReference(toolPath);
            databaseReference.child(marker.getTitle()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                //User user3=new User();
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if (!task.isSuccessful()) {
                        Log.e("firebase static", "Error getting data", task.getException());
                    }
                    else {
                        Toast.makeText(getActivity(),marker.getTitle(),Toast.LENGTH_LONG).show();
                        /*getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.activity_main_frame_layout, new OutilDetailsFragment(task.getResult().getValue(Outil.class) , this).commit();*/
                    }
                }
            });

           }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        System.out.println("Hello World From Get Current Location");
        //initialize LocationManger
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        //check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //when location service is enabled
            //get Location
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //initialize Location
                    Location location = task.getResult();
                    //check condition
                    if (location != null) {
                        System.out.println("Heloooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                        lang = location.getLongitude();
                        lat = location.getLatitude();
                        System.out.println("****Lang="+lang);
                        System.out.println("****Lat="+lat);
                        //Modify User Longitude And Latitude
                        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/").getReference("users");
                        System.out.println(keyUser);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    user = snapshot1.getValue(User.class);
                                    System.out.println(user.getLastName());
                                    if ((user.getId()).equals(keyUser)) {
                                        user.setLangitude(lang);
                                        user.setLantitude(lat);
                                        String key = snapshot1.getKey();
                                        databaseReference.child(key).setValue(user);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        //when location result is null
                        //initialization location request

                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //initilize Location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                //Inisilize location
                                Location location1 = locationResult.getLastLocation();

                            }
                        };

                        //request location update
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            //when location service is not innabled
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        System.out.println("Lang=" + lang + "-" + user.getLangitude());
        System.out.println("Lat=" + lat + "-" + user.getLantitude());

    }

   /* public void getUser(@NonNull DataSnapshot snapshot) throws InterruptedException {
        //User userx = FireBaseTraitement.findUserByID(keyUser,snapshot,MapsActivity.this);
        User userx= FireBaseTraitement.getUserByID(keyUser,databaseReference);
        Log.d("tagTest","firebase traitment"+userx.getLastName()+"/ "+userx.getLantitude()+"/ "+userx.getLangitude());
        user.setId(userx.getId());
        user.setLastName(userx.getLastName());
        user.setLangitude(userx.getLangitude());
        user.setLantitude(userx.getLantitude());
        Log.d("tagTestDDD","firebase traitment"+user.getLastName()+"/ "+user.getLantitude()+"/ "+user.getLantitude());
        mapContent();
    }*/

    public class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            firebaseDatabase = FirebaseDatabase.getInstance();
            // below line is used to get reference for our database.
            databaseReference = firebaseDatabase.getReference(userPath);
            Log.d("test", "log test 0");
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(Void... voids) {
            // below line is used to get the
            // instance of our FIrebase database.

           /* try {
                user.cloneUser(FireBaseTraitement.getUserByID(keyUser,databaseReference));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            //databaseReference.addValueEventListener(MapsActivity.this);

            Log.d("test", "log test 1" + user.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            //key = databaseReference.push().getKey();
            //FireBaseTraitement.getUserByID(keyUser,databaseReference);
            // mapContent();
            Log.d("test", "log test 2" + user.toString());
            //mapContent();
        }


    }
}