package com.example.bankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class main extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageView logo = findViewById(R.id.logo);


        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);

        TranslateAnimation translate = new TranslateAnimation(
                0, 0,   200, 0
        );
        translate.setDuration(1000);
        translate.setFillAfter(true);

        ScaleAnimation zoomIn = new ScaleAnimation(
                0.5f, 1f,0.5f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        zoomIn.setDuration(1000);
        zoomIn.setFillAfter(true);

        RotateAnimation rotate = new RotateAnimation(
                0f, 360f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(1000);
        rotate.setFillAfter(true);

       logo.startAnimation(fadeIn);
        logo.startAnimation(translate);
        logo.startAnimation(zoomIn);
        logo.startAnimation(rotate);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(main.this, signup.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
