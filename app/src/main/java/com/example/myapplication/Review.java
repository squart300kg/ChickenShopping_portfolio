package com.example.myapplication;

public class Review {
    String title;
    String contents;
    int img;
    String writter;
    String name;

    public void setImg(int img) {
        this.img = img;
    }

    public void setWritter(String writter) {
        this.writter = writter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public String getWritter() {
        return writter;
    }

    public String getName() {
        return name;
    }

    public Review(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
    public Review(){}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }
}
