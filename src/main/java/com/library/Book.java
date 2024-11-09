package com.library;

public class Book {
    private String ISBN;
    private String title;
    private String description;
    private int category;
    private String year;
    private int publisherId;

    public Book(String ISBN, String title, String description, int category, String edition, int publisherId) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.category = category;
        this.year = edition;
        this.publisherId = publisherId;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    public String getYear() {
        return year;
    }

    public int getPublisherId() {
        return publisherId;
    }
}
