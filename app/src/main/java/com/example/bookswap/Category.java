package com.example.bookswap;

public class Category {

    private String name;
    private String imageurl;

    public Category(String name, String imageurl){
        this.name = name;
        this.imageurl = imageurl;
    }


    public  String getName(){
        return name;
    }
    public  String getImageurl(){return imageurl;}
}
