package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Choreographer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.orm.query.Select;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.LoggingPermission;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


public class CultureHome extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = Constants.getLogTag("CultureHome");
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
        getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();

        switch (number) {

            case 1:
                ft.replace(R.id.container, About.newInstance(number + 1)).commit();
                mTitle = getString(R.string.about);
                break;
            case 2:
                ft.replace(R.id.container, News.newInstance(number + 2)).commit();
                mTitle = getString(R.string.all_article);
                break;
            case 3:
                mTitle = getString(R.string.culture);
                break;
            case 4:
                mTitle = getString(R.string.discovery);
                break;
            case 5:
                mTitle = getString(R.string.ml);
                break;
            case 6:
                mTitle = getString(R.string.proverb);
                break;
            case 7:
                mTitle = getString(R.string.traditional);
                break;
            case 8:
                mTitle = getString(R.string.uncategorized);
                break;
            case 9:
                mTitle = getString(R.string.village);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.culture_home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_culture_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((CultureHome) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /**
     * About
     */
    public static class About extends Fragment {

        private static final String ARG_SECTION_NUMBER_1 = "about";

        public static About newInstance(int sectionNumber) {
            About fragment = new About();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER_1, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public About() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.about, container,
                    false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            Log.d(TAG, "onAttach About");
            super.onAttach(activity);
        }
    }

    /**
     * News
     */
    public static class News extends Fragment {

        private static final String ARG_SECTION_NUMBER_2 = "News";
        private ListView mListView;
        private Context context;

        public static News newInstance(int sectionNumber) {
            News fragment = new News();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER_2, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public News() {
        }

        @Override
        public void onResume() {
            super.onResume();
            setupUI();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.news, container, false);

            mListView = (ListView) rootView.findViewById(R.id.list);

            String urlJson = Constants.getUrl("articles.json");
            context = container.getContext();
            new GetJson().execute(urlJson);

            ArticleElement articleElement;
            ArrayList<ArticleElement> articleElements = new ArrayList<ArticleElement>();
            List<NewsData> newsDataList;
            newsDataList = Select.from(NewsData.class).orderBy("id").list();

            for (NewsData news : newsDataList) {
                articleElement = new ArticleElement();
                articleElement.setArticleId(Integer.parseInt(String.valueOf(news.getArticleId())));
                if (String.valueOf(news.getThumbnail()).equals("null")) {
                    articleElement.setEncodedThumbnail(Constants.DEFAULTHUMBNAIL);
                } else {
                    articleElement.setEncodedThumbnail(String.valueOf(news.getThumbnail()));
                }
                articleElement.setTitle(news.getTitle());
                articleElement.setPublishedOn(Constants.formatDatime(news.getPublishedOn()));
                articleElement.setContentSize(Constants.displaySizeForArticleContent(news));
                articleElement.setLocal(news.getContent().toString() != "");
                articleElements.add(articleElement);
            }

            mListView.setAdapter(new ArticleElementsAdapter(context, articleElements));
            return rootView;
        }

        public void setupUI() {

        }

        private class GetJson extends AsyncTask<String, Void, Void> {

            JSONObject jObject;
            JSONParser jParser = new JSONParser();

            String data = null;
            private ProgressDialog Dialog = new ProgressDialog(context.getApplicationContext());

            @Override
            protected void onPreExecute() {
                // Loading

              // Dialog.setMessage("Chargement en cours ...");
               //Dialog.show();
            }

            @Override
            protected Void doInBackground(String... params) {
                try {
                    data = JSONParser.getJSONFromUrl(params[0]);
                } catch (IOException e) {
                    Log.d(TAG, "IOException " + e.toString());
                } catch (Exception e) {
                    Log.e(TAG, "Exception" + e);
                    return null ;
                }
                try {
                    jObject = new JSONObject(data);
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException " + e.toString());
                }

                List<HashMap<String, Object>> resources = null;
                resources = jParser.parse(jObject);

                for (HashMap<String, Object> article : resources) {
                    String publishedOn = article.get(Constants.KEY_PUBLISHED_ON).toString();
                    String articleId = article.get(Constants.KEY_ARTICLE_ID).toString();
                    String thumbnail = article.get(Constants.KEY_THUMBNAIL).toString();
                    String title = article.get(Constants.KEY_TITLE).toString();
                    String nbComments = article.get(Constants.KEY_NB_COMMENTS).toString();
                    String contentSize = article.get(Constants.KEY_CONTENT_SIZE).toString();
                    String content = article.get(Constants.KEY_CONTENT).toString();
                    List<NewsData> news = NewsData.find(NewsData.class, "articleId = ?", articleId);
                    if (news.isEmpty()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            date = dateFormat.parse(publishedOn.replace("T", " "));
                        } catch (ParseException e) {
                            Log.d(TAG, "ParseException" + e.toString());
                        }
                        NewsData newsData = new NewsData(date,
                                articleId,
                                thumbnail,
                                title,
                                nbComments,
                                contentSize,
                                content);
                        newsData.save();
                    } else {
                        Log.d(TAG, "Existe déjà dans la base");
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                /*if (isOnline()) {
                    Dialog.dismiss();
                }else{
                    //Dialog.
                    Popups.getStandardProgressDialog(CultureHome.this,
                            String.valueOf(R.string.required_connexion_title),
                            String.valueOf(R.string.required_connexion_body), true);
                }*/
            }
        }

        @Override
        public void onAttach(Activity activity) {
            Log.d(TAG, "onAttach News");
            super.onAttach(activity);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK :
                new AlertDialog.Builder(this)
                        .setTitle("Quitter")
                        .setMessage("Voulez vous vraiment quitter ?")
                        .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                        System.exit(0);
                                }})
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                }).create().show();
                return true;
        }
        return false;
    }

}
