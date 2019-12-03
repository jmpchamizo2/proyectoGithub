package com.jmpcdev.wardrobe;

import android.widget.ImageView;

public class ImvSelected {

    private boolean selected = false;


    public boolean isSelected() {
        return selected;
    }

    public void imvClick(ImageView image, int imageSelectedResourceId, int imageDefaultResourceId){
        selected = !isSelected();
        if(selected){
            image.setImageResource(imageSelectedResourceId);
        } else {
            image.setImageResource(imageDefaultResourceId);
        }
    }
}
