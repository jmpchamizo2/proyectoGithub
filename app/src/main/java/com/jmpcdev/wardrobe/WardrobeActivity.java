package com.jmpcdev.wardrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class WardrobeActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private RecyclerView.Adapter mAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe);

        mAuth = FirebaseAuth.getInstance();

        mRecycler = findViewById(R.id.recycler_wardrobe);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("garments")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Garment> garments = (List<Garment>) dataSnapshot.getValue();
                            if(garments == null){
                                garments = new ArrayList<>();
                            }
                            mAdapter = new GarmentAdapter(garments);
                            mRecycler.setAdapter(mAdapter);
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
        }
    }



}
