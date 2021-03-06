package com.ensias.mine_is_yoursapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.ensias.mine_is_yoursapp.fragments.AcceuilFragment;
import com.ensias.mine_is_yoursapp.fragments.AjouterOutilFragment;
import com.ensias.mine_is_yoursapp.fragments.BoiteMessagerieFragment;
import com.ensias.mine_is_yoursapp.fragments.OtherUserProfileFragment;
import com.ensias.mine_is_yoursapp.fragments.ProfilMenuFragment;
import com.ensias.mine_is_yoursapp.fragments.ProfileFragment;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MenuPrincipaleActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    private BoiteMessagerieFragment boiteMessagerieFragment;
    private ProfilMenuFragment profileFragment;
    private AcceuilFragment acceuilFragment;
    private AjouterOutilFragment ajouterOutilFragment;
    //Test
    //private OtherUserProfileFragment otherUserProfileFragment;

    BottomNavigationView bottomNavigationView;

    public AcceuilFragment getAcceuilFragment() {
        return acceuilFragment;
    }

    public void setAcceuilFragment(AcceuilFragment acceuilFragment) {
        this.acceuilFragment = acceuilFragment;
    }

    public BoiteMessagerieFragment getBoiteMessagerieFragment() {
        return boiteMessagerieFragment;
    }

    public ProfilMenuFragment getProfileFragment() {
        return profileFragment;
    }

    public void setProfileFragment(ProfilMenuFragment profileFragment) {
        this.profileFragment = profileFragment;
    }

    public void setBoiteMessagerieFragment(BoiteMessagerieFragment boiteMessagerieFragment) {
        this.boiteMessagerieFragment = boiteMessagerieFragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if ( firebaseUser == null ){
            Intent intent = new Intent(MenuPrincipaleActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public AjouterOutilFragment getAjouterOutilFragment() {
        return ajouterOutilFragment;
    }

    public void setAjouterOutilFragment(AjouterOutilFragment ajouterOutilFragment) {
        this.ajouterOutilFragment = ajouterOutilFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_principale);
        boiteMessagerieFragment = new BoiteMessagerieFragment();
        profileFragment  = new ProfilMenuFragment();
        ajouterOutilFragment = new AjouterOutilFragment();
        acceuilFragment = new AcceuilFragment();
        //TEST
        //otherUserProfileFragment = new OtherUserProfileFragment("MefeVkXLNKeIqwPqN4p8vpMIt9G3");

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, profileFragment).commit();

        // firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference();
        User user = new User("2",2d,2d,"name2","last","mail@gmail.com","default");
        //myRef.push().setValue(user);

        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        this.configureBottomView();
    }

    private void configureBottomView() {
       bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }


    // 3 - Update Main Fragment design
    private Boolean updateMainFragment(Integer integer){
        Fragment selectedFragment = null;
        switch (integer) {


            case R.id.profile:
                selectedFragment = profileFragment;
                break;
            case R.id.acceuil:
                selectedFragment = acceuilFragment;
                break;
            case R.id.boite_messagerie:
                selectedFragment = boiteMessagerieFragment;
                break;
            case R.id.ajouter_outil:
                selectedFragment = ajouterOutilFragment;
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, selectedFragment).commit();
        return true;
    }



}