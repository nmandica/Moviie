package com.nmandica.dev.moviie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nico on 16/01/2017.
 */

public class MovieActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = "extra_movie";
    private static final String ROOT_URL_IMAGE = "https://image.tmdb.org/t/p/w960_and_h540_bestv2";
    private static final String TAG = "com.ensiie.moviie.MoviesListActivity";

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkBoxSeen.getVisibility() == View.VISIBLE) {
                deleteMovieToSee(movie);
            } else {
                addMovieToSee(movie);
            }
        }
    };

    CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            movie.setSeen(isChecked);
            updateMovieToSee(movie);
        }
    };

    private CheckBox checkBoxSeen;
    private CollapsingToolbarLayout collapsingToolbar;
    private Realm realm;
    private TextView date;
    private FloatingActionButton fab;
    private ImageView image;
    private Movie movie;
    private TextView overview;
    private ProgressBar progressBar;
    private TextView rating;

    /**
     * Fill the movie view
     */
    private void fillView() {
        //Load the background image with Picasso
        Picasso.with(this).load(ROOT_URL_IMAGE + this.movie.getBackdropPath()).into(this.image);

        this.collapsingToolbar.setTitle(this.movie.getTitle());
        this.rating.setText(this.movie.getVoteAverage());
        this.overview.setText(this.movie.getOverview());

        //Format the release date
        SimpleDateFormat movieDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        try {
            Date releaseDate = movieDateFormat.parse(this.movie.getReleaseDate());
            this.date.setText(displayDateFormat.format(releaseDate));
        } catch (ParseException localParseException) {
            localParseException.printStackTrace();
        }
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();

        this.rating = ((TextView) findViewById(R.id.rating));
        this.overview = ((TextView) findViewById(R.id.synopsis));
        this.date = ((TextView) findViewById(R.id.date));
        this.image = ((ImageView) findViewById(R.id.image));
        this.fab = ((FloatingActionButton) findViewById(R.id.actionButton));
        this.checkBoxSeen = ((CheckBox) findViewById(R.id.seenCheckBox));
        this.progressBar = ((ProgressBar) findViewById(R.id.progress_bar));
        this.collapsingToolbar = ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar));

        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);

        this.fab.setOnClickListener(this.buttonClickListener);
        this.checkBoxSeen.setOnCheckedChangeListener(this.checkBoxListener);
        this.movie = ((Movie) getIntent().getSerializableExtra(EXTRA_MOVIE));

        fillView();

        getMovieFromDataBase(movie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_about) {
            Intent aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(aboutIntent);
        }
        else if(id == R.id.action_share) {
            String sharedText;

            if (this.checkBoxSeen.isChecked())
            {
                sharedText = String.format(getString(R.string.share_seen), this.movie.getTitle());
            }
            else
            {
                sharedText = String.format(getString(R.string.share_not_seen), this.movie.getTitle());
            }

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movie", this.movie);
    }

    public void addMovieToSee(final Movie movie) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(movie);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Original queries and Realm objects are automatically updated.
                fab.setImageResource(R.drawable.ic_delete_black_24dp);
                checkBoxSeen.setVisibility(View.VISIBLE);
            }
        });
    }

    public void deleteMovieToSee(final Movie movie) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Movie> result = realm.where(Movie.class).equalTo("id", movie.getId()).findAll();
                result.deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Original queries and Realm objects are automatically updated.
                fab.setImageResource(R.drawable.ic_add_black_24dp);
                checkBoxSeen.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void updateMovieToSee(final Movie movie) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final Movie realmMovie = realm.where(Movie.class).equalTo("id", movie.getId()).findFirst();
                realmMovie.setSeen(movie.isSeen());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Original queries and Realm objects are automatically updated.

            }
        });
    }

    public void getMovieFromDataBase(final Movie movie) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final Movie realmMovie = realm.where(Movie.class).equalTo("id", movie.getId()).findFirst();

                if (realmMovie != null) {
                    final boolean isSeen = realmMovie.isSeen();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            fab.setImageResource(R.drawable.ic_delete_black_24dp);
                            checkBoxSeen.setVisibility(View.VISIBLE);
                            checkBoxSeen.setChecked(isSeen);
                        }
                    });
                } else {
                    fab.setImageResource(R.drawable.ic_add_black_24dp);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Original queries and Realm objects are automatically updated.

            }
        });
    }
}