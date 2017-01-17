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

public class MoviesListFragment extends Fragment implements Callback
{
    private static final int ERROR = 0;
    private static final String EXTRA_MOVIES_TYPE = "extra_movies_type";
    public static final int MOVIES_FAMOUS = 0;
    public static final int MOVIES_TOP_RATED = 1;
    public static final int MOVIES_UPCOMING = 2;
    private static final int SUCCESS = 1;
    private static final String TAG = "MoviesListActivity";
    private Button button;
    private ArrayList<Movie> movies;
    private int moviesTab;

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
        }
    };

    MoviesAdapter.MovieClickListener movieClickListener = new MoviesAdapter.MovieClickListener()
    {
        @Override
        public void onMovieClickListener(Movie movie)
        {
            startActivity(MovieActivity.newIntent(getContext(), movie));
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

        if (this.moviesTab == MOVIES_FAMOUS) {
            url = "https://api.themoviedb.org/3/discover/movie?api_key=3c0f130197ce35297d7e7465ceb36da7&language=fr-FR&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_year=2016";
        }
        else if (this.moviesTab == MOVIES_TOP_RATED) {
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

    public static MoviesListFragment newInstance(int tab)
    {
        final Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MOVIES_TYPE, tab);

        final MoviesListFragment fragment = new MoviesListFragment();
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
        this.moviesTab = getArguments().getInt(EXTRA_MOVIES_TYPE);

        if (moviesTab == MOVIES_FAMOUS) {
            singleton.setFamous(movies);
        }
        else if (moviesTab == MOVIES_TOP_RATED) {
            singleton.setTopRated(movies);
        }
        else if (moviesTab == MOVIES_UPCOMING) {
            singleton.setUpComing(movies);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.movies_list_fragment, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        this.recyclerView = ((RecyclerView)view.findViewById(R.id.recycler_view));
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(gridLayoutManager);

        this.button = ((Button)view.findViewById(R.id.button));
        this.button.setOnClickListener(this.onClickListener);

        this.progressBar = ((ProgressBar)view.findViewById(R.id.progress_bar));

        if ((savedInstanceState == null) || (savedInstanceState.getSerializable("movies") == null))
        {
            if ((this.moviesTab == MOVIES_FAMOUS) && (this.singleton.getFamousMovies() != null)) {
                this.movies = this.singleton.getFamousMovies();
            }
            else if ((this.moviesTab == MOVIES_TOP_RATED) && (this.singleton.getTopRated() != null)) {
                this.movies = this.singleton.getTopRated();
            }
            else if ((this.moviesTab == MOVIES_UPCOMING) && (this.singleton.getUpComing() != null)) {
                this.movies = this.singleton.getUpComing();
            }
        }
        else
        {
            this.movies = ((ArrayList)savedInstanceState.getSerializable("movies"));
        }

        callWebservice();

        return view;
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        this.handler.sendEmptyMessage(ERROR);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (!response.isSuccessful()) {
            this.handler.sendEmptyMessage(ERROR);
        }

        try
        {
            ArrayList<Movie> movies = parserResponse(response);
            Message message = Message.obtain();
            message.what = SUCCESS;
            message.obj = movies;
            this.handler.sendMessage(message);
        }
        catch (JSONException exception)
        {
            Log.e(TAG, exception.getMessage());
            this.handler.sendEmptyMessage(ERROR);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (this.movies != null) {
            outState.putSerializable("movies", this.movies);
        }
    }
}
