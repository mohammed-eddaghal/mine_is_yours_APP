package com.ensias.mine_is_yoursapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class ChatActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        this.configureBottomView();
    }

    private void configureBottomView() {
       //bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }


    // 3 - Update Main Fragment design
    private Boolean updateMainFragment(Integer integer){
        switch (integer) {
            case R.id.action_android:
                break;
            case R.id.action_logo:
                break;
            case R.id.action_landscape:
                break;
        }
        return true;
    }



}