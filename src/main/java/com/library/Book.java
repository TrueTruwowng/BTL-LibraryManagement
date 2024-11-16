package com.library;

import javafx.scene.control.CheckBox;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private int year;
    private String description;
    private int available;
    private byte[] bookImage;

    public Book() {}

    public Book(String isbn, String title, String author, int year, int available, String description, byte[] bookImage) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.description = description;
        this.available = available;
        this.bookImage = bookImage;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getters and setters
    public String getIsbn() {return isbn;}

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public byte[] getBookImage() {
        return bookImage;
    }

    public void setBookImage(byte[] bookImage) {
        this.bookImage = bookImage; // Gán mảng byte cho bookImage
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}