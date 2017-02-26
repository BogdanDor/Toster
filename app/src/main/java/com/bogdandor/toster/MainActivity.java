package com.bogdandor.toster;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bogdandor.toster.model.Loader;
import com.bogdandor.toster.model.Question;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AsyncTask<Void, Void, Question[]>() {
            Exception exception = null;

            protected Question[] doInBackground(Void... params) {
                Question[] questions = null;
                try {
                    questions = Loader.getInstance().getQuestions();
                } catch (Exception e) {
                    exception = e;
                }
                return questions;
            }

            protected void onPostExecute(Question[] questions) {
                if (exception == null) {
                    setContentView(R.layout.activity_main);
                    ListView listQuestions = (ListView) findViewById(R.id.list_questions);
                    ArrayAdapter<Question> listAdapter = new ArrayAdapter<Question>(
                            MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            questions);
                    listQuestions.setAdapter(listAdapter);
                } else {
                    setContentView(R.layout.error);
                }
            }
        }.execute();
    }
}