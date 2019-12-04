package com.jmpcdev.wardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView imvWeather, imvWear, imvGarment, imvWash, imvWardrobe;
    private TextView txvTempMax, txvTempmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        imvWeather =  findViewById(R.id.imvWeather);
        imvWardrobe = findViewById(R.id.imvWardrobe);
        imvWash =     findViewById(R.id.imvWash);
        imvWear =     findViewById(R.id.imvWear);
        imvGarment =  findViewById(R.id.imvGarment);
        txvTempMax =  findViewById(R.id.txvTempMax);
        txvTempmin =  findViewById(R.id.txvTempMin);


        imvWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WearActivity.class));
            }
        });

        imvGarment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GarmentActivity.class));
            }
        });

        imvWardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WardrobeActivity.class));
            }
        });

        imvWash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WashActivity.class));
            }
        });


    }


}
