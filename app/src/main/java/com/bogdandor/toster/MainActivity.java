package com.bogdandor.toster;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.bogdandor.toster.model.PageQuestions;
import com.bogdandor.toster.model.Question;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ListView listQuestions;
    Button prev;
    String prevPage;
    Button next;
    String nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listQuestions = (ListView) findViewById(R.id.list_questions);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);

        new DownloaderPage().execute();
    }

    public void onClickPrev(View view) {
        new DownloaderPage().execute(prevPage);
    }

    public void onClickNext(View view) {
        new DownloaderPage().execute(nextPage);
    }

    void viewPageQuestions(PageQuestions pageQuestions) throws IOException {
        Question[] questions = pageQuestions.getQuestions();
        ArrayAdapter<Question> listAdapter = new ArrayAdapter<Question>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                questions);
        listQuestions.setAdapter(listAdapter);

        prevPage = pageQuestions.getPrevPage();
        prev.setVisibility(View.VISIBLE);
        if (prevPage == null) {
            prev.setVisibility(View.INVISIBLE);
        }
        nextPage = pageQuestions.getNextPage();
        next.setVisibility(View.VISIBLE);
        if (nextPage == null) {
            next.setVisibility(View.INVISIBLE);
        }

    }

    private class DownloaderPage extends AsyncTask<String, Void, PageQuestions> {
        Exception exception = null;

        protected PageQuestions doInBackground(String... params) {
            PageQuestions pageQuestions = null;
            try {
                if (params.length == 0) {
                    pageQuestions = new PageQuestions();
                } else {
                    String url = params[0];
                    pageQuestions = new PageQuestions(url);
                }
            } catch (Exception e) {
                exception = e;
            }
            return pageQuestions;
        }

        protected void onPostExecute(PageQuestions pageQuestions) {
            try {
                if (exception == null) {
                    viewPageQuestions(pageQuestions);
                } else {
                    setContentView(R.layout.error);
                }
            } catch (Exception e) {
                setContentView(R.layout.error);
            }
        }

    }
}