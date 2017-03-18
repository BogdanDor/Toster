package com.bogdandor.toster;

import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<QuestionPresenter> {
    public static final String QUESTION_URL = "questionUrl";
    private View header;
    private TextView title;
    private TextView text;
    private ListView listAnswers;
    private QuestionPresenter presenter;
    private static final int LOADER_ID = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        header = getLayoutInflater().inflate(R.layout.question_header, null);
        listAnswers = (ListView) findViewById(R.id.list_answers);
        title = (TextView) header.findViewById(R.id.title);
        text = (TextView) header.findViewById(R.id.text);
        listAnswers.addHeaderView(header);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
    }

    @Override
    protected void onStop() {
        presenter.onViewDetached();
        super.onStop();
    }

    public void showQuestionTitle(String s) {
        title.setText(s);
    }

    public void showQuestionText(String s) {
        text.setText(fromHtml(s));
    }

    public void showArray(Object[] objects) {
        ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<Object>(
                QuestionActivity.this,
                android.R.layout.simple_list_item_1,
                objects);
        listAnswers.setAdapter(arrayAdapter);
    }

    public void showError() {
        setContentView(R.layout.error);
    }

    public String getQuestionUrl() {
        return (String) getIntent().getExtras().get(QUESTION_URL);
    }

    @Override
    public Loader<QuestionPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new QuestionPresenter(this));
    }

    @Override
    public void onLoadFinished(Loader<QuestionPresenter> loader, QuestionPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<QuestionPresenter> loader) {
        presenter = null;
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
