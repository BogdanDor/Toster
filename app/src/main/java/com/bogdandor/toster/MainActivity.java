package com.bogdandor.toster;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.bogdandor.toster.model.Loader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView myTextView = (TextView) findViewById(R.id.myTextView);

        new AsyncTask<Void, Void, String>() {

            protected String doInBackground(Void... params) {
                String text;
                try {
                    text = Loader.getInstance().getQuestions()[0].title;
                } catch (Exception e) {
                    text = "Error";
                }
                return text;
            }

            protected void onPostExecute(String text) {
                myTextView.setText(fromHtml(text));
            }
        }.execute();
    }

    @SuppressWarnings("deprecation")
    private Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

}