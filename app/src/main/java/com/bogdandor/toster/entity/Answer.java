package com.bogdandor.toster.entity;

public class Answer {
    private String text;
    private Author author;
    private Comment[] comments;

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

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public Comment[] getComments() {
        return comments;
    }

    public String toString() {
        return text;
    }
}
