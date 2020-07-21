package com.example.camprecapp.models;

public class Company {
    private String address;
    private String city;
    private String name;

    public Company(String address, String city, String name) {
        this.address = address;
        this.city = city;
        this.name = name;
    }

    public Company() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
