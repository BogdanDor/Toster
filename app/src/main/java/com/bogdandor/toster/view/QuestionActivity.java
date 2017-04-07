package com.bogdandor.toster.view;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bogdandor.toster.presenter.QuestionPresenter;
import com.bogdandor.toster.R;
import com.bogdandor.toster.entity.Question;

public class QuestionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<QuestionPresenter> {
    public static final String QUESTION_URL = "questionUrl";
    private RecyclerView questionView;
    private QuestionPresenter presenter;
    private static final int LOADER_ID = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionView = (RecyclerView) findViewById(R.id.activity_question);
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
        //title.setText(question.getTitle());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        questionView.setLayoutManager(linearLayoutManager);
        QuestionAdapter adapter = new QuestionAdapter(R.layout.question_header, R.id.title, R.layout.item, R.id.name_author, R.id.text_message, question);
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
}
