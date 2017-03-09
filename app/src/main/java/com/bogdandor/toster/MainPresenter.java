package com.bogdandor.toster;

import android.os.AsyncTask;

import com.bogdandor.toster.data.Repository;
import com.bogdandor.toster.entity.PageQuestions;

public class MainPresenter {
    private MainActivity view;
    private PageQuestions pageQuestions;

    MainPresenter(MainActivity view) {
        this.view = view;
    }

    public void onCreate() {
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

    private class DownloaderPageQuestions extends AsyncTask<String, Void, PageQuestions> {
        Exception exception = null;

        protected PageQuestions doInBackground(String... params) {
            Repository repository = new Repository();
            PageQuestions pageQuestions = null;
            try {
                if (params.length == 0) {
                    pageQuestions = repository.getPageQuestions();
                } else {
                    String url = params[0];
                    pageQuestions = repository.getPageQuestions(url);
                }
            } catch (Exception e) {
                exception = e;
            }
            return pageQuestions;
        }

        protected void onPostExecute(PageQuestions pageQuestions) {
            try {
                if (exception == null) {
                    MainPresenter.this.pageQuestions = pageQuestions;
                    view.showArray(pageQuestions.getQuestions());
                    view.showPrevButton(pageQuestions.getPrevPageUrl());
                    view.showNextButton(pageQuestions.getNextPageUrl());
                } else {
                    view.showError();
                }
            } catch (Exception e) {
                view.showError();
            }
        }
    }

}
