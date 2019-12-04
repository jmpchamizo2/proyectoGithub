package com.jmpcdev.wardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

import java.util.List;




public class GarmentActivity extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener {

    private ImageView imvGarmentHome;
    private EditText edtNameGarment, edtDesc, edtColor, edtTissue, edtBrandname;
    private Button btnGarmentContinue, btnCombine;
    private MultiSpinner multiSpinnerTemperature;
    private Spinner spinnerType;

    private List<String> temperatures = new ArrayList<String>();
    private List<TemperaturesGarment> temperaturesGarment = new ArrayList<TemperaturesGarment>();
    private List<String> types = new ArrayList<>();


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garment);
        getSupportActionBar().hide();

        temperatures.add("<0");
        temperatures.add("0-5");
        temperatures.add("5-10");
        temperatures.add("10-15");
        temperatures.add("15-20");
        temperatures.add("20-25");
        temperatures.add("25-30");
        temperatures.add(">30");

        types.add(getString(R.string.hat));
        types.add(getString(R.string.sweater));
        types.add(getString(R.string.shirt));
        types.add(getString(R.string.tshirt));
        types.add(getString(R.string.pants));
        types.add(getString(R.string.jeans));
        types.add(getString(R.string.skirt));
        types.add(getString(R.string.dress));
        types.add(getString(R.string.tracksuit));
        types.add(getString(R.string.shoes));
        types.add(getString(R.string.sports));
        types.add(getString(R.string.coat));
        types.add(getString(R.string.swimsuit));


        imvGarmentHome = findViewById(R.id.imvGarmentHome);
        edtColor = findViewById(R.id.edtColor);
        edtDesc = findViewById(R.id.edtDesc);
        edtNameGarment = findViewById(R.id.edtNameGarment);
        edtBrandname = findViewById(R.id.edtStore);
        edtTissue = findViewById(R.id.edtTissue);
        btnCombine = findViewById(R.id.btnCombine);
        btnGarmentContinue = findViewById(R.id.btnGarment);
        multiSpinnerTemperature = (MultiSpinner) findViewById(R.id.multispinnerTemperature);
        multiSpinnerTemperature.setItems(temperatures, getString(R.string.select_temperature),  this);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);






        ArrayAdapter<String> adapater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, types);
        spinnerType.setPrompt(getString(R.string.select_type));
        spinnerType.setAdapter(adapater);

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

        btnCombine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GarmentActivity.this, CombineActivity.class));
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
        String type = spinnerType.getSelectedItem().toString();
        String description = edtDesc.getText().toString();
        String color = edtColor.getText().toString();
        String tissue = edtTissue.getText().toString();

        String brandname = edtBrandname.getText().toString();
        final Garment garment = new Garment("prueba", name, type, description, color, tissue, temperaturesGarment, brandname);
        garment.addUser(currentUser.getUid());
        DatabaseReference mDataBaseGarmentsGarmentId = mDataBase.child("garments").child(garment.getId());
        mDataBaseGarmentsGarmentId.child("name").setValue(garment.getName());
        mDataBaseGarmentsGarmentId.child("image").setValue(garment.getImage());
        mDataBaseGarmentsGarmentId.child("type").setValue(garment.getType());
        mDataBaseGarmentsGarmentId.child("description").setValue(garment.getDescription());
        mDataBaseGarmentsGarmentId.child("color").setValue(garment.getColor());
        mDataBaseGarmentsGarmentId.child("tissue").setValue(garment.getTissue());
        mDataBaseGarmentsGarmentId.child("temperature").setValue(garment.getTemperature());
        mDataBaseGarmentsGarmentId.child("brandname").setValue(garment.getBrandName());
        mDataBaseGarmentsGarmentId.child("users").setValue(garment.getUsers());
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("garments")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> garmentsId = (List<String>) dataSnapshot.getValue();
                        if(garmentsId == null){
                            garmentsId = new ArrayList<>();
                        }
                        garmentsId.add(garment.getId());
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(currentUser.getUid()).child("garments").setValue(garmentsId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
            //startActivity(new Intent(GarmentActivity.this, LoginActivity.class));
        }
    }


    @Override
    public void onItemsSelected(boolean[] selected) {

        for(int i = 0; i < selected.length; i++){
            if(selected[i]){
                temperaturesGarment.add(TemperaturesGarment.values()[i]);
            }
        }
    }
}
