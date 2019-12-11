package com.jmpcdev.wardrobe;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WardrobeAdapter extends RecyclerView.Adapter {

    private List<Garment> garments;

    public WardrobeAdapter(List<Garment> garments) {
        this.garments = garments;
    }

    @Override
    public WardrobeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.garment_card, parent, false);

        return new WardrobeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        WardrobeHolder holder = (WardrobeHolder) viewHolder;
        holder.txvName.setText(garments.get(position).getName());
        holder.txvDescript.setText(garments.get(position).getDescription());
        holder.txvType.setText(garments.get(position).getType());
        Picasso.get().load(Uri.parse(garments.get(position).getImage())).
                into(holder.imvGarmentCard);
        holder.garment = garments.get(position);

    }

    @Override
    public int getItemCount() {
        return garments.size();
    }




}
