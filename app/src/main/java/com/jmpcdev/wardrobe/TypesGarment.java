package com.jmpcdev.wardrobe;

import android.content.res.Resources;

public enum  TypesGarment {
    SWEATER(BodyGarment.CHEST, "Sweater"),
    SHIRT(BodyGarment.CHEST, Resources.getSystem().getString(R.string.shirt)),
    TSHIRT(BodyGarment.CHEST, Resources.getSystem().getString(R.string.tshirt)),
    PANTS(BodyGarment.LEGS, Resources.getSystem().getString(R.string.pants)),
    JEANS(BodyGarment.LEGS, Resources.getSystem().getString(R.string.jeans)),
    SKIRT(BodyGarment.LEGS, Resources.getSystem().getString(R.string.skirt)),
    DRESS(BodyGarment.BODY, Resources.getSystem().getString(R.string.dress)),
    COAT(BodyGarment.BODY, Resources.getSystem().getString(R.string.coat)),
    SHOES(BodyGarment.FEET, Resources.getSystem().getString(R.string.shoes)),
    SPORTS(BodyGarment.FEET, Resources.getSystem().getString(R.string.sports)),
    TRACKSUIT(BodyGarment.BODY, Resources.getSystem().getString(R.string.tracksuit)),
    SWIMSUIT(BodyGarment.BODY, Resources.getSystem().getString(R.string.swimsuit)),
    HAT(BodyGarment.HEAD, Resources.getSystem().getString(R.string.hat));

    private BodyGarment part;
    private String name;

    private TypesGarment(BodyGarment part, String name){
        this.part = part;
        this.name = name;
    }

    public BodyGarment getPart() {
        return part;
    }

    public String getName(){
        return name;
    }

}



enum BodyGarment {
    HEAD, CHEST ,BODY, LEGS, FEET
}