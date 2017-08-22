package com.loya.android.javadevelopers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {


    //private field for the textview
    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        splashText = (TextView) findViewById(R.id.eko_textview);

        //sets the animation of the textview
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        splashText.startAnimation(animation);

        //intent to the Developer Activity
        final Intent intent = new Intent(this, DeveloperActivity.class);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start(); //starts the timer
    }
}
