package com.jmpcdev.wardrobe;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class WashHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

    TextView txvName,txvDescript, txvType;
    ImageView imvGarmentCard;
    CardView cdvGarment;


    public WashHolder(@NonNull View v) {
        super(v);
        txvName = (TextView) v.findViewById(R.id.txvNameGarment);
        txvDescript = (TextView) v.findViewById(R.id.txvDescGarment);
        txvType = (TextView) v.findViewById(R.id.txvTypeGarment);
        imvGarmentCard = (ImageView) v.findViewById(R.id.imvGarmentCard);
        v.setOnCreateContextMenuListener(this);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //menu.setHeaderTitle("Select The Action");
        MenuItem toWash = menu.add(0, v.getId(), 0, v.getResources().getString(R.string.toWash));//groupId, itemId, order, title
        toWash.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        System.out.println("YAaaaaaaaaaaaaaaaaaaaaa");
        return false;
    }
}
