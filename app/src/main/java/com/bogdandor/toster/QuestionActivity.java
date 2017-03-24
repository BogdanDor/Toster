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
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.bogdandor.toster.entity.Answer;
import com.bogdandor.toster.entity.Comment;
import com.bogdandor.toster.entity.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<QuestionPresenter> {
    public static final String QUESTION_URL = "questionUrl";
    private View header;
    private TextView title;
    private ExpandableListView expListQuestion;
    private QuestionPresenter presenter;
    private static final int LOADER_ID = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        header = getLayoutInflater().inflate(R.layout.question_header, null);
        title = (TextView) header.findViewById(R.id.title);
        expListQuestion = (ExpandableListView) findViewById(R.id.exp_list_question);
        expListQuestion.addHeaderView(header);
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

        Map<String, String> map;
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        ArrayList<ArrayList<Map<String, String>>> childDataList = new ArrayList<>();
        ArrayList<Map<String, String>> childDataItemList;

        map = new HashMap<>();
        map.put("groupName", question.getText());
        groupDataList.add(map);
        childDataItemList = new ArrayList<>();
        Comment[] comments = question.getComments();
        if (comments != null) {
            for (Comment comment : comments) {
                map = new HashMap<>();
                map.put("comment", comment.getText());
                childDataItemList.add(map);
            }
            childDataList.add(childDataItemList);
        }

        Answer[] answers = question.getAnswers();
        if (answers != null) {
            for (Answer answer : answers) {
                map = new HashMap<>();
                map.put("groupName", answer.getText());
                groupDataList.add(map);
                childDataItemList = new ArrayList<>();
                comments = answer.getComments();
                if (comments != null) {
                    for (Comment comment : comments) {
                        map = new HashMap<>();
                        map.put("comment", comment.getText());
                        childDataItemList.add(map);
                    }
                    childDataList.add(childDataItemList);
                }
            }
        }

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this,
                groupDataList,
                R.layout.group,
                new String[] { "groupName" },
                new int[] { R.id.text_group },
                childDataList,
                R.layout.item,
                new String[] { "comment" },
                new int[] { R.id.text_item }
        );
        expListQuestion.setAdapter(adapter);
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
