package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Penguin {
    @SerializedName("ID")
    private String _id;
    private String name;
    @SerializedName("location")
    private String eats;
    private String size;
    private Auxdata auxdata;

    public Penguin(String _id, String name, String eats, String size, Auxdata auxdata) {
        this._id = _id;
        this.name = name;
        this.eats = eats;
        this.size = size;
        this.auxdata = auxdata;

    }

    public String get_id(){return _id;}

    public void set_id(String _id){this._id = _id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEats() {
        return eats;
    }

    public void setEats(String eats) {
        this.eats = eats;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Auxdata getAuxdata() {
        return auxdata;
    }

    public void setAuxdata(Auxdata auxdata) {
        this.auxdata = auxdata;
    }
}

