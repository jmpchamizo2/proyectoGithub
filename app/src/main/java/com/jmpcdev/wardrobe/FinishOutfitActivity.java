package com.jmpcdev.wardrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class FinishOutfitActivity extends AppCompatActivity {
    private RecyclerView recyclerFinish, recyclerCobine;
    private FloatingActionButton fabFinish;
    private FirebaseAuth mAuth;
    private ArrayList<String> idsSelected;
    private SelectableGarmentAdapter sgAdapter;

    private List<SelectableOutfitGarment> selectableOutfitGarmentsSelecteds = new ArrayList<>();
    private List<SelectableGarment> selectableGarmentsCombineTemp = new ArrayList<>();
    private final List<String> tempIdsCombine = new ArrayList<>();
    private ArrayList<String> partialIds = new ArrayList<>();
    private ArrayList<String> partialNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_outfit);

        mAuth = FirebaseAuth.getInstance();
        recyclerFinish = findViewById(R.id.recyclerFinish);
        recyclerFinish.setHasFixedSize(true);
        recyclerFinish.setLayoutManager(new LinearLayoutManager(this));
        recyclerCobine = findViewById(R.id.recyclerFinishCombine);
        recyclerCobine.setHasFixedSize(true);
        recyclerCobine.setLayoutManager(new LinearLayoutManager(this));
        fabFinish = findViewById(R.id.fabFinish);
        idsSelected = updateIdsSelected();
    }


    private ArrayList<String> updateIdsSelected(){
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
            for(String id : idsSelected){
                FirebaseDatabase.getInstance().getReference().child("garments").child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                getSelectablesOutfitGarmentsSelected(dataSnapshot);
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }



        }
    }


    private void getSelectablesOutfitGarmentsSelected(DataSnapshot dataSnapshot){
        Garment garment = dataSnapshot.getValue(Garment.class);
        final SelectableOutfitGarment sog = new SelectableOutfitGarment(garment, false);
        sog.setListener(new SelectableOutfitGarment.ChangeListener() {
            @Override
            public void onChange(boolean selected) {
                updateSelectablesGarmentCombine(selected, sog.getId());
            }
        });
        selectableOutfitGarmentsSelecteds.add(sog);
        if(selectableOutfitGarmentsSelecteds.size() == idsSelected.size()){
            SelectableOutfitGarmentAdapter sogAdapter = new SelectableOutfitGarmentAdapter(selectableOutfitGarmentsSelecteds);
            recyclerFinish.setAdapter(sogAdapter);
        }

    }


    private void updateSelectablesGarmentCombine(final boolean selected, final String id){
        final int[] count = {0};
        FirebaseDatabase.getInstance().getReference().child("garments").child(id).child("combine")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            if(selected){
                                partialIds.add(id);
                                tempIdsCombine.add(ds.getValue(String.class));
                            } else {
                                partialIds.remove(id);
                                tempIdsCombine.remove(ds.getValue(String.class));
                            }
                            count[0]++;
                            if(count[0] == dataSnapshot.getChildrenCount()){
                                getSelectableGarmentOfTempCombine(tempIdsCombine);

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void getSelectableGarmentOfTempCombine(final List<String> tempIdsCombine){
        final List<SelectableGarment> selectableGarmentsCombineTemp = new ArrayList<>();
        if(tempIdsCombine.size() == 0){
            sgAdapter = new SelectableGarmentAdapter(new ArrayList<SelectableGarment>());
            recyclerCobine.setAdapter(sgAdapter);
            fabFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outfitSelected(selectableGarmentsCombineTemp);
                }
            });
            return;
        }
        for(String id : tempIdsCombine){
            FirebaseDatabase.getInstance().getReference().child("garments").child(id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Garment garmentTemp = dataSnapshot.getValue(Garment.class);
                            SelectableGarment selectableGarmentTemp = new SelectableGarment(garmentTemp, false);
                            selectableGarmentsCombineTemp.add(selectableGarmentTemp);
                            if(selectableGarmentsCombineTemp.size() == tempIdsCombine.size()){
                                sgAdapter = new SelectableGarmentAdapter(selectableGarmentsCombineTemp);
                                recyclerCobine.setAdapter(sgAdapter);
                                fabFinish.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        outfitSelected(selectableGarmentsCombineTemp);
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


    private void outfitSelected(List<SelectableGarment> selectableGarments){
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        Intent i = new Intent(FinishOutfitActivity.this, SelectOutfit.class);
        for(int j = 0; j < this.partialIds.size() ; j += 2){
            ids.add(this.partialIds.get(j));
        }
        names.addAll(this.partialNames);
        for (SelectableGarment s : selectableGarments) {
            if (s.isSelected()) {
                ids.add(s.getId());
                names.add(s.getName());
            }
        }

        for(String s : ids){
            System.out.println("###########################" + s);
        }

        if (ids.size() > 0) {
            i.putStringArrayListExtra("ids", ids);
            i.putStringArrayListExtra("names", names);
        }

        startActivity(i);
    }



}