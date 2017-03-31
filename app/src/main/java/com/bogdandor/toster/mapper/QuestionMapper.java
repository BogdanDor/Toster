package com.bogdandor.toster.mapper;

import com.bogdandor.toster.entity.Answer;
import com.bogdandor.toster.entity.Comment;
import com.bogdandor.toster.entity.Question;
import com.bogdandor.toster.entity.Author;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class QuestionMapper {

    public Question transform(Document document) throws IOException {
        Question question = null;
        if (document != null) {
            question = new Question();
            question.setTitle(transformTitle(document));
            question.setText(transformText(document));
            question.setAuthor(transformAuthor(document.select(".question").first()));
            question.setComments(transformComments(document));
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

    private Author transformAuthor(Element element) {
        Author author = new Author();
        String name = element.select(".user-summary__name").first().text();
        author.setName(name);
        return author;
    }

    private Answer[] transformAnswers(Document document) throws IOException {
        Answer[] answers = null;
        Elements elements = document.select(".answer_wrapper");
        if (!elements.isEmpty()) {
            answers = new Answer[elements.size()];
            for (int i=0; i<answers.length; i++) {
                String text = elements.get(i).select(".answer__text").html();
                Author author = transformAuthor(elements.get(i));
                Comment[] comments = transformComments(elements.get(i));
                answers[i] = new Answer();
                answers[i].setText(text);
                answers[i].setAuthor(author);
                answers[i].setComments(comments);
            }
        }
        return answers;
    }

    private Comment[] transformComments(Element e) {
        Comment[] comments = null;
        Elements elements = e.select(".comment");
        if (!elements.isEmpty()) {
            comments = new Comment[elements.size()];
            for (int i=0; i<comments.length; i++) {
                String text = elements.get(i).select(".comment__text").html();
                Author author = transformAuthor(elements.get(i));
                comments[i] = new Comment();
                comments[i].setText(text);
                comments[i].setAuthor(author);
            }
        }
        return comments;
    }

    private Comment[] transformComments(Document document) {
        Element element = document.select(".question__comments").first();
        Element e = Jsoup.parse(element.html());
        e.select(".comment__text").select(".comment__date").remove();
        Comment[] comments = transformComments(e);
        return comments;
    }
}
