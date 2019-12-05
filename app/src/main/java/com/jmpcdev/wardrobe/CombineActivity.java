package com.jmpcdev.wardrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CombineActivity extends AppCompatActivity{
    private RecyclerView mRecycler;
    private RecyclerView.Adapter mAdapter;

    private FloatingActionButton boton;

    private FirebaseAuth mAuth;


    SelectableGarmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine);

        mAuth = FirebaseAuth.getInstance();

        mRecycler = findViewById(R.id.recyclerCombine);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        boton = findViewById(R.id.floatingActionButton3);


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




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
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final List<String> garmetnsId = (ArrayList<String>) dataSnapshot.getValue();
                            //final List<Garment> garments = new ArrayList<>();

                            final List<SelectableGarment> selectableGarments = new ArrayList<>();


                            for (String id : garmetnsId) {
                                FirebaseDatabase.getInstance().getReference().child("garments").child(id)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Garment garment = dataSnapshot.getValue(Garment.class);
                                                final SelectableGarment selectableGarment = new SelectableGarment(garment, false);
                                                //garments.add(garment);
                                                selectableGarments.add(selectableGarment);
                                                if (selectableGarments.size() == garmetnsId.size()) {
                                                    adapter = new SelectableGarmentAdapter(selectableGarments);
                                                    mRecycler.setAdapter(adapter);
                                                    boton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                           ArrayList<String> ids = new ArrayList<>();
                                                           ArrayList<String> names = new ArrayList<>();
                                                           Intent i = new Intent(CombineActivity.this, GarmentActivity.class);
                                                           for(SelectableGarment s : selectableGarments){
                                                               if(s.isSelected()){
                                                                   ids.add(s.getId());
                                                                   names.add(s.getName());
                                                               }
                                                           }
                                                            if (ids.size() > 0) {
                                                                i.putStringArrayListExtra("ids", ids);
                                                                i.putStringArrayListExtra("names", names);
                                                            }

                                                           startActivity(i);
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }
    }


}

