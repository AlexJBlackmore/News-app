package com.alexblackmore.newsfeed;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseStories {

    private static final String TAG = "ParseStories";

    ArrayList<NewsStory> newsStoryArrayList;

    public ParseStories(){

        //Include this keyword?
        newsStoryArrayList = new ArrayList<>();

    }

    public ArrayList<NewsStory> getNewsStoryArrayList() {

        return newsStoryArrayList;

    }

    public void parse(String xmlData){

        NewsStory currentRecord = null;
        boolean insideItem = false;
        String textJustParsed = "";

        try{

            XmlPullParserFactory xmlPullParserFactoryObj = XmlPullParserFactory.newInstance();
            xmlPullParserFactoryObj.setNamespaceAware(true);
            XmlPullParser xmlPullParserObj = xmlPullParserFactoryObj.newPullParser();
            xmlPullParserObj.setInput(new StringReader(xmlData));
            int eventType = xmlPullParserObj.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = xmlPullParserObj.getName();
                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if("item".equalsIgnoreCase(tagName)){
                            insideItem = true;
                            currentRecord = new NewsStory();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textJustParsed = xmlPullParserObj.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if(insideItem){
                            if("item".equalsIgnoreCase(tagName)){
                                newsStoryArrayList.add(currentRecord);
                                insideItem = false;
                            } else if("title".equalsIgnoreCase(tagName)){
                                currentRecord.setTitle(textJustParsed);
                            } else if("description".equalsIgnoreCase(tagName)){
                                currentRecord.setDescription(textJustParsed);
                            } else if("link".equalsIgnoreCase(tagName)){
                                currentRecord.setLink(textJustParsed);
                            }
                        }
                        break;

                    default:
                }

                eventType = xmlPullParserObj.next();

            }

            for(NewsStory ns : newsStoryArrayList){

                Log.d(TAG, "**********************");
                Log.d(TAG, ns.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
