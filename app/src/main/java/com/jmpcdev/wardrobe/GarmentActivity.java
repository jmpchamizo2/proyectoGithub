package com.jmpcdev.wardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GarmentActivity extends AppCompatActivity {

    private ImageView imvGarmentHome;
    private EditText edtNameGarment, edtType, edtDesc, edtColor, edtTissue, edtTemperature, edtStore, edtCombine;
    private Button btnGarmentContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garment);

        imvGarmentHome = findViewById(R.id.imvGarmentHome);
        edtColor = findViewById(R.id.edtColor);
        edtDesc = findViewById(R.id.edtDesc);
        edtNameGarment = findViewById(R.id.edtNameGarment);
        edtStore = findViewById(R.id.edtStore);
        edtTemperature = findViewById(R.id.edtTemperature);
        edtTissue = findViewById(R.id.edtTissue);
        edtType = findViewById(R.id.edtType);
        edtCombine = findViewById(R.id.edtCombine);
        btnGarmentContinue = findViewById(R.id.btnGarment);



        imvGarmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GarmentActivity.this, MainActivity.class));
            }
        });
    }

}
