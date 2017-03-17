package com.bogdandor.toster.mapper;

import com.bogdandor.toster.entity.Answer;
import com.bogdandor.toster.entity.Question;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class QuestionMapper {

    public Question transform(Document document) throws IOException {
        Question question = null;
        if (document != null) {
            question = new Question();
            question.setTitle(transformTitle(document));
            question.setText(transformText(document));
            question.setAswers(transformAnswers(document));
        }
        return question;
    }

    private String transformTitle(Document document) {
        return document.select(".question__title").first().text();
    }

    private String transformText(Document document) {
        return document.select(".question__text").html();
    }

    private Answer[] transformAnswers(Document document) throws IOException {
        Answer[] answers = null;
        Elements elements = document.select(".answer__text");
        if (!elements.isEmpty()) {
            answers = new Answer[elements.size()];
            for (int i=0; i<answers.length; i++) {
                String text = elements.get(i).text();
                answers[i] = new Answer();
                answers[i].setText(text);
            }
        }
        return answers;
    }
}
