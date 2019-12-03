package com.jmpcdev.wardrobe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    //private String id;
    private String email;
    private String name;
    private String birthDate;
    private String gender;
    private String country;
    private String state;
    private String city;
    private String zipCode;
    private List<String> garments;

    public User(){
        this.email = null;
        this.name = null;
        this.birthDate = null;
        this.gender = null;
        this.country = null;
        this.state = null;
        this.city = null;
        this.zipCode = null;
        garments = new ArrayList<String>();
    }

    public User(String email, String name, String birthDate, String gender, String country, String state, String city, String zipCode) {
        //this.id = id;
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
        garments = new ArrayList<String>();
    }

    //public String getId() {
    //    return id;
    //}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String pais) {
        this.country = pais;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city =  city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public List<String> getGarments() {
        return garments;
    }

    public boolean addGarment(String garment){
        return garments.add(garment);
    }

    public boolean removeGarment(String garment){
        return garments.remove(garment);
    }

    public String toString(){
        return this.email + ", " + this.zipCode + ", " + this.gender + "=================================";
    }
}
