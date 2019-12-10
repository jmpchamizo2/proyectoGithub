package com.jmpcdev.wardrobe;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GarmentAdapter extends RecyclerView.Adapter<GarmentAdapter.ViewHolder> {

    private List<Garment> garments;

    public GarmentAdapter(List<Garment> garments) {
        this.garments = garments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.garment_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txvName,txvDescript, txvType;
        private ImageView imvGarmentCard;
        public ViewHolder(View v) {
            super(v);
            txvName = (TextView) v.findViewById(R.id.txvNameGarment);
            txvDescript = (TextView) v.findViewById(R.id.txvDescGarment);
            txvType = (TextView) v.findViewById(R.id.txvTypeGarment);
            imvGarmentCard = (ImageView) v.findViewById(R.id.imvGarmentCard);
        }
    }
}
