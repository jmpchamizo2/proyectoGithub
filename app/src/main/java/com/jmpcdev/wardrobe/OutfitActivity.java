package com.jmpcdev.wardrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OutfitActivity extends AppCompatActivity {
    private boolean head, chest, legs, feet;

    private RecyclerView mRecycler;
    private FloatingActionButton fabOutfit;
    private FirebaseAuth mAuth;
    private ArrayList<String> idsSelected;
    private String filter = "";

    SelectableGarmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit);


        mAuth = FirebaseAuth.getInstance();
        mRecycler = findViewById(R.id.recyclerOutfit);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        fabOutfit = findViewById(R.id.fabOutfit);
        getExtras();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void getExtras(){
        Bundle data = this.getIntent().getExtras();
        if (data != null) {
            head = data.getBoolean("imvsHead");
            chest = data.getBoolean("imvsChest");
            legs = data.getBoolean("imvsLegs");
            feet = data.getBoolean("imvsFeet");
        }

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
        final int[] count = {garmentsId.size()};
        for (String id : garmentsId) {
            FirebaseDatabase.getInstance().getReference().child("garments").child(id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Garment garment = dataSnapshot.getValue(Garment.class);
                            filterGarment(garment, selectableGarments, garmentsId, count);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }
    }



    private void filterGarment(Garment garment, List<SelectableGarment> selectableGarments, List<String> garmentsId, int[] count){
        System.out.println("filter head: " + head + ", chest: " + chest + ", legs: " + legs + ", feet: " + feet);
        if(head){
            createSelectedGarmentsAndRecyclerWithfilter("head", garment, selectableGarments, garmentsId, count);
        }
        if(chest){
            createSelectedGarmentsAndRecyclerWithfilter("chest", garment, selectableGarments, garmentsId, count);
            if(legs){
                createSelectedGarmentsAndRecyclerWithfilter("body", garment, selectableGarments, garmentsId, count);
            }
        }
        if(legs){
            createSelectedGarmentsAndRecyclerWithfilter("legs", garment, selectableGarments, garmentsId, count);
        }
        if(feet){
            createSelectedGarmentsAndRecyclerWithfilter("feet", garment, selectableGarments, garmentsId, count);
        }
    }


    private void createSelectedGarmentsAndRecyclerWithfilter(String filter, Garment garment, List<SelectableGarment> selectableGarments, List<String> garmentsId, int[] count){
        if (getBody(garment.getType()).equals(filter)){
            createSelectableGarments(garment, selectableGarments, count);
            createRecyclerAtFinish(selectableGarments, garmentsId, count);
        }
    }


    private String getBody (String type){
        switch (type){
            case "Gorro": case "Hat":
                return "head";
            case "Sueter": case "Sweater":
            case "Camisa": case "Shirt":
            case "Camiseta": case "T-Shirt":
                return "chest";
            case "Pantalones": case "Pants":
            case "Vaqueros": case "Jeans":
            case "Falda": case "Skirt":
                return "legs";
            case "Abrigo": case "Coat":
            case "Vestido": case "Dress":
            case "Chandal": case "Tracksuit":
            case "Bañador": case "Swimsuit":
                return "body";
            case "Zapatos": case "Shoes":
            case "Deportivas": case "Sports":
                return "feet";
        }

        return null;
    }




    private void createSelectableGarments(Garment garment,  List<SelectableGarment> selectableGarments, int[]count){
        SelectableGarment selectableGarment = new SelectableGarment(garment, false);
        if (idsSelected != null) {
            if(idsSelected.contains(garment.getId())){
                selectableGarment = new SelectableGarment(garment, true);
            }
        }
        selectableGarments.add(selectableGarment);
        count[0]--;
    }


    private void createRecyclerAtFinish(final List<SelectableGarment> selectableGarments, List<String> garmentsId, int[] count){
        if (selectableGarments.size() + count[0] == garmentsId.size()) {
            adapter = new SelectableGarmentAdapter(selectableGarments);
            mRecycler.setAdapter(adapter);
            fabOutfit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   goToSelectOutfit(selectableGarments);
                }
            });
        }
    }


    private void goToSelectOutfit(List<SelectableGarment> selectableGarments){
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        Intent i = new Intent(OutfitActivity.this, FinishOutfitActivity.class);
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

        startActivity(i);
    }
}
