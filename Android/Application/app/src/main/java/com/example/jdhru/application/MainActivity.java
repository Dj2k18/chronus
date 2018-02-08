package com.example.jdhru.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;

import com.example.jdhru.application.Adaptor.FeedAdaptor;
import com.example.jdhru.application.Common.HTTPDataHandler;
import com.example.jdhru.application.model.RSSObject;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;
    ImageView im;
    SwipeRefreshLayout s;
    BottomNavigationView bm;


    //RSS link
    private  String RSS_link="http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Chronus");
        setSupportActionBar(toolbar);
        s = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        bm = (BottomNavigationView)findViewById(R.id.navigation);



        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadRSS();

        s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRSS();
                s.setRefreshing(false);
            }
        });

        bm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        RSS_link = "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml";
                        loadRSS();
                        break;
                    case R.id.menu_trend:
                        Intent m = new Intent(getApplicationContext(),trending.class);
                        startActivity(m);
                        finish();
                        break;
                    case R.id.menu_sup:
                        toolbar.setTitle("Feeling Bored.....");
                        String[] url = new String[]{"http://rss.nytimes.com/services/xml/rss/nyt/Arts.xml","http://rss.nytimes.com/services/xml/rss/nyt/Travel.xml",
                                "http://rss.nytimes.com/services/xml/rss/nyt/RealEstate.xml","http://rss.nytimes.com/services/xml/rss/nyt/Space.xml","http://rss.nytimes.com/services/xml/rss/nyt/Environment.xml",
                                "http://rss.nytimes.com/services/xml/rss/nyt/Movies.xml","http://rss.nytimes.com/services/xml/rss/nyt/Books.xml",
                                "http://rss.nytimes.com/services/xml/rss/nyt/Music.xml","http://rss.nytimes.com/services/xml/rss/nyt/HealthCarePolicy.xml",
                                "http://rss.nytimes.com/services/xml/rss/nyt/EnergyEnvironment.xml","http://rss.nytimes.com/services/xml/rss/nyt/MediaandAdvertising.xml",
                                "http://rss.nytimes.com/services/xml/rss/nyt/Nutrition.xml"};
                        int size = url.length;
                        double r = Math.random();
                        int ra = (int) (r*(size-1));
                        RSS_link = url[ra];
                        loadRSS();
                        break;
                }
                return true;
            }
        });





    }

    private void loadRSS() {
        AsyncTask<String,String,String> loadRSSAsync = new AsyncTask<String, String, String>() {

            ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                mDialog.setMessage("Please wait...");
                mDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result = http.GetHTTPData(params[0]);
                return  result;
            }

            @Override
            protected void onPostExecute(String s) {
                mDialog.dismiss();
                rssObject = new Gson().fromJson(s,RSSObject.class);
                FeedAdaptor adapter = new FeedAdaptor(rssObject,getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        StringBuilder url_get_data = new StringBuilder(RSS_to_Json_API);
        url_get_data.append(RSS_link);
        loadRSSAsync.execute(url_get_data.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
}