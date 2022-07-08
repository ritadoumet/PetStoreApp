package com.androidproject.petstoreapp;

public class Pet {
    private static int Pet_ID =0;
    private String name;
    private int petID;
    private int year, dayOfMonth, month;
    private PetSpecies species;
    private int ownerID;
    private String furColor;

    public Pet(String name, int dayOfMonth, int month, int year, PetSpecies species, int ownerID, String furColor) {
        this.name = name;
        this.dayOfMonth=dayOfMonth;
        this.year=year;
        this.month=month;
        this.species = species;
        this.ownerID = ownerID;
        this.furColor = furColor;
        petID= Pet_ID++;
    }

    public Pet(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetSpecies getSpecies() {
        return species;
    }

    public void setSpecies(PetSpecies species) {
        this.species = species;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getFurColor() {
        return furColor;
    }

    public int getPetID() {
        return petID;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setFurColor(String furColor) {
        this.furColor = furColor;
    }

    public static void setPET_ID(int petID){ Pet_ID = petID;}
    public static int getPET_ID(){return Pet_ID;}

}
