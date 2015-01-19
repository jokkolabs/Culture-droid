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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.webkit.WebView;
import android.widget.ListView;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;


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
        private WebView contentView;

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
            View rootView = inflater.inflate(R.layout.web_view, container,
                    false);
            contentView = (WebView) rootView.findViewById(R.id.webView);
            String htmlString = "<Html><h1>DJ DASHI</h1></html>";
            //contentView.loadDataWithBaseURL("file:///android_asset/", htmlString, "text/html", "utf-8", null);
            contentView.loadUrl("file:///android_asset/about.html");
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
            final View rootView = inflater.inflate(R.layout.article, container, false);
            Log.d(TAG , "onCreateView");

            mListView = (ListView) rootView.findViewById(R.id.list);
            context = container.getContext();
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
                    articleElement.setPublishedOn(Constants.dateToStrDate(news.getPublishedOn()));
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
