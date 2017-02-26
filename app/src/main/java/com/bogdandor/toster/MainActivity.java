package com.bogdandor.toster;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bogdandor.toster.model.Loader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AsyncTask<Void, Void, String>() {
            Exception exception = null;

            protected String doInBackground(Void... params) {
                String text = null;
                try {
                    text = Loader.getInstance().getQuestions()[0].title;
                } catch (Exception e) {
                    exception = e;
                }
                return text;
            }

            protected void onPostExecute(String text) {
                if (exception == null) {
                    setContentView(R.layout.activity_main);
                    final TextView myTextView = (TextView) findViewById(R.id.myTextView);
                    myTextView.setText(text);
                } else {
                    setContentView(R.layout.error);
                }
            }
        }.execute();
    }
}