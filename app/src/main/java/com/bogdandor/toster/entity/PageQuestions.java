package com.bogdandor.toster.entity;

public class PageQuestions {
    private Question[] questions;
    private String prevPageUrl;
    private String nextPageUrl;

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }
}
