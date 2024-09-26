package com.example.bookswap;

public class Author {
    private String name;
    private String imageUrl; // URL for the author's image, if any

    public Author(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
