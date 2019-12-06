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
                switchView(v);
                if(selectableGarment.isSelected()){
                    v.setBackgroundColor(Color.GRAY);
                } else {
                    v.setBackgroundColor(Color.WHITE);
                }
            }
        });

    }



    private void switchView(View view){
        view.setActivated(!view.isActivated());
        selectableGarment.setSelected(!selectableGarment.isSelected());
        selectableGarments.set(selectableGarments.indexOf(selectableGarment), selectableGarment);
        for (SelectableGarment s : selectableGarments){
            System.out.println(s.isSelected() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    public void checkCardView(boolean setBackground){
        if(setBackground){
            cardView.setBackgroundColor(Color.GRAY);
        }
    }


}
