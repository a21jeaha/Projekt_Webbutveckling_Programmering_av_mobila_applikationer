package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Auxdata {
    @SerializedName("ID")
    private String _id2;
    private String info;
    private String img;

    public Auxdata(String _id2, String info, String img) {
        this._id2 = _id2;
        this.info = info;
        this.img = img;
    }

    public String get_id2(){return _id2;}

    public void set_id2(String _id2){this._id2 = _id2; }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
