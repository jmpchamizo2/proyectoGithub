package com.jmpcdev.wardrobe;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class OutfitAdapter extends RecyclerView.Adapter<OutfitAdapter.ViewHolder> {

    private List<Garment> garments;
    private GarmentAdapter adapter;
    private Context context;

    public OutfitAdapter(List<Garment> garments, Context context) {
        this.garments = garments;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outfit_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txvName.setText("Prueba Outfit");
        holder.txvName.setText("Prueba Outfit");
        adapter = new GarmentAdapter(garments);
        holder.rcv.setHasFixedSize(true);
        holder.rcv.setLayoutManager(new LinearLayoutManager(context));
        holder.rcv.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txvName,txvDate;
        private RecyclerView rcv;

        public ViewHolder(View v) {
            super(v);
            txvName = (TextView) v.findViewById(R.id.txvDateStored);
            txvDate = (TextView) v.findViewById(R.id.txvNameOutfit);
            rcv = (RecyclerView) v.findViewById(R.id.rcv);
        }
    }



}
