package com.library;

public class Book {
    private String ISBN;
    private String title;
    private String description;
    private int category;
    private String edition;
    private int publisherId;

    public Book(String ISBN, String title, String description, int category, String edition, int publisherId) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.category = category;
        this.edition = edition;
        this.publisherId = publisherId;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }
}
