package com.example.riverislandnn4m;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

//Activity to Open the app with start screen
public class StartScreen extends AppCompatActivity {

    ImageView riverLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        riverLogo = findViewById(R.id.riverIslandLogo);

        //Creating and setting new animation with fade in and fade out animation
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        riverLogo.setAnimation(animation);

        //Starting new Main Menu activity afterwards
        new Handler().postDelayed(this::startNewActivity, 6000);
    }

    //Main Menu activity
    public void startNewActivity(){
        Intent mainIntent = new Intent(this,MainMenuActivity.class);
        startActivity(mainIntent);
    }


}