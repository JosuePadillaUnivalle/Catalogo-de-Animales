package com.example.catalogoanimales;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoImageView = findViewById(R.id.splashImageView);
        MaterialButton enterButton = findViewById(R.id.enterButton);
        
        // Cargar y aplicar la animación de fade in
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImageView.startAnimation(fadeIn);

        // Configurar el botón de ingreso
        enterButton.setOnClickListener(v -> {
            // Aplicar animación de fade out
            Animation fadeOut = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_out);
            logoImageView.startAnimation(fadeOut);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Iniciar MainActivity y finalizar SplashActivity
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        });
    }
} 