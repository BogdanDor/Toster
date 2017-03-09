package com.bogdandor.toster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    MainPresenter presenter;
    ListView listQuestions;
    Button prev;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);
        listQuestions = (ListView) findViewById(R.id.list_questions);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        presenter.onCreate();
    }

    public void onClickPrev(View view) {
        presenter.onClickPrev();
    }

    public void onClickNext(View view) {
        presenter.onClickNext();
    }

    public void showPrevButton(String url) {
        if (url != null) {
            prev.setVisibility(View.VISIBLE);
        } else {
            prev.setVisibility(View.INVISIBLE);
        }
    }

    public void showNextButton(String url) {
        if (url != null) {
            next.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.INVISIBLE);
        }
    }

    public void showArray(Object[] objects) {
        ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<Object>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                objects);
        listQuestions.setAdapter(arrayAdapter);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onItemClickList(i);
            }
        };
        listQuestions.setOnItemClickListener(itemClickListener);
    }

    public void showQuestion(String urlQuestion) {
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra(QuestionActivity.QUESTION_URL, urlQuestion);
        startActivity(intent);
    }

    public void showError() {
        setContentView(R.layout.error);
    }
}