package com.jmpcdev.wardrobe;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectableOutfitGarmentAdapter extends RecyclerView.Adapter {
    private final List<SelectableOutfitGarment> selectableOutfitGarments;





    public SelectableOutfitGarmentAdapter(List<SelectableOutfitGarment> selectableOutfitGarments) {
        this.selectableOutfitGarments = selectableOutfitGarments;
    }


    @Override
    public SelectableOutfitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectable_garment_card, parent, false);

        return new SelectableOutfitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableOutfitViewHolder holder = (SelectableOutfitViewHolder) viewHolder;
        SelectableOutfitGarment g = selectableOutfitGarments.get(position);
        String name = g.getName();
        holder.txvSelectableOutfitGarmentName.setText(name);
        holder.txvSelectableOutfitGarmentDescription.setText(g.getDescription());
        holder.txvSelectableOutfitGarmentType.setText(g.getType());
        holder.selectableOutfitGarment = g;
        holder.selectableOutfitGarments = selectableOutfitGarments;
        holder.cardView.setBackgroundColor(Color.WHITE);
        holder.checkCardView();
        Picasso.get().load(Uri.parse(g.getImage())).
                into(holder.imvSelectableOutfitGarmentImage);
    }

    @Override
    public int getItemCount() {
        return selectableOutfitGarments.size();
    }



}

