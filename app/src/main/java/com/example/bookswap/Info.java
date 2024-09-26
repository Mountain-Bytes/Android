package com.example.bookswap;

public class Info {
    private String books;
    private String name;  //URl of author's images
    private String user_id;  // URL of diplay image


    public Info(String books,String name,String user_id){

        this.books = books;
        this.name = name;
        this.user_id = user_id;

    }

    public String getName() {
        return books;
    }

    public String getname() {
        return name;
    }

    public String getUser_id(){
        return user_id;
    }





}
