package com.bogdandor.toster.mapper;

import com.bogdandor.toster.model.PageQuestions;
import com.bogdandor.toster.model.Question;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PageQuestionsMapper {

    public PageQuestions transform(Document document) throws IOException {
        PageQuestions pageQuestions = null;
        if (document != null) {
            pageQuestions = new PageQuestions();
            pageQuestions.setQuestions(transformQuestions(document));
            pageQuestions.setPrevPageUrl(transformPrevPageUrl(document));
            pageQuestions.setNextPageUrl(transformNextPageUrl(document));
        }
        return pageQuestions;
    }

    private Question[] transformQuestions(Document document) {
        Question[] questions = null;
        Elements elements = document.select(".question__title");
        if (!elements.isEmpty()) {
            questions = new Question[elements.size()];
            for (int i=0; i<questions.length; i++) {
                String title = elements.get(i).text();
                String url = elements.get(i).select("a[href]").first().attr("href");
                questions[i] = new Question();
                questions[i].setTitle(title);
                questions[i].setUrl(url);
            }
        }
        return questions;
    }

    private String transformPrevPageUrl(Document document) {
        String nextPageUrl = null;
        Elements elements = document.select(".paginator__item.prev");
        if (!elements.isEmpty()) {
            nextPageUrl = elements.first().select("a[href]").first().attr("href");
        }
        return nextPageUrl;
    }

    private String transformNextPageUrl(Document document) {
        String nextPageUrl = null;
        Elements elements = document.select(".paginator__item.next");
        if (!elements.isEmpty()) {
            nextPageUrl = elements.first().select("a[href]").first().attr("href");
        }
        return nextPageUrl;
    }
}
