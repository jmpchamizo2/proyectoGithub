package com.jmpcdev.wardrobe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Garment {

    private String id;
    private String image;
    private String name;
    private String type;
    private String description;
    private String color;
    private String tissue;
    private String temperature;
    private String brandName;
    private List<String> garments;
    private List<String> users;


    public Garment(String image, String name, String type, String description, String color, String tissue, String temperature, String brandName) {
        this.id = UUID.randomUUID().toString();;
        this.image = image;
        this.name = name;
        this.type = type;
        this.description = description;
        this.color = color;
        this.tissue = tissue;
        this.temperature = temperature;
        this.brandName = brandName;
        garments = new ArrayList<String>();
        users = new ArrayList<String>();
    }

    public String getId(){
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean addGarment(String garmentId){
        return garments.add(garmentId);
    }

    public boolean removeGarment(String garmentId){
        return garments.remove(garmentId);
    }

    public List<String> getGarments(){
        return  garments;
    }


    public boolean addUser(String userId){
        return users.add(userId);
    }

    public boolean removeUser(String userId){
        return users.remove(userId);
    }

    public List<String> getUsers(){
        return users;
    }

}
