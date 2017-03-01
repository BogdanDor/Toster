package com.bogdandor.toster.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Question {
    public String title;
    public String url;
    public String text;

    Question(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public void load() throws IOException {
        Document doc = Jsoup.connect(url).get();
        text = doc.select(".question__text").html();
    }

    public String toString() {
        return title;
    }
}
