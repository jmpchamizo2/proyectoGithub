package com.jmpcdev.wardrobe;

import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WardrobeHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

    TextView txvName,txvDescript, txvType;
    ImageView imvGarmentCard;
    Garment garment;
    View v;

    public WardrobeHolder(@NonNull View v) {
        super(v);
        txvName = (TextView) v.findViewById(R.id.txvNameGarment);
        txvDescript = (TextView) v.findViewById(R.id.txvDescGarment);
        txvType = (TextView) v.findViewById(R.id.txvTypeGarment);
        imvGarmentCard = (ImageView) v.findViewById(R.id.imvGarmentCard);
        v.setOnCreateContextMenuListener(this);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(v.getResources().getString(R.string.select_action));
        MenuItem toWash = menu.add(0, 0, 0, v.getResources().getString(R.string.toWash));//groupId, itemId, order, title
        MenuItem delete = menu.add(0, 1, 0, v.getResources().getString(R.string.delete));
        toWash.setOnMenuItemClickListener(this);
        delete.setOnMenuItemClickListener(this);
        this.v = v;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mDataBaseGarmentsGarmentId = mDataBase.child("garments").child(garment.getId());
        switch (item.getItemId()){
            case 0:
                new AlertDialog.Builder(v.getRootView().getContext())
                        .setMessage(v.getResources().getString(R.string.wash_message))
                        .setPositiveButton(v.getResources().getString(R.string.accept_wash), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                garment.setWashing(true);
                                mDataBaseGarmentsGarmentId.child("washing").setValue(garment.isWashing());

                            }
                        })
                        .setNegativeButton(v.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();



                return true;
            case 1:
                new AlertDialog.Builder(v.getRootView().getContext())
                        .setMessage(v.getResources().getString(R.string.delete_garment))
                        .setPositiveButton(v.getResources().getString(R.string.accept_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDataBaseGarmentsGarmentId.removeValue();
                                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                mDataBase.child("users").child(currentUser.getUid()).child("garments").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        List<String> garmentsId = (List<String>) dataSnapshot.getValue();
                                        garmentsId.remove(garment.getId());
                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                .child(currentUser.getUid()).child("garments").setValue(garmentsId);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(v.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
        }
        return false;
    }




}
