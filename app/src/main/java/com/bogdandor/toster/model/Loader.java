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

    public Question[] getQuestions() throws IOException {
        Document doc = Jsoup.connect(siteUrl).get();
        Elements elements = doc.select("h2.question__title");
        Question[] questions = new Question[elements.size()];
        for (int i=0; i<questions.length; i++) {
            questions[i] = new Question();
        }
        for (int i=0; i<elements.size(); i++) {
            questions[i].title = elements.get(i).text();
        }
        return questions;
    }

    private Loader() {  }

}
