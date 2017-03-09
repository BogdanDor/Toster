package com.bogdandor.toster;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.bogdandor.toster.data.Repository;
import com.bogdandor.toster.model.Question;

public class QuestionActivity extends AppCompatActivity {
    public static final String QUESTION_URL = "questionUrl";
    private TextView title;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);

        String questionUrl = (String) getIntent().getExtras().get(QUESTION_URL);
        new DownloaderQuestion().execute(questionUrl);
    }

    void viewQuestion(Question question) {
        title.setText(question.getTitle());
        text.setText(fromHtml(question.getText()));
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
                viewQuestion(question);
            } else {
                setContentView(R.layout.error);
            }
        }
    }
}
