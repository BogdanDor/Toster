package com.bogdandor.toster.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Loader {
    private static Loader ourInstance = new Loader();
    private String siteUrl = "https://www.toster.ru";

    public static Loader getInstance() {
        return ourInstance;
    }

    public String getAllQuestions() throws IOException {
        Document doc = Jsoup.connect(siteUrl).get();
        Elements questions = doc.select("h2.question__title");
        String result = questions.html();
        return result;
    }

    private Loader() {  }

}
