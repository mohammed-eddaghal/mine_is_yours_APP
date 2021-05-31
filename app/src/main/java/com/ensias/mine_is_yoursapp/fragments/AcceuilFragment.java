package com.ensias.mine_is_yoursapp.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.ensias.mine_is_yoursapp.FireBaseTraitement;
import com.ensias.mine_is_yoursapp.Item;
import com.ensias.mine_is_yoursapp.MapsActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.Tool;
import com.ensias.mine_is_yoursapp.model.Outil;
import com.ensias.mine_is_yoursapp.model.User;
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
    static final String[] tolsName={"Bolt","Nail","Screwdriver","Bradawl","Handsaw","Nut","Screw","Wrench","Hammer","Hacksaw"};

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

    double lang,lat;

    String searchQuery;



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
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_acceuil, container, false);

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



        lang = getActivity().getIntent().getDoubleExtra("langitud",1);
        lat = getActivity().getIntent().getDoubleExtra("latitude",1);


        AcceuilFragment.LoadData loadData = new AcceuilFragment.LoadData();
        loadData.execute();



        return view;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mapContent();
    }

    public void mapContent(){

        LatLng mark = new LatLng(33.994, -6.736 );


        mMap.addMarker(new MarkerOptions().position(mark).title("markerTest"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 11));

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
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
                    Log.d("test",""+(++i));
                    Outil c = contact.getValue(Outil.class);
                    //Log.d("contact:: ", c.toString());
                    if( (c.getTitre().toLowerCase()).compareTo(query.toLowerCase())==0 && (c.getEtat().toLowerCase()).compareTo(("available").toLowerCase())==0)
                    {

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


                        Toast.makeText(getActivity(),""+c.getId(),Toast.LENGTH_LONG).show();
                    }
                    tools.add(c);
                }

                for(Outil outil:tools){
                    if( (outil.getTitre().toLowerCase()).compareTo(query.toLowerCase())==0 && (outil.getEtat().toLowerCase()).compareTo(("available").toLowerCase())==0)
                    Log.d("contact HOHOHO:: ", "zzzzzz "+outil.toString());
                }
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

        mapMarker=markerOptions;
        return markerOptions;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getPosition().longitude!=lang&&marker.getPosition().latitude!=lat){
            Toast.makeText(getActivity(),"latitude"+marker.getPosition().latitude+" | longitude"
                    +marker.getPosition().longitude,Toast.LENGTH_LONG).show();
        }
        return false;
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
            Log.d("test","log test 0");
        }

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

            Log.d("test","log test 1"+user.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            //key = databaseReference.push().getKey();
            //FireBaseTraitement.getUserByID(keyUser,databaseReference);
           // mapContent();
            Log.d("test","log test 2"+user.toString());
            //mapContent();
        }
    }
}