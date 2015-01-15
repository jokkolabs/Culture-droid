package com.yeleman.culture_droid;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

        JSONArray jResource = null;
        try {
            // Retrieves all the elements in the 'countries' array
            jResource = jObject.getJSONArray("article");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Invoking getResources with the array of json object
        // where each json object represent a resource
        return getResource(jResource);
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

        HashMap<String, Object> resource = new HashMap<String, Object>();
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
            resource.put(Constants.KEY_PUBLISHED_ON, published_on);
            resource.put(Constants.KEY_TITLE, title);
            resource.put(Constants.KEY_CONTENT_SIZE, content_size);
            resource.put(Constants.KEY_ARTICLE_ID, articleId);
            resource.put(Constants.KEY_NB_COMMENTS, nb_comments);
            resource.put(Constants.KEY_THUMBNAIL, thumbnail);
            resource.put(Constants.KEY_CONTENT, content);
            resource.put(Constants.KEY_TAGS, tags);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resource;
    }

    public static String getJSONFromUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
        }
        return data;
    }
}