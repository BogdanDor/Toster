package com.bogdandor.toster.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PageQuestions {
    public static PageQuestions currentPage;
    private Document doc;

    public PageQuestions(String siteUrl) throws IOException {
        doc = Jsoup.connect(siteUrl).get();
        currentPage = this;
    }

    public PageQuestions() throws IOException {
        this("https://www.toster.ru");
    }

    public Question[] getQuestions() throws IOException {
        Elements elements = doc.select("h2.question__title");
        Question[] questions = new Question[elements.size()];
        for (int i=0; i<questions.length; i++) {
            String title = elements.get(i).text();
            String url = elements.get(i).select("a[href]").first().attr("href");
            questions[i] = new Question(title, url);
        }
        return questions;
    }

    public String getNextPage() throws IOException {
        Elements elements = doc.select(".paginator__item.next");
        if (!elements.isEmpty()) {
            Element element = elements.first();
            String link = element.select("a[href]").first().attr("href");
            return link;
        }
        return null;
    }

    public String getPrevPage() throws IOException {
        Elements elements = doc.select(".paginator__item.prev");
        if (!elements.isEmpty()) {
            Element element = elements.first();
            String link = element.select("a[href]").first().attr("href");
            return link;
        }
        return null;
    }

}
