package com.yeleman.culture_droid;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.orm.query.Select;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


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
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
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

        switch (number) {
            case 1:
                mTitle = getString(R.string.about);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, About.newInstance(number + 1)).commit();
                break;
            case 2:
                mTitle = getString(R.string.news);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, News.newInstance(number + 2)).commit();
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

        private static final String ARG_SECTION_NUMBER_1 = "section_number_1";

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
            super.onAttach(activity);
            ((CultureHome) activity).onSectionAttached(getArguments().getInt(
                    ARG_SECTION_NUMBER_1));
        }
    }
    /**
     * News
     */
    public static class News extends Fragment {

        private static final String ARG_SECTION_NUMBER_1 = "section_number_2";
        private ListView mList;
        private ListView mListView;
        private ArrayAdapter<NewsData> listAdapter ;
        private SimpleAdapter adapter;

        public static News newInstance(int sectionNumber) {
            News fragment = new News();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER_1, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public News() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.news, container, false);
            new GetRssFeed().execute("http://quandlevillagesereveille.wordpress.com/feed/");

            String[] from = {"title", "description", "date"};
            int[] to = {R.id.title, R.id.description, R.id.dateP};

            mListView = (ListView) rootView.findViewById(R.id.list);
            List<Map<String, ?>> data = getData();
            adapter = new SimpleAdapter(container.getContext(), data, R.layout.basic_list_item, from, to);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem((int) id);
                    String selectId = (String) hm.get("id");
                    Log.d(TAG, selectId + " has selected");
                   // Intent a = new Intent(CultureHome.this, About.class);
                   // startActivity(a);
                }
            });
            mListView.setAdapter(adapter);
            return rootView;
        }

        List<Map<String, ?>> getData() {
            List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
            List<NewsData> reports;

            reports = Select.from(NewsData.class).orderBy("title").list();
            for (NewsData rpt: reports){
                //Date date = rpt.getpubDate();
                Map map = new HashMap();
                map.put("id", String.valueOf(rpt.getId()));
                map.put("title", rpt.getTitle());
                map.put("description", Html.fromHtml(rpt.getDescription()));
                map.put("date", rpt.getpubDate());
                list.add(map);
            }
            return list;
        }

        private class GetRssFeed extends AsyncTask<String, Void, Void> {
            @Override
            protected Void doInBackground(String... params) {
                Log.d(TAG, "doInBackground" + " " + params[0]);
                try {
                    URL url = new URL(params[0]);
                    RssFeed feed = RssReader.read(url);

                    ArrayList<RssItem> rssItems = feed.getRssItems();
                    for (RssItem item : rssItems) {
                        Log.i(TAG, item.getTitle());
                        List<NewsData> news = NewsData.find(NewsData.class, "title = ?", item.getTitle());
                        if (news.isEmpty()){
                            NewsData newsData = new NewsData(item.getPubDate(),
                                                            item.getTitle(),
                                                            item.getDescription(),
                                                            item.getContent(),
                                                            "status",
                                                            "category");
                            newsData.save();
                        } else {
                            Log.d(TAG, "Existe déjà dans la base");
                        }
                       }
                } catch (Exception e) {
                    Log.v(TAG, String.valueOf(e));
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
                mListView.setAdapter(adapter);
            }
        }

        protected  void updateNewsStatusData(long sid, String status) {
            NewsData rpt =  NewsData.findById(NewsData.class, sid);
            rpt.setTitle(status);
            rpt.save();
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((CultureHome) activity).onSectionAttached(getArguments().getInt(
                    ARG_SECTION_NUMBER_1));
        }
    }
}
