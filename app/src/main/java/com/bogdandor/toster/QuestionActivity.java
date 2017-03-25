package com.bogdandor.toster;

import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bogdandor.toster.entity.Question;

public class QuestionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<QuestionPresenter> {
    public static final String QUESTION_URL = "questionUrl";
    private View header;
    private TextView title;
    private ExpandableListView questionView;
    private QuestionPresenter presenter;
    private static final int LOADER_ID = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        header = getLayoutInflater().inflate(R.layout.question_header, null);
        title = (TextView) header.findViewById(R.id.title);
        questionView = (ExpandableListView) findViewById(R.id.question_view);
        questionView.addHeaderView(header);
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

    public void showQuestion(Question question) {
        title.setText(question.getTitle());
        QuestionAdapter adapter = new QuestionAdapter(this, question);
        questionView.setAdapter(adapter);
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
