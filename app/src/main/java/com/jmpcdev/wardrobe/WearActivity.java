package com.jmpcdev.wardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class WearActivity extends AppCompatActivity {

    private ImageView imvWearHome, imvHead, imvSweater, imvPants, imvShoes;
    private Button btnWear;
    private ImvSelected imvsHead, imvsChest, imvsLegs, imvsFeet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);
        getSupportActionBar().hide();

        imvWearHome = findViewById(R.id.imvWearHome);
        imvHead = findViewById(R.id.imvHead);
        imvSweater = findViewById(R.id.imvSweater);
        imvPants = findViewById(R.id.imvPants);
        imvShoes = findViewById(R.id.imvShoes);
        btnWear = findViewById(R.id.btnWear);
        imvsHead = new ImvSelected();
        imvsChest  = new ImvSelected();
        imvsLegs = new ImvSelected();
        imvsFeet = new ImvSelected();




        imvWearHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WearActivity.this, MainActivity.class));
            }
        });

        imvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvsHead.imvClick(imvHead, R.drawable.head_selected, R.drawable.head);
            }
        });

        imvSweater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvsChest.imvClick(imvSweater, R.drawable.sweater_selected, R.drawable.sweater);
            }
        });

        imvPants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvsLegs.imvClick(imvPants, R.drawable.pants_selected, R.drawable.pants);
            }
        });

        imvShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvsFeet.imvClick(imvShoes, R.drawable.shoes_selected, R.drawable.shoes);
            }
        });

        btnWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WearActivity.this, OutfitActivity.class);
                i.putExtra("imvsHead", imvsHead.isSelected());
                i.putExtra("imvsChest", imvsChest.isSelected());
                i.putExtra("imvsLegs", imvsLegs.isSelected());
                i.putExtra("imvsFeet", imvsFeet.isSelected());
                startActivity(i);
            }
        });


    }







}
