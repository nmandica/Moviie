package com.nmandica.dev.moviie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nico on 16/01/2017.
 */

public class MovieActivity extends AppCompatActivity
{
    private static final String EXTRA_MOVIE = "extra_movie";
    private static final String ROOT_URL_IMAGE = "https://image.tmdb.org/t/p/w960_and_h540_bestv2";
    private static final String TAG = "com.ensiie.moviie.RecentMoviesActivity";

    View.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        public void onClick(View paramAnonymousView)
        {
            if (checkBoxSeen.getVisibility() == View.VISIBLE)
            {
                //new MovieActivity.DeleteMovieToSeeTask(MovieActivity.this).execute(new Movie[] { movie });
            }
            else
            {
                //new MovieActivity.AddMovieToSeeTask(MovieActivity.this).execute(new Movie[] { movie });
            }
        }
    };

    CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener()
    {
        public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
        {
            movie.setSeen(paramAnonymousBoolean);

            //new MovieActivity.UpdateMovieTask(MovieActivity.this).execute(new Movie[] { movie });
        }
    };

    private CheckBox checkBoxSeen;
    private CollapsingToolbarLayout collapsingToolbar;
    //private DatabaseHelper databaseHelper;
    private TextView date;
    private FloatingActionButton fab;
    private ImageView image;
    private Movie movie;
    private TextView overview;
    private ProgressBar progressBar;
    private TextView rating;

    private void fillSimpleView()
    {
        Picasso.with(this).load("https://image.tmdb.org/t/p/w960_and_h540_bestv2" + this.movie.getBackdropPath()).into(this.image);
        this.collapsingToolbar.setTitle(this.movie.getTitle());
        this.rating.setText(this.movie.getVoteAverage());
        this.overview.setText(this.movie.getOverview());
        Object localObject = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        try
        {
            localObject = ((SimpleDateFormat)localObject).parse(this.movie.getReleaseDate());
            this.date.setText(localSimpleDateFormat.format((Date)localObject));
            return;
        }
        catch (ParseException localParseException)
        {
            localParseException.printStackTrace();
        }
    }

    public static Intent newIntent(Context context, Movie movie)
    {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra("extra_movie", movie);
        return intent;
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_movie);

        //this.databaseHelper = new DatabaseHelper(this);
        this.rating = ((TextView)findViewById(R.id.rating));
        this.overview = ((TextView)findViewById(R.id.synopsis));
        this.date = ((TextView)findViewById(R.id.date));
        this.image = ((ImageView)findViewById(R.id.image));
        this.fab = ((FloatingActionButton)findViewById(R.id.actionButton));
        this.checkBoxSeen = ((CheckBox)findViewById(R.id.seenCheckBox));
        this.progressBar = ((ProgressBar)findViewById(R.id.progress_bar));
        this.collapsingToolbar = ((CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar));
        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        this.fab.setOnClickListener(this.buttonClickListener);
        this.checkBoxSeen.setOnCheckedChangeListener(this.checkBoxListener);
        this.movie = ((Movie)getIntent().getSerializableExtra("extra_movie"));
        fillSimpleView();

        //new GetMovieTask().execute(new Integer[] { Integer.valueOf(this.movie.getId()) });
    }

    public boolean onCreateOptionsMenu(Menu paramMenu)
    {
        //getMenuInflater().inflate(movie_menu, paramMenu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem)
    {
//        String str;
//        if (paramMenuItem.getItemId() == 2131558574) {
//            if (this.checkBoxSeen.isChecked())
//            {
//                str = String.format(getString(2131165243), new Object[] { this.movie.getTitle() });
//                Intent localIntent = new Intent("android.intent.action.SEND");
//                localIntent.putExtra("android.intent.extra.TEXT", str);
//                localIntent.setType("text/plain");
//                startActivity(localIntent);
//            }
//        }
//        for (;;)
//        {
//            return super.onOptionsItemSelected(paramMenuItem);
//            str = String.format(getString(2131165242), new Object[] { this.movie.getTitle() });
//            break;
//            if (paramMenuItem.getItemId() == 2131558573) {
//                startActivity(new Intent(this, AboutActivity.class));
//            }
//        }
        return false;
    }

    protected void onSaveInstanceState(Bundle paramBundle)
    {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putSerializable("movie", this.movie);
    }

//    public class AddMovieToSeeTask extends AsyncTask<Movie, Void, Long>
//    {
//        public AddMovieToSeeTask() {}
//
//        protected Long doInBackground(Movie... paramVarArgs)
//        {
//            return Long.valueOf(databaseHelper.addMovie(paramVarArgs[0]));
//        }
//
//        protected void onPostExecute(Long paramLong)
//        {
//            if (paramLong.longValue() != -1L)
//            {
//                fab.setImageResource(2130837596);
//                checkBoxSeen.setVisibility(0);
//                return;
//            }
//            fab.setImageResource(2130837595);
//        }
//    }

//    public class DeleteMovieToSeeTask extends AsyncTask<Movie, Void, Integer>
//    {
//        public DeleteMovieToSeeTask() {}
//
//        protected Integer doInBackground(Movie... paramVarArgs)
//        {
//            return Integer.valueOf(databaseHelper.deleteMovie(paramVarArgs[0]));
//        }
//
//        protected void onPostExecute(Integer paramInteger)
//        {
//            if (paramInteger.intValue() == 0)
//            {
//                fab.setImageResource(2130837596);
//                checkBoxSeen.setVisibility(0);
//                return;
//            }
//            fab.setImageResource(2130837595);
//            checkBoxSeen.setVisibility(4);
//        }
//    }

//    public class GetMovieTask extends AsyncTask<Integer, Void, Movie>
//    {
//        public GetMovieTask() {}
//
//        protected Movie doInBackground(Integer... paramVarArgs)
//        {
//            return databaseHelper.getMovie(paramVarArgs[0].intValue());
//        }
//
//        protected void onPostExecute(Movie paramMovie)
//        {
//            if (paramMovie != null)
//            {
//                fab.setImageResource(2130837596);
//                checkBoxSeen.setVisibility(0);
//                checkBoxSeen.setChecked(paramMovie.isSeen());
//                return;
//            }
//            fab.setImageResource(2130837595);
//        }
//    }
//
//    public class UpdateMovieTask extends AsyncTask<Movie, Void, Integer>
//    {
//        public UpdateMovieTask() {}
//
//        protected Integer doInBackground(Movie... paramVarArgs)
//        {
//            return Integer.valueOf(MovieActivity.this.databaseHelper.updateMovie(paramVarArgs[0]));
//        }
//
//        protected void onPostExecute(Integer paramInteger) {}
//    }
}