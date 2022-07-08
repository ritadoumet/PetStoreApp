package com.androidproject.petstoreapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Product {

    private String name;
    private int weight;
    private int productID;
    private static int ProductID = 0;
    private String description;
    private String color;
    private ProductType type;
    private ArrayList<PetSpecies> species;
    static ArrayList<Product> products = new ArrayList<Product>();

    private double price;

    public static void addProduct(Product p) {
        products.add(p);
        //TODO DATABASE

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(Product.class.getSimpleName());
        ref.child(p.getProductID()+"").setValue(p);
    }

    public ArrayList<PetSpecies> getSpecies() {
        return species;
    }

    public void setSpecies(ArrayList<PetSpecies> species) {
        this.species = species;
    }

    public Product(String name, int weight, String description, String color, ProductType type, ArrayList<PetSpecies> species, double price) {
        this.name = name;
        this.weight = weight;
        this.description = description;
        this.color = color;
        this.type = type;
        this.price=price;
        this.species=species;
        this.productID=ProductID++;
    }

    public Product(){}
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public static int getPRODUCTID(){return ProductID;}
    public static void setPRODUCTID(int productID){ProductID=productID;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
