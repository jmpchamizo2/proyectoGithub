package com.jmpcdev.wardrobe;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WashAdapter extends RecyclerView.Adapter {

    private List<Garment> garments;

    public WashAdapter(List<Garment> garments) {
        this.garments = garments;
    }

    @Override
    public WashHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.garment_card, parent, false);

        return new WashHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        WashHolder holder = (WashHolder) viewHolder;
        System.out.println(holder + "/////////////////");
        holder.txvName.setText(garments.get(position).getName());
        holder.txvDescript.setText(garments.get(position).getDescription());
        holder.txvType.setText(garments.get(position).getType());
        Picasso.get().load(Uri.parse(garments.get(position).getImage())).
                into(holder.imvGarmentCard);

    }

    @Override
    public int getItemCount() {
        return garments.size();
    }




}
