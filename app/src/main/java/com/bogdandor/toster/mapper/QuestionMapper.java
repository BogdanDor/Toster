package com.bogdandor.toster.mapper;

import com.bogdandor.toster.entity.Question;

import org.jsoup.nodes.Document;

import java.io.IOException;

public class QuestionMapper {

    public Question transform(Document document) throws IOException {
        Question question = null;
        if (document != null) {
            question = new Question();
            String title = document.select(".question__title").first().text();
            String text = document.select(".question__text").html();
            question.setTitle(title);
            question.setText(text);
        }
        return question;
    }
}
