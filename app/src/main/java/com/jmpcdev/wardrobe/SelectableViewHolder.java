package com.jmpcdev.wardrobe;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckedTextView;

import androidx.recyclerview.widget.RecyclerView;

public class SelectableViewHolder extends RecyclerView.ViewHolder {

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    CheckedTextView textView;
    SelectableGarment selectableGarment;
    OnItemSelectedListener itemSelectedListener;


    public SelectableViewHolder(View view, OnItemSelectedListener listener) {
        super(view);
        itemSelectedListener = listener;
        textView = (CheckedTextView) view.findViewById(R.id.checkedTextView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selectableGarment.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(selectableGarment);

            }
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            textView.setBackgroundColor(Color.LTGRAY);
        } else {
            textView.setBackground(null);
        }
        selectableGarment.setSelected(value);
        textView.setChecked(value);
    }

    public interface OnItemSelectedListener {

        void onItemSelected(SelectableGarment selectableGarment);
    }

}
