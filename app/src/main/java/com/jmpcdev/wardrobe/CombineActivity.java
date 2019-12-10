package com.jmpcdev.wardrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.Arrays;
import java.util.List;

public class CombineActivity extends AppCompatActivity{
    private RecyclerView mRecycler;


    private FloatingActionButton btnUpdateGarmentCombination;

    private FirebaseAuth mAuth;
    private ArrayList<String> idsSelected;


    SelectableGarmentAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine);

        mAuth = FirebaseAuth.getInstance();
        mRecycler = findViewById(R.id.recyclerCombine);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        btnUpdateGarmentCombination = findViewById(R.id.floatingActionButton3);
        idsSelected = updateIdsSelected();


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
                            getUserGarments(dataSnapshot);
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }
    }


    private void getUserGarments(DataSnapshot dataSnapshot){
        final List<String> garmentsId = (ArrayList<String>) dataSnapshot.getValue();
        if(garmentsId == null){
            return;
        }
        final List<SelectableGarment> selectableGarments = new ArrayList<>();
        for (String id : garmentsId) {
            FirebaseDatabase.getInstance().getReference().child("garments").child(id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Garment garment = dataSnapshot.getValue(Garment.class);
                            createSelectableGarments(garment, selectableGarments);
                            createRecyclerAtFinish(selectableGarments, garmentsId);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }
    }



    private void createRecyclerAtFinish(final List<SelectableGarment> selectableGarments, List<String> garmentsId){
        if (selectableGarments.size() == garmentsId.size()) {
            adapter = new SelectableGarmentAdapter(selectableGarments);
            mRecycler.setAdapter(adapter);
            btnUpdateGarmentCombination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateGarmentCombination(selectableGarments);
                }
            });
        }
    }


    private void updateGarmentCombination(List<SelectableGarment> selectableGarments){
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        Intent i = new Intent(CombineActivity.this, GarmentActivity.class);
        for (SelectableGarment s : selectableGarments) {
            if (s.isSelected()) {
                ids.add(s.getId());
                names.add(s.getName());
            }
        }
        if (ids.size() > 0) {
            i.putStringArrayListExtra("ids", ids);
            i.putStringArrayListExtra("names", names);
        }

        prepareExtras(i);
        startActivity(i);
    }

    private void createSelectableGarments(Garment garment,  List<SelectableGarment> selectableGarments){
        SelectableGarment selectableGarment = new SelectableGarment(garment, false);
        if (idsSelected != null) {
            if(idsSelected.contains(garment.getId())){
                selectableGarment = new SelectableGarment(garment, true);
            }
        }
        selectableGarments.add(selectableGarment);



    }



    private ArrayList<String> updateIdsSelected(){
        Bundle data = this.getIntent().getExtras();
        if (data != null && data.getStringArrayList("ids") != null) {
            return data.getStringArrayList("ids");
        } else {
            return new ArrayList<>();
        }
    }


    private void prepareExtras(Intent i){
        Bundle data = this.getIntent().getExtras();
        Intent intent = getIntent();
        if (data != null){
            if(data.getString("nameGarment") != null){
                i.putExtra("nameGarment", data.getString("nameGarment"));
            }
            if(data.getString("brandName") != null){
                i.putExtra("brandName", data.getString("brandName"));
            }
            if(data.getString("description") != null){
                i.putExtra("description", data.getString("description"));
            }
            if(data.getString("color") != null){
                i.putExtra("color", data.getString("color"));
            }
            if(data.getString("tissue") != null){
                i.putExtra("tissue", data.getString("tissue"));
            }
            if(data.getIntegerArrayList("itemsSelected") != null) {
                i.putIntegerArrayListExtra("itemsSelected", data.getIntegerArrayList("itemsSelected"));
            }
            if(getIntent().getByteArrayExtra("bitmap") != null){
                i.putExtra("bitmap", getIntent().getByteArrayExtra("bitmap"));
            }
            i.putExtra("itemSelected", data.getInt("itemSelected"));

        }

    }


}

