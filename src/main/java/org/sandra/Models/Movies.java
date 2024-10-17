package org.sandra.Models;

public class Movies {

    public Movies() {
    }

    public Movies(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public boolean getTitle;
    private long id;

    private String title;

    private int year;

    public boolean isGetTitle() {
        return getTitle;
    }

    public void setGetTitle(boolean getTitle) {
        this.getTitle = getTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
