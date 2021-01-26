package com.example.riverislandnn4m;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Activity where Main Menu is displayed
public class MainMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.women);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            //Setting up listener for Women section
            if (item.getItemId() == R.id.women){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            }

            //TODO listeners for the rest of sections once created

            return false;
        });
    }

}