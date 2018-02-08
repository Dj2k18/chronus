package com.example.jdhru.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.jdhru.application.Adaptor.FeedAdaptor;
import com.example.jdhru.application.Common.HTTPDataHandler;
import com.example.jdhru.application.model.RSSObject;
import com.google.gson.Gson;

public class trending extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;
    ImageView im;
    SwipeRefreshLayout s;
    BottomNavigationView bm;
    FloatingActionButton en,sci,world,he,sp;



    //RSS link
    private  String RSS_link="http://rss.nytimes.com/services/xml/rss/nyt/MostViewed.xml";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        en = (FloatingActionButton)findViewById(R.id.en);
        sci = (FloatingActionButton)findViewById(R.id.sci);
        world = (FloatingActionButton)findViewById(R.id.world);
        he = (FloatingActionButton)findViewById(R.id.health);
        sp = (FloatingActionButton)findViewById(R.id.sports);


        toolbar.setTitle("Trending");
        setSupportActionBar(toolbar);
        s = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        bm = (BottomNavigationView)findViewById(R.id.navigation);
        bm.setSelectedItemId(R.id.menu_trend);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadRSS();
        en.setOnClickListener(this);
        sci.setOnClickListener(this);
        world.setOnClickListener(this);
        he.setOnClickListener(this);
        sp.setOnClickListener(this);


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
                       Intent m = new Intent(getApplicationContext(),MainActivity.class);
                       startActivity(m);
                       finish();
                       break;
                   case R.id.menu_trend:
                       RSS_link = "http://rss.nytimes.com/services/xml/rss/nyt/MostViewed.xml";
                       loadRSS();
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

            ProgressDialog mDialog = new ProgressDialog(trending.this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.en:
                  RSS_link = "http://rss.nytimes.com/services/xml/rss/nyt/FashionandStyle.xml";
                  toolbar.setTitle("Fashion");
                  loadRSS();
                  break;
            case R.id.world:
                RSS_link = "http://rss.nytimes.com/services/xml/rss/nyt/World.xml";
                toolbar.setTitle("World");
                loadRSS();
                break;

            case R.id.sports:
                RSS_link = "http://rss.nytimes.com/services/xml/rss/nyt/Sports.xml";
                toolbar.setTitle("Sports");
                loadRSS();
                break;
            case R.id.sci:
                RSS_link = "http://rss.nytimes.com/services/xml/rss/nyt/Science.xml";
                toolbar.setTitle("Science");
                loadRSS();
                break;

            case R.id.health:
                RSS_link = "http://rss.nytimes.com/services/xml/rss/nyt/Health.xml";
                toolbar.setTitle("Health");
                loadRSS();
                break;



        }

    }
}
