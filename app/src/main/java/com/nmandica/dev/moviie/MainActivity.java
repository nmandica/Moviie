package com.nmandica.dev.moviie;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_TAB = "selected_tab";
    public static final int ALL_MOVIES = 0;
    public static final int MOVIES_TO_SEE = 1;
    public static final int MOVIES_SEEN = 2;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private TextView textView;

    MoviesAdapter.MovieClickListener movieClickListener = new MoviesAdapter.MovieClickListener()
    {
        @Override
        public void onMovieClickListener(Movie movie)
        {
            Intent intent = MovieActivity.newIntent(getApplicationContext(), movie);
            startActivity(intent);
        }
    };

    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setSelectedTab(position);
            new GetMyMoviesTask().execute(getSelectedTab());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private int getSelectedTab()
    {
        return getPreferences(MODE_PRIVATE).getInt(SELECTED_TAB, 0);
    }

    private void setSelectedTab(int selectedTab)
    {
        getPreferences(MODE_PRIVATE).edit().putInt(SELECTED_TAB, selectedTab).apply();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Realm
        Realm.init(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, MoviesListActivity.class);
                startActivity(intent);
            }
        });

        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.textView = (TextView) findViewById(R.id.textview);
        this.spinner = (Spinner) findViewById(R.id.spinner);

        this.spinner.setOnItemSelectedListener(this.spinnerListener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_about) {
            Intent aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(aboutIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.spinner.setSelection(getSelectedTab());

        new GetMyMoviesTask().execute(getSelectedTab());
    }

    private class GetMyMoviesTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(Integer... tab) {
            final RealmResults<Movie> result;

            // Get a Realm instance for this thread
            Realm realm = Realm.getDefaultInstance();

            switch (tab[0]) {
                case ALL_MOVIES :
                    result = realm.where(Movie.class).findAll();
                    break;
                case MOVIES_TO_SEE :
                    result = realm.where(Movie.class).equalTo("seen", false).findAll();
                    break;
                default:
                case MOVIES_SEEN :
                    result = realm.where(Movie.class).equalTo("seen", true).findAll();
            }

            ArrayList<Movie> myMovies = (ArrayList<Movie>) realm.copyFromRealm(result);

            realm.close();

            return myMovies;
        }

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            progressBar.setVisibility(View.GONE);

            if (movies.isEmpty())
            {
                textView.setVisibility(View.VISIBLE);
            }

            recyclerView.setVisibility(View.VISIBLE);

            final MoviesAdapter moviesAdapter = new MoviesAdapter(movies);
            moviesAdapter.setListener(movieClickListener);

            recyclerView.setAdapter(moviesAdapter);
        }
    }
}
