package com.bogdandor.toster.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.bogdandor.toster.presenter.MainPresenter;
import com.bogdandor.toster.R;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MainPresenter> {
    private View footer;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ListView listQuestions;
    private Button prev;
    private Button next;
    private MainPresenter presenter;
    private static final int LOADER_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        footer = getLayoutInflater().inflate(R.layout.main_footer, null);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawer = (DrawerLayout) findViewById(R.id.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listQuestions = (ListView) findViewById(R.id.list_questions);
        prev = (Button) footer.findViewById(R.id.prev);
        next = (Button) footer.findViewById(R.id.next);
        listQuestions.addFooterView(footer);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener());
        drawer.addDrawerListener(new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                R.layout.item,
                R.id.text,
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

    @Override
    public Loader<MainPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new MainPresenter(this));
    }

    @Override
    public void onLoadFinished(Loader<MainPresenter> loader, MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<MainPresenter> loader) {
        presenter = null;
    }

    private class NavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int i = item.getItemId();
            switch (item.getItemId()) {
                case R.id.latest:
                    presenter.onLatestClick();
                    break;
                case R.id.interesting:
                    presenter.onInterestingClick();
                    break;
                case R.id.without_answer:
                    presenter.onWithoutAnswerClick();
                    break;
            }
            item.setChecked(true);
            setTitle(item.getTitle());
            drawer.closeDrawers();
            return false;
        }
    }
}