package com.yeleman.culture_droid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** A class to parse json data */
public class JSONParser {

    private static final String TAG = Constants.getLogTag("JSONParser");

    // Receives a JSONObject and returns a list
    public List<HashMap<String,Object>> parse(JSONObject jObject){

        JSONArray jArticle = null;
        try {
            // Retrieves all the elements in the 'countries' array
            jArticle = jObject.getJSONArray("article");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Invoking getResources with the array of json object
        // where each json object represent a resource
        return getResource(jArticle);
    }

    private List<HashMap<String, Object>> getResource(JSONArray jResource){
        int resourceCount = jResource.length();
        List<HashMap<String, Object>> resourceList = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> resource = null;

        // Taking each resource, parses and adds to list object
        for(int i=0; i<resourceCount;i++){
            try {
                // Call getResource with resource JSON object to parse the resource
                resource = getResource((JSONObject) jResource.get(i));
                resourceList.add(resource);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resourceList;
    }

    // Parsing the Resource JSON object
    private HashMap<String, Object> getResource(JSONObject jResource){

        HashMap<String, Object> articles = new HashMap<String, Object>();
        String published_on = "";
        String title =   "";
        String content_size ="";
        String articleId = "";
        String nb_comments = "";
        String thumbnail = "";
        String content = "";
        JSONArray tags;

        try {
            published_on = jResource.getString(Constants.KEY_PUBLISHED_ON);
            title = jResource.getString(Constants.KEY_TITLE);
            content_size = jResource.getString(Constants.KEY_CONTENT_SIZE);
            articleId = jResource.getString("id");
            nb_comments = jResource.getString(Constants.KEY_NB_COMMENTS);
            thumbnail = jResource.getString(Constants.KEY_THUMBNAIL);
            content = "";
            tags = jResource.getJSONArray(Constants.KEY_TAGS);
            articles.put(Constants.KEY_PUBLISHED_ON, published_on);
            articles.put(Constants.KEY_TITLE, title);
            articles.put(Constants.KEY_CONTENT_SIZE, content_size);
            articles.put(Constants.KEY_ARTICLE_ID, articleId);
            articles.put(Constants.KEY_NB_COMMENTS, nb_comments);
            articles.put(Constants.KEY_THUMBNAIL, thumbnail);
            articles.put(Constants.KEY_CONTENT, content);
            articles.put(Constants.KEY_TAGS, tags);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return articles;
    }
}