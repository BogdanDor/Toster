package com.bogdandor.toster;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.bogdandor.toster.data.Repository;
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
        new DownloaderPageQuestions().execute();
    }

    public void onClickPrev(View view) {
        new DownloaderPageQuestions().execute(prevPage);
    }

    public void onClickNext(View view) {
        new DownloaderPageQuestions().execute(nextPage);
    }

    void viewPageQuestions(final PageQuestions pageQuestions) throws IOException {
        final Question[] questions = pageQuestions.getQuestions();
        ArrayAdapter<Question> listAdapter = new ArrayAdapter<Question>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                questions);
        listQuestions.setAdapter(listAdapter);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra(QuestionActivity.QUESTION_URL, pageQuestions.getQuestions()[i].getUrl());
                startActivity(intent);
            }
        };
        listQuestions.setOnItemClickListener(itemClickListener);

        prevPage = pageQuestions.getPrevPageUrl();
        prev.setVisibility(View.VISIBLE);
        if (prevPage == null) {
            prev.setVisibility(View.INVISIBLE);
        }
        nextPage = pageQuestions.getNextPageUrl();
        next.setVisibility(View.VISIBLE);
        if (nextPage == null) {
            next.setVisibility(View.INVISIBLE);
        }

    }

    private class DownloaderPageQuestions extends AsyncTask<String, Void, PageQuestions> {
        Exception exception = null;

        protected PageQuestions doInBackground(String... params) {
            PageQuestions pageQuestions = null;
            Repository repository = new Repository();
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