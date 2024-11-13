package com.library;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private int year;
    private String description;
    private int available;
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Book(String isbn, String title, String author, int year, String description, int available, byte[] image) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.description = description;
        this.available = available;
        this.image = image;
    }

    // Getters and setters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public int getAvailable() {
        return available;
    }

    // Thêm setter cho available
    public void setAvailable(int available) {
        this.available = available;
    }
}
