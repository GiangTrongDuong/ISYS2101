package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsPages extends AppCompatActivity {

    private RecyclerView recyclerViewNew;
    private ArrayList<NewsItem> news;

    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_pages);

        news = new ArrayList<>();
        recyclerViewNew = findViewById(R.id.recyclerViewNew);
        adapter = new NewsAdapter(this);
        recyclerViewNew.setAdapter(adapter);
        recyclerViewNew.setLayoutManager(new LinearLayoutManager(this));

        new GetNews().execute();
    }
    private class GetNews extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            //Create input stream that give stream of data from the internet (pull news)
            InputStream inputStream = getInputStream();
            if (null != inputStream) {
                try {
                    initXMLPullParser(inputStream);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            adapter.setNews(news);
        }

        private InputStream getInputStream(){
            try {
                URL url = new URL("https://thanhnien.vn/rss/du-lich.rss");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                return connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
            Log.d(TAG, "initXMLPullParser: Initializing XML Pull Parser");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream, null);
            parser.next();

            parser.require(XmlPullParser.START_TAG,null,"rss");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                parser.require(XmlPullParser.START_TAG, null, "channel");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }

                    if (parser.getName().equals("item")){
                        parser.require(XmlPullParser.START_TAG, null, "item");

                        String title = "";
                        String description = "";
                        String link = "";
                        String date = "";

                        while (parser.next() != XmlPullParser.END_TAG) {
                            if (parser.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }

                            String tagName = parser.getName();
                            if (tagName.equals("title")) {
                                // get the content of title
                                title = getContent(parser, "title");
                            } else if (tagName.equals("description")) {
                                //get the content of description
                                description = getContent(parser, "description");
                            } else if (tagName.equals("link")) {
                                //get the content of link
                                link = getContent(parser, "link");
                            } else if (tagName.equals("pubdate")) {
                                //get the content of published date
                                date = getContent(parser, "pubdate");
                            } else {
                                //Skip tag
                                skipTag(parser);
                            }
                        }

                        NewsItem item = new NewsItem(title, description, link, date);
                        news.add(item);
                    }else {
                        //Skip Tag
                        skipTag(parser);
                    }
                }
            }
        }

        private String getContent (XmlPullParser parser, String tagName) throws XmlPullParserException, IOException {
            String content = "";
            parser.require(XmlPullParser.START_TAG,null,tagName);
            if (parser.next() == XmlPullParser.TEXT) {
                content = parser.getText();
                parser.next();
            }
            return content;
        }

        private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }

            int number = 1;

            while (number != 0) {
                switch (parser.next()) {
                    case XmlPullParser.START_TAG:
                        number++;
                        break;
                    case XmlPullParser.END_TAG:
                        number--;
                        break;
                    default:
                        break;
                }
            }
        }
    }
}