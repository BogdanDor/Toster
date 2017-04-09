package com.bogdandor.toster.entity;

public class Question {
    private String title;
    private String url;
    private String text;
    private Author author;
    private Answer[] answers;
    private Comment[] comments;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

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

    public void setAswers(Answer[] answers) {
        this.answers = answers;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public Comment[] getComments() {
        return comments;
    }

    public String toString() {
        return title;
    }
}
