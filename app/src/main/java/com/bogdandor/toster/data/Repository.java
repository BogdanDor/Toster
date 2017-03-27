package com.bogdandor.toster.data;

import com.bogdandor.toster.mapper.PageQuestionsMapper;
import com.bogdandor.toster.mapper.QuestionMapper;
import com.bogdandor.toster.entity.PageQuestions;
import com.bogdandor.toster.entity.Question;

import java.io.IOException;

public class Repository {

    public PageQuestions getPageQuestions(String url) throws IOException {
        DataStore dataStore = new DataStore();
        PageQuestionsMapper mapper = new PageQuestionsMapper();
        PageQuestions pageQuestions = mapper.transform(dataStore.getPage(url));
        return pageQuestions;
    }

    public PageQuestions getPageQuestions() throws IOException {
        String siteUrl = "https://toster.ru";
        return getPageQuestions(siteUrl);
    }

    public Question getQuestion(String url) throws IOException {
        DataStore dataStore = new DataStore();
        QuestionMapper mapper = new QuestionMapper();
        Question question = mapper.transform(dataStore.getPage(url));
        return question;
    }
}
