package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
        if (number == 1) {
            ft.replace(R.id.container, AboutFragment.newInstance(number + 1)).commit();
            mTitle = getString(R.string.about);
        } else {
            number -= 1;
            String tagName = String.valueOf(TagData.getListTags().get(number));
            ft.replace(R.id.container, ArticleFragment.newInstance(number, tagName)).commit();
            mTitle = tagName;
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
        if (id == R.id.all_dl) {

            Intent intent = new Intent(
                    getApplicationContext(),
                    SaveAllArticleContent.class);
            startActivity(intent);
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
     * AboutFrFragment
     */
    public static class AboutFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER_1 = "about";

        public static AboutFragment newInstance(int sectionNumber) {
            AboutFragment fragment = new AboutFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER_1, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public AboutFragment() {
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
            Log.d(TAG, "onAttach AboutFragment");
            super.onAttach(activity);
        }
    }

    /**
     * ArticleFragment
     */
    public static class ArticleFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER_2 = "ArticleFragment";
        private ListView mListView;
        private Context context;

        private ArticleElementsAdapter mAdapter;
        //private ViewGroup containers;

        public static ArticleFragment newInstance(int sectionNumber, String tagName) {
            ArticleFragment fragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER_2, sectionNumber);
            args.putString("tag", tagName);
            fragment.setArguments(args);
            return fragment;
        }

        public ArticleFragment() {
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
            Log.d(TAG , "onCreateView");

            mListView = (ListView) rootView.findViewById(R.id.list);
            String urlJson = Constants.getUrl("articles.json");
            context = container.getContext();
            new GetJson().execute(urlJson);
            setupUI();
            return rootView;
        }

        public void setupUI() {
            String tagName = getArguments().getString("tag");
            ArticleElement articleElement;
            ArrayList<ArticleElement> articleElements = new ArrayList<ArticleElement>();
            List<ArticleData> articleDataList;

            if (tagName.equals(Constants.TAG_ALL)){
                articleDataList = Select.from(ArticleData.class).orderBy("id").list();
            } else {
                articleDataList = ArticleData.allByTagName(getArguments().getString("tag"));
            }
            try {
                for (ArticleData news : articleDataList) {
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
            }catch (Exception e){
                Log.e(TAG, "newsDataList-" + e);
            }
            mAdapter = new ArticleElementsAdapter(context, articleElements);
            mListView.setAdapter(mAdapter);
        }

        private class GetJson extends AsyncTask<String, Void, Void> {

            JSONObject jObject;
            JSONParser jParser = new JSONParser();

            String data = null;
            private ProgressDialog progressDialog;

            public boolean isOnline() {
                ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onPreExecute() {
                // Loading
                if (!isOnline()) {
                    Toast toast = Toast.makeText(context.getApplicationContext(), R.string.required_connexion_title,
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else {
                    progressDialog = Popups.getStandardProgressDialog(getActivity(), "",
                            getString(R.string.loading), false);
                    progressDialog.show();
                }
            }

            @Override
            protected Void doInBackground(String... params) {
                try {
                    data = JSONParser.getJSONFromUrl(params[0]);
                } catch (IOException e) {
                    Log.d(TAG, "doInBackground IOException " + e.toString());
                } catch (Exception e) {
                    Log.e(TAG, "doInBackground Exception" + e + "\nLe lien (url) vers la liste des articles est mort.");
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
                    JSONArray tags = (JSONArray) article.get(Constants.KEY_TAGS);
                    List<ArticleData> news = ArticleData.find(ArticleData.class, "articleId = ?", articleId);
                    if (news.isEmpty()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            date = dateFormat.parse(publishedOn.replace("T", " "));
                        } catch (ParseException e) {
                            Log.d(TAG, "ParseException" + e.toString());
                        }
                        ArticleData articleData = new ArticleData(date,
                                articleId,
                                thumbnail,
                                title,
                                nbComments,
                                contentSize,
                                content);
                        articleData.saveWithId();
                        Log.d(TAG, "Creating article: "+ articleData.getArticleId() + " with ID: "+ articleData.getId());
                        articleData.articleTagSave(Constants.listStringFromJsonArray(tags));
                    } else {
                        //Log.d(TAG, "Existe déjà dans la base");
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isOnline()) {
                    try {
                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (final Exception e) {
                        progressDialog = null;
                    }
                }
                setupUI();
            }
        }

        @Override
        public void onAttach(Activity activity) {
            Log.d(TAG, "onAttach ArticleFragment");
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
