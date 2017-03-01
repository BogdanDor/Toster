package com.bogdandor.toster;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.bogdandor.toster.model.PageQuestions;
import com.bogdandor.toster.model.Question;

import java.io.IOException;

public class QuestionActivity extends AppCompatActivity {
    public static final String EXTRA_QUESTIONNO = "questionNo";
    private TextView title;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);

        int questionNo = (Integer) getIntent().getExtras().get(EXTRA_QUESTIONNO);
        try {
            Question question = PageQuestions.currentPage.getQuestions()[questionNo];
            new DownloaderQuestion().execute(question);
        } catch (IOException e) {
            setContentView(R.layout.error);
        }
    }

    void viewQuestion(Question question) {
        title.setText(question.title);
        text.setText(fromHtml(question.text));
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

    private class DownloaderQuestion extends AsyncTask<Question, Void, Question> {
        Exception exception = null;

        protected Question doInBackground(Question... params) {
            Question question = null;
            try {
                question = params[0];
                question.load();
            } catch (Exception e) {
                exception = e;
            }
            return question;
        }

        protected void onPostExecute(Question question) {
            if (exception == null) {
                viewQuestion(question);
            } else {
                setContentView(R.layout.error);
            }
        }

    }

}
