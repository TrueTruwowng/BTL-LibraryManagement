package com.library;

public class Book {
    private String ISBN;
    private String title;
    private String author;
    private String ImageSrc;
    private String year;

    public Book() {

    }

    public String getImageSrc() {
        return ImageSrc;
    }

    public void setImageSrc(String imageSrc) {
        ImageSrc = imageSrc;
    }

    public Book(String ISBN, String title, String author, String year) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.year = year;
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

    public String getYear() {

        return year;
    }

    public void setYear(String year) {

        this.year = year;
    }
}
