package com.bogdandor.toster;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {
    public static final String QUESTION_URL = "questionUrl";
    public QuestionPresenter presenter;
    private TextView title;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        presenter = new QuestionPresenter(this);
        presenter.onCreate();
    }

    public void showQuestionTitle(String s) {
        title.setText(s);
    }

    public void showQuestionText(String s) {
        text.setText(fromHtml(s));
    }

    public void showError() {
        setContentView(R.layout.error);
    }

    public String getQuestionUrl() {
        return (String) getIntent().getExtras().get(QUESTION_URL);
    }

    @SuppressWarnings("deprecation")
    private Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
