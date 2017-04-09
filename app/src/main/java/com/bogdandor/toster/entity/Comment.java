package com.bogdandor.toster.entity;

public class Comment {
    private String text;
    private Author author;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    public String toString() {
        return text;
    }
}
