package com.jmpcdev.wardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GarmentActivity extends AppCompatActivity {

    private ImageView imvGarmentHome;
    private EditText edtNameGarment, edtType, edtDesc, edtColor, edtTissue, edtTemperature, edtBrandname, edtGarments;
    private Button btnGarmentContinue;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garment);
        getSupportActionBar().hide();

        imvGarmentHome = findViewById(R.id.imvGarmentHome);
        edtColor = findViewById(R.id.edtColor);
        edtDesc = findViewById(R.id.edtDesc);
        edtNameGarment = findViewById(R.id.edtNameGarment);
        edtBrandname = findViewById(R.id.edtStore);
        edtTemperature = findViewById(R.id.edtTemperature);
        edtTissue = findViewById(R.id.edtTissue);
        edtType = findViewById(R.id.edtType);
        edtGarments = findViewById(R.id.edtCombine);
        btnGarmentContinue = findViewById(R.id.btnGarment);

        mAuth = FirebaseAuth.getInstance();

        imvGarmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GarmentActivity.this, MainActivity.class));
            }
        });

        btnGarmentContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGarment();
                startActivity(new Intent(GarmentActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }




    private void createGarment(){
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String name = edtNameGarment.getText().toString();
        String type = edtType.getText().toString();
        String description = edtDesc.getText().toString();
        String color = edtColor.getText().toString();
        String tissue = edtTissue.getText().toString();
        String temperature = edtTemperature.getText().toString();
        String brandname = edtBrandname.getText().toString();
        Garment garment = new Garment("prueba", name, type, description, color, tissue, temperature, brandname);
        garment.addUser(currentUser.getUid());
        System.out.println(garment.getUsers() + "///////////////////////////////////");
        DatabaseReference mDataBaseGarmentsGarmentId = mDataBase.child("garments").child(garment.getId());
        mDataBaseGarmentsGarmentId.child("image").setValue(garment.getImage());
        mDataBaseGarmentsGarmentId.child("type").setValue(garment.getType());
        mDataBaseGarmentsGarmentId.child("description").setValue(garment.getDescription());
        mDataBaseGarmentsGarmentId.child("color").setValue(garment.getColor());
        mDataBaseGarmentsGarmentId.child("tissue").setValue(garment.getTissue());
        mDataBaseGarmentsGarmentId.child("temperature").setValue(garment.getTemperature());
        mDataBaseGarmentsGarmentId.child("brandname").setValue(garment.getBrandName());
        mDataBaseGarmentsGarmentId.child("users").setValue(garment.getUsers());

    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            /**
            FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            User inneruser = dataSnapshot.getValue(User.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            */

        } else {
            startActivity(new Intent(GarmentActivity.this, LoginActivity.class));
        }
    }


}
