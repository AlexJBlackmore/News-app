package com.alexblackmore.newsfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView storiesListViewObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storiesListViewObj = (ListView)findViewById(R.id.xmlListView);

        Log.d(TAG, "onCreate: starting Asynctask");

        DownloadNewsStories downloadNewsStoriesObj = new DownloadNewsStories();
        downloadNewsStoriesObj.execute("http://feeds.bbci.co.uk/news/rss.xml");

        Log.d(TAG, "onCreate is finished and download started");
    }


    private class DownloadNewsStories extends AsyncTask<String, Void, String>{

        private static final String TAG = "DownloadNewsStories";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "onPostExecute: parameter is " + s);

            ParseStories parseStoriesObj = new ParseStories();
            parseStoriesObj.parse(s);

            FeedAdapter newsStoryArrayAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, parseStoriesObj.getNewsStoryArrayList());
            storiesListViewObj.setAdapter(newsStoryArrayAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.d(TAG, "doInBackground: starting");

            String rssFeed = downloadRssFeed(strings[0]);

            if(rssFeed == null){

                Log.e(TAG, "doInBackground: error downloading");

            }

            return rssFeed;
        }

        private String downloadRssFeed(String urlParam){

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL urlObj = new URL(urlParam);

                HttpURLConnection httpURLConnectionObj = (HttpURLConnection) urlObj.openConnection();

                InputStream inputStreamObj = httpURLConnectionObj.getInputStream();

                InputStreamReader inputStreamReaderObj = new InputStreamReader(inputStreamObj);

                BufferedReader bufferedReaderObj = new BufferedReader(inputStreamReaderObj);

                int response = httpURLConnectionObj.getResponseCode();

                Log.d(TAG, "downloadRssFeed: the response code was" + response);

                int charsRead;

                char[] inputBuffer = new char[500];

                while(true){

                    charsRead = bufferedReaderObj.read(inputBuffer);

                    if(charsRead < 0){

                        break;

                    }

                    if(charsRead > 0){

                        stringBuilder.append(String.copyValueOf(inputBuffer, 0, charsRead));

                    }

                }

                bufferedReaderObj.close();

                return stringBuilder.toString();

            } catch (MalformedURLException e){

                Log.e(TAG, "downloadRssFeed: Invalid URL" + e.getMessage());

            } catch (IOException e){

                Log.e(TAG, "downloadRssFeed: IO Exception reading data: " + e.getMessage());
                
            } catch (SecurityException e){

                Log.e(TAG, "downloadRssFeed: Security Exception, need permission?" + e.getMessage());
                
            }

            return null;

        }


    }

}
