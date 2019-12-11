package com.jmpcdev.wardrobe;


import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectableOutfitViewHolder extends RecyclerView.ViewHolder {


    TextView txvSelectableOutfitGarmentName, txvSelectableOutfitGarmentDescription, txvSelectableOutfitGarmentType;
    ImageView imvSelectableOutfitGarmentImage;
    CardView cardView;
    SelectableOutfitGarment selectableOutfitGarment;
    List<SelectableOutfitGarment> selectableOutfitGarments;




    public SelectableOutfitViewHolder(View view) {
        super(view);
        txvSelectableOutfitGarmentDescription = view.findViewById(R.id.txvSelectableDescGarment);
        txvSelectableOutfitGarmentName = view.findViewById(R.id.txvSelectableNameGarment);
        txvSelectableOutfitGarmentType = view.findViewById(R.id.txvSelectableTypeGarment);
        imvSelectableOutfitGarmentImage = view.findViewById(R.id.imvSelectableGarmentCard);
        cardView = (CardView) view.findViewById(R.id.cdvSelectableGarment);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchView(v);
                if(selectableOutfitGarment.isSelected()){
                    v.setBackgroundColor(Color.GRAY);
                } else {
                    v.setBackgroundColor(Color.WHITE);
                }
            }
        });

    }




    private void switchView(View view){
        view.setActivated(!view.isActivated());
        selectableOutfitGarment.setSelected(!selectableOutfitGarment.isSelected());
        selectableOutfitGarments.set(selectableOutfitGarments.indexOf(selectableOutfitGarment), selectableOutfitGarment);
    }


    public void checkCardView(){
        if(selectableOutfitGarment.isSelected()){
            cardView.setBackgroundColor(Color.GRAY);
        }
    }


}
