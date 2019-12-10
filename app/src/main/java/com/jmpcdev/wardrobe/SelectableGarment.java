package com.jmpcdev.wardrobe;

import androidx.annotation.Nullable;
import java.util.List;

public class SelectableGarment extends Garment {
    private boolean isSelected = false;
    private String id;
    private List<String> combine, users;

    public SelectableGarment(Garment garment, boolean isSelected) {
        super(garment.getImage(), garment.getName(), garment.getType(), garment.getDescription(), garment.getColor(), garment.getTissue(), garment.getTemperature(), garment.getBrandName());
        this.id = garment.getId();
        this.isSelected = isSelected;
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
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean equals(@Nullable SelectableGarment obj) {
        return this.getId() == obj.getId();
    }





}
