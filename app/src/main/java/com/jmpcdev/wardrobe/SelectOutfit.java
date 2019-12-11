package com.jmpcdev.wardrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectOutfit extends AppCompatActivity {
    private RecyclerView recycletSelectOutfit;
    private FloatingActionButton fabSelectOutfit;
    private FirebaseAuth mAuth;
    private ArrayList<String> idsSelected;
    private List<Garment> outfitGarments = new ArrayList<Garment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_outfit);


        mAuth = FirebaseAuth.getInstance();
        recycletSelectOutfit = findViewById(R.id.recyclerSelectOutfit);
        recycletSelectOutfit.setHasFixedSize(true);
        recycletSelectOutfit.setLayoutManager(new LinearLayoutManager(this));
        fabSelectOutfit = findViewById(R.id.fabSelectOutfit);
        idsSelected = updateIdsSelected();

        fabSelectOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Outfit outfit = new Outfit("prueba", "prueba", idsSelected);
                DatabaseReference outfitId = FirebaseDatabase.getInstance().getReference().child("outfits").child(mAuth.getCurrentUser().getUid()).child(outfit.getId());
                outfitId.child("name").setValue(outfit.getName());
                outfitId.child("date").setValue(outfit.getDate());


            }
        });
    }

    private ArrayList<String> updateIdsSelected() {
        Bundle data = this.getIntent().getExtras();
        if (data != null && data.getStringArrayList("ids") != null) {
            return data.getStringArrayList("ids");
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null && idsSelected != null && idsSelected.size() > 0) {
            for (String id : idsSelected) {
                FirebaseDatabase.getInstance().getReference().child("garments").child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                getOutfitGarmentsSelected(dataSnapshot);
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }


        }
    }


    private void getOutfitGarmentsSelected(DataSnapshot dataSnapshot){

        Garment garment = dataSnapshot.getValue(Garment.class);
        outfitGarments.add(garment);
        if(outfitGarments.size() == idsSelected.size()){
            OutfitAdapter sogAdapter = new OutfitAdapter(outfitGarments, SelectOutfit.this);
            recycletSelectOutfit.setAdapter(sogAdapter);
        }

    }





}
