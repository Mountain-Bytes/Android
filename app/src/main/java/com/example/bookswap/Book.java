package com.example.bookswap;

public class Book {
    private String[] authors = new String[0];
    private String[] categories = new String[0];
    private String content_version = "1.0.0";
    private String description = "";
    private ImageLinks image_links = null;
    private String isbn = "";
    private String lang = "en";
    private int page_count = 0;
    private String title = "";



    // Constructor
    public Book(String[] authors, String[] categories, String content_version, String description, ImageLinks image_links, String isbn, String lang, int page_count, String title) {
        this.authors = authors;
        this.categories = categories;
        this.content_version = content_version;
        this.description = description;
        this.image_links = image_links;
        this.isbn = isbn;
        this.lang = lang;
        this.page_count = page_count;
        this.title = title;
    }

    // Getters
    public String[] getAuthors() {
        return authors;
    }

    public String[] getCategories() {
        return categories;
    }

    public String getContent_version() {
        return content_version;
    }

    public String getDescription() {
        return description;
    }

    public ImageLinks getImage_links() {
        return image_links;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getLang() {
        return lang;
    }

    public int getPage_count() {
        return page_count;
    }

    public String getTitle() {
        return title;
    }

    // ImageLinks class (nested class)
    public static class ImageLinks {
        private String smallThumbnail;
        private String thumbnail;

        // Constructor
        public ImageLinks(String smallThumbnail, String thumbnail) {
            this.smallThumbnail = smallThumbnail;
            this.thumbnail = thumbnail;
        }

        // Getters
        public String getSmallThumbnail() {
            return smallThumbnail;
        }

        public String getThumbnail() {
            return thumbnail;
        }
    }
}

