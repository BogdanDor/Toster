package com.bogdandor.toster.presenter;

import android.os.AsyncTask;

import com.bogdandor.toster.view.Presenter;
import com.bogdandor.toster.data.Repository;
import com.bogdandor.toster.entity.PageQuestions;
import com.bogdandor.toster.view.MainActivity;

public class MainPresenter implements Presenter<MainActivity> {
    private MainActivity view;
    private PageQuestions pageQuestions;

    public MainPresenter(MainActivity view) {
        this.view = view;
        new DownloaderPageQuestions().execute();
    }

    public void onClickPrev() {
        String prevPage = pageQuestions.getPrevPageUrl();
        new DownloaderPageQuestions().execute(prevPage);
    }

    public void onClickNext() {
        String nextPage = pageQuestions.getNextPageUrl();
        new DownloaderPageQuestions().execute(nextPage);
    }

    public void onItemClickList(int i) {
        String urlQuestion = pageQuestions.getQuestions()[i].getUrl();
        view.showQuestion(urlQuestion);
    }

    public void onLatestClick() {
        String pageQuestions = "https://toster.ru/questions/latest";
        new DownloaderPageQuestions().execute(pageQuestions);
    }

    public void onInterestingClick() {
        String pageQuestions = "https://toster.ru/questions/interesting";
        new DownloaderPageQuestions().execute(pageQuestions);
    }

    public void onWithoutAnswerClick() {
        String pageQuestions = "https://toster.ru/questions/without_answer";
        new DownloaderPageQuestions().execute(pageQuestions);
    }

    @Override
    public void onViewAttached(MainActivity view) {
        this.view = view;
        if (pageQuestions != null) {
            view.showArray(pageQuestions.getQuestions());
            view.showPrevButton(pageQuestions.getPrevPageUrl());
            view.showNextButton(pageQuestions.getNextPageUrl());
        }
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onDestroyed() {
        pageQuestions = null;
    }

    private class DownloaderPageQuestions extends AsyncTask<String, Void, Exception> {

        protected Exception doInBackground(String... params) {
            Exception exception = null;
            Repository repository = new Repository();
            try {
                if (params.length == 0) {
                    MainPresenter.this.pageQuestions = repository.getPageQuestions();
                } else {
                    String url = params[0];
                    MainPresenter.this.pageQuestions = repository.getPageQuestions(url);
                }
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
                view.showArray(pageQuestions.getQuestions());
                view.showPrevButton(pageQuestions.getPrevPageUrl());
                view.showNextButton(pageQuestions.getNextPageUrl());
            } else {
                view.showError();
            }
        }
    }

}
