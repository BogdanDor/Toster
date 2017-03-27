package com.bogdandor.toster.entity;

public class Answer {
    private String text;
    private Comment[] comments;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
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
