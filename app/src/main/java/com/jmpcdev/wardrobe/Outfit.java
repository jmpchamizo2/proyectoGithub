package com.jmpcdev.wardrobe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Outfit {

    private String id;
    private String name;
    private String date;
    private List<String> idsGarment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Outfit(String name, String date, List<String> idsGarment) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;
        this.idsGarment = idsGarment;
    }

    public String getName() {
        return name;
    }

    public List<String> getIdsGarment() {
        return idsGarment;
    }

    public void setIdsGarment(List<String> idsGarment) {
        this.idsGarment = idsGarment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addIdsGarment(String id){
        idsGarment.add(id);
    }
}
