package com.bogdandor.toster;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String siteUrl = "https://www.toster.ru";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView myTextView = (TextView) findViewById(R.id.myTextView);

        new AsyncTask<String, Void, String>() {

            protected String doInBackground(String... urls) {
                String result;
                try {
                    result = getAllQuestion(urls[0]);
                } catch (Exception e) {
                    result = "Error";
                }
                return result;
            }

            protected void onPostExecute(String result) {
                myTextView.setText(fromHtml(result));
            }
        }.execute(siteUrl);
    }

    @SuppressWarnings("deprecation")
    public Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    private String getAllQuestion(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements questions = doc.select("h2.question__title");
        String result = questions.html();
        return result;
    }
}