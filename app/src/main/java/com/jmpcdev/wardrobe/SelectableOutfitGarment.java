package com.jmpcdev.wardrobe;

import android.view.View;

import androidx.annotation.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class SelectableOutfitGarment extends Garment {
    private boolean selected = false;
    private String id;
    private List<String> combine, users;
    private ChangeListener listener;




    public SelectableOutfitGarment(Garment garment, boolean selected) {
        super(garment.getImage(), garment.getName(), garment.getType(), garment.getDescription(), garment.getColor(), garment.getTissue(), garment.getTemperature(), garment.getBrandName());
        this.id = garment.getId();
        this.selected = selected;
        this.combine = garment.getCombine();
        this.users = garment.getUsers();
        this.setWashing(isWashing());

    }

    @Override
    public String getId() {
        return id;
    }



    @Override
    public List<String> getCombine() {
        return combine;
    }


    @Override
    public List<String> getUsers() {
        return users;
    }



    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if(listener != null){
            listener.onChange(this.selected);
        }
    }

    public ChangeListener getListener(){
        return listener;
    }

    public void setListener(ChangeListener listener){
        this.listener = listener;
    }

    public boolean equals(@Nullable SelectableOutfitGarment obj) {
        return this.getId() == obj.getId();
    }


    public interface ChangeListener {
        void onChange(boolean selected);
    }


}
