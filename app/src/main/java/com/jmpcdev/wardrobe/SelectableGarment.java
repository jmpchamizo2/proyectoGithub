package com.jmpcdev.wardrobe;

public class SelectableGarment extends Garment {
    private boolean isSelected = false;


    public SelectableGarment(Garment garment,boolean isSelected) {
        super(garment.getImage(), garment.getName(),garment.getType(), garment.getDescription(), garment.getColor(), garment.getTissue(), garment.getTemperature(), garment.getBrandName());
        this.isSelected = isSelected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
