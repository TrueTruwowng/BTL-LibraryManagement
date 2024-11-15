package com.library;

public class Book {
    private String ISBN;
    private String title;
    private String author;
    private String ImageSrc;
    private int year;
    private String description;
    private int available;
    private byte[] image;

    public byte[] getImage() {
        return image;
    }
    public Book() {

    }

    public String getImageSrc() {
        return ImageSrc;
    }

    public void setImageSrc(String imageSrc) {
        ImageSrc = imageSrc;
    }

    public Book(String ISBN, String title, String author, int year) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Getters and setters
    public String getIsbn() {
        return ISBN;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }


    public int getYear() {
        return year;
    }

    public String setYear(int year) {

        this.year = year;

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
