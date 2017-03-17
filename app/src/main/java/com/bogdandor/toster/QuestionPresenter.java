package com.bogdandor.toster;

import android.os.AsyncTask;

import com.bogdandor.toster.data.Repository;
import com.bogdandor.toster.entity.Question;

public class QuestionPresenter implements Presenter<QuestionActivity> {
    QuestionActivity view;
    Question question;

    QuestionPresenter(QuestionActivity view) {
        this.view = view;
        String questionUrl = view.getQuestionUrl();
        new DownloaderQuestion().execute(questionUrl);
    }

    @Override
    public void onViewAttached(QuestionActivity view) {
        this.view = view;
        if (question != null) {
            view.showQuestionText(question.getTitle());
            view.showQuestionText(question.getText());
        }
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onDestroyed() {
        question = null;
    }

    private class DownloaderQuestion extends AsyncTask<String, Void, Exception> {

        protected Exception doInBackground(String... params) {
            Exception exception = null;
            Repository repository = new Repository();
            try {
                String url = params[0];
                QuestionPresenter.this.question = repository.getQuestion(url);
            } catch (Exception e) {
                exception = e;
            }
            return exception;
        }

        protected void onPostExecute(Exception exception) {
            if (view == null) {
                return;
            }
            if (exception == null) {
                view.showQuestionTitle(question.getTitle());
                view.showQuestionText(question.getText());
            } else {
                view.showError();
            }
        }
    }
}
