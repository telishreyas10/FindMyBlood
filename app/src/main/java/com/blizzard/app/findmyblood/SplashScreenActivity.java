package com.blizzard.app.findmyblood;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        }, 3500);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            Intent i=new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
