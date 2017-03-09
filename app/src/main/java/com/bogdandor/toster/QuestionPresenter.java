package com.bogdandor.toster;

import android.os.AsyncTask;

import com.bogdandor.toster.data.Repository;
import com.bogdandor.toster.entity.Question;

public class QuestionPresenter {
    QuestionActivity view;

    QuestionPresenter(QuestionActivity view) {
        this.view = view;
    }

    public void onCreate() {
        String questionUrl = view.getQuestionUrl();
        new DownloaderQuestion().execute(questionUrl);
    }

    private class DownloaderQuestion extends AsyncTask<String, Void, Question> {
        Exception exception = null;

        protected Question doInBackground(String... params) {
            Question question = null;
            Repository repository = new Repository();
            try {
                String url = params[0];
                question = repository.getQuestion(url);
            } catch (Exception e) {
                exception = e;
            }
            return question;
        }

        protected void onPostExecute(Question question) {
            if (exception == null) {
                view.showQuestionTitle(question.getTitle());
                view.showQuestionText(question.getText());
            } else {
                view.showError();
            }
        }
    }

}
