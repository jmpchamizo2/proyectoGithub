package com.jmpcdev.wardrobe;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.CharArrayReader;
import java.util.List;

public class SelectableViewHolder extends RecyclerView.ViewHolder {


    TextView txvSelectableGarmentName, txvSelectableGarmentDescription, txvSelectableGarmentType;
    ImageView imvSelectableGarmentImage;
    CardView cardView;
    SelectableGarment selectableGarment;
    List<SelectableGarment> selectableGarments;



    public SelectableViewHolder(View view) {
        super(view);
        txvSelectableGarmentDescription = view.findViewById(R.id.txvSelectableDescGarment);
        txvSelectableGarmentName = view.findViewById(R.id.txvSelectableNameGarment);
        txvSelectableGarmentType = view.findViewById(R.id.txvSelectableTypeGarment);
        cardView = (CardView) view.findViewById(R.id.cdvSelectableGarment);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Entramos//////////////////////////////////");
                switchView(v);
                if(v.isActivated()){
                    v.setBackgroundColor(Color.GRAY);
                } else {
                    v.setBackgroundColor(Color.WHITE);
                }
            }
        });


    }



    public void switchView(View view){
        view.setActivated(!view.isActivated());
        selectableGarment.setSelected(!selectableGarment.isSelected());
        selectableGarments.set(selectableGarments.indexOf(selectableGarment), selectableGarment);

    }


}
