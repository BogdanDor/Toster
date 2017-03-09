package com.bogdandor.toster.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DataStore {

    public Document getPage(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
