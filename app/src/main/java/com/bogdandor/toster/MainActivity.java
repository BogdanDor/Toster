package com.bogdandor.toster;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView myTextView = (TextView) findViewById(R.id.myTextView);

        new AsyncTask<String, Void, String>() {

            protected String doInBackground(String... urls) {
                String result;
                try {
                    URL url = new URL(urls[0]);
                    result = getAllQuestion(urls[0]);
                } catch (Exception e) {
                    result = "Error";
                }
                return result;
            }

            protected void onProgressUpdate() {}

            protected void onPostExecute(String result) {
                myTextView.setText(Html.fromHtml(result));
            }
        }.execute("https://www.toster.ru");
    }


    /**
     * Given a URL, sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in String form. Otherwise,
     * it will throw an IOException.
     */
    private String downloadUrl(URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(30000000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(30000000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect() ;
            //publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            //publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
            if (stream != null) {
                result = inputStreamToString(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    private String inputStreamToString(InputStream stream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        InputStreamReader in = new InputStreamReader(stream, "UTF-8");
        int rsz = in.read(buffer, 0, buffer.length);
        while (rsz>=0) {
            out.append(buffer, 0, rsz);
            rsz = in.read(buffer, 0, buffer.length);
        }
        return out.toString();
    }

    private String getAllQuestion(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements questions = doc.select("h2.question__title");
        String result = questions.html();
        return result;
    }
}