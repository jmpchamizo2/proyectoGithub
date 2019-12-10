package com.jmpcdev.wardrobe;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SelectableGarmentAdapter extends RecyclerView.Adapter {
    private final List<SelectableGarment> selectableGarments;





    public SelectableGarmentAdapter(List<SelectableGarment> selectableGarments) {
        this.selectableGarments = selectableGarments;
    }


    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectable_garment_card, parent, false);

        return new SelectableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableViewHolder holder = (SelectableViewHolder) viewHolder;
        SelectableGarment g = selectableGarments.get(position);
        String name = g.getName();
        holder.txvSelectableGarmentName.setText(name);
        holder.txvSelectableGarmentDescription.setText(g.getDescription());
        holder.txvSelectableGarmentType.setText(g.getType());
        holder.selectableGarment = g;
        holder.checkCardView(g.isSelected());
        holder.selectableGarments = selectableGarments;
        Picasso.get().load(Uri.parse(g.getImage())).
                into(holder.imvSelectableGarmentImage);
    }

    @Override
    public int getItemCount() {
        return selectableGarments.size();
    }



}

