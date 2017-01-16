package com.nmandica.dev.moviie;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nico on 16/01/2017.
 */

public class RecentMoviesFragment extends Fragment implements Callback
{
    private static final int ERROR = 0;
    private static final String EXTRA_MOVIES_TYPE = "extra_movies_type";
    public static final int MOVIES_FAMOUS = 0;
    public static final int MOVIES_TOP_RATED = 1;
    public static final int MOVIES_UPCOMING = 2;
    private static final int SUCCESS = 1;
    private static final String TAG = "com.ensiie.moviie.RecentMoviesActivity";
    private Button button;
    private ArrayList<Movie> movies;
    private int moviesType;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case ERROR:
                    showError();
                    break;
                case SUCCESS:
                    movies = (ArrayList)msg.obj;
                    fillList();
                    break;
            }

            if (moviesType == 0) {
                singleton.setFamous(movies);
            }
            else if (moviesType == 1) {
                singleton.setTopRated(movies);
            }
            else {
                singleton.setUpComing(movies);
            }
        }
    };

    MoviesAdapter.MovieClickListener movieClickListener = new MoviesAdapter.MovieClickListener()
    {
        @Override
        public void onMovieClickListener(Movie paramAnonymousMovie)
        {
            startActivity(MovieActivity.newIntent(getContext(), paramAnonymousMovie));
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View paramAnonymousView)
        {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            callWebservice();
        }
    };

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Singleton singleton = Singleton.getInstance();

    private void callWebservice()
    {
        String url;

        if (this.moviesType == 0) {
            url = "https://api.themoviedb.org/3/discover/movie?api_key=3c0f130197ce35297d7e7465ceb36da7&language=fr-FR&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_year=2016";
        }
        else if (this.moviesType == 1) {
            url = "https://api.themoviedb.org/3/movie/top_rated?api_key=3c0f130197ce35297d7e7465ceb36da7&language=fr-FR&page=1";
        }
        else {
            url = "https://api.themoviedb.org/3/movie/upcoming?api_key=3c0f130197ce35297d7e7465ceb36da7&language=fr-FR&page=1&region=FR";
        }

        Request request = new Request.Builder().url(url).build();
        Webservice.getOkHttpClient().newCall(request).enqueue(this);
    }

    private void fillList()
    {
        this.button.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.GONE);

        final MoviesAdapter moviesAdapter = new MoviesAdapter(this.movies);
        moviesAdapter.setListener(this.movieClickListener);

        this.recyclerView.setAdapter(moviesAdapter);
    }

    public static RecentMoviesFragment newInstance(int tab)
    {
        final Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MOVIES_TYPE, tab);

        final RecentMoviesFragment fragment = new RecentMoviesFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private ArrayList<Movie> parserResponse(Response paramResponse) throws IOException, JSONException
    {
        ArrayList localArrayList = new ArrayList();

        JSONArray json = new JSONObject(paramResponse.body().string()).getJSONArray("results");

        int i = 0;
        while (i < json.length())
        {
            JSONObject localJSONObject = json.getJSONObject(i);
            int j = localJSONObject.getInt("id");
            String str1 = localJSONObject.getString("title");
            String str2 = localJSONObject.getString("poster_path");
            String str3 = localJSONObject.getString("backdrop_path");
            String str4 = localJSONObject.getString("release_date");
            localArrayList.add(new Movie(j, str1, localJSONObject.getString("overview"), localJSONObject.getDouble("vote_average") + "", str4, str2, str3));
            i += 1;
        }

        return localArrayList;
    }

    private void showError()
    {
        this.button.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.moviesType = getArguments().getInt(EXTRA_MOVIES_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.recent_movies_fragment, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        this.recyclerView = ((RecyclerView)view.findViewById(R.id.recycler_view));
        this.recyclerView.setLayoutManager(gridLayoutManager);

        this.button = ((Button)view.findViewById(R.id.button));
        this.button.setOnClickListener(this.onClickListener);

        this.progressBar = ((ProgressBar)view.findViewById(R.id.progress_bar));

        if ((savedInstanceState == null) || (savedInstanceState.getSerializable("movies") == null))
        {
            while (this.movies != null)
            {
                if ((this.moviesType == 0) && (this.singleton.getFamousMovies() != null)) {
                    this.movies = this.singleton.getFamousMovies();
                }
                else if ((this.moviesType == 1) && (this.singleton.getTopRated() != null)) {
                    this.movies = this.singleton.getTopRated();
                }
                else if ((this.moviesType == 2) && (this.singleton.getUpComing() != null)) {
                    this.movies = this.singleton.getUpComing();
                }

                fillList();
            }

            callWebservice();
        }
        else
        {
            this.movies = ((ArrayList)savedInstanceState.getSerializable("movies"));
            fillList();
        }

        return view;
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        this.handler.sendEmptyMessage(0);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (!response.isSuccessful()) {
            this.handler.sendEmptyMessage(0);
        }

        try
        {
            ArrayList<Movie> moviesList = parserResponse(response);
            Message message = Message.obtain();
            message.what = 1;
            message.obj = moviesList;
            this.handler.sendMessage(message);
        }
        catch (JSONException exception)
        {
            Log.e("RecentMoviesActivity", exception.getMessage());
            this.handler.sendEmptyMessage(0);
        }
    }

    public void onSaveInstanceState(Bundle paramBundle)
    {
        super.onSaveInstanceState(paramBundle);
        if (this.movies != null) {
            paramBundle.putSerializable("movies", this.movies);
        }
    }
}

