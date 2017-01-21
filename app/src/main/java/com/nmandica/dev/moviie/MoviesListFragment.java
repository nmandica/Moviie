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
    private static final String EXTRA_MOVIES_TAB = "extra_movies_tab";
    public static final int MOVIES_FAMOUS = 0;
    public static final int MOVIES_TOP_RATED = 1;
    public static final int MOVIES_UPCOMING = 2;
    private static final int SUCCESS = 1;
    private static final String TAG = "MoviesListActivity";
    private Button button;
    GridLayoutManager gridLayoutManager;
    private ArrayList<Movie> movies = new ArrayList<>();
    private int moviesTab;
    private int pageNumber = 1;

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
                    movies.addAll((ArrayList)msg.obj);
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
            callWebservice(pageNumber);
        }
    };

//    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//
//            if(dy > 0) //check for scroll down
//            {
//                int visibleItemCount = gridLayoutManager.getChildCount();
//                int totalItemCount = gridLayoutManager.getItemCount();
//                int pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();
//
//                if (isLoading)
//                {
//                    if ( (visibleItemCount + pastVisibleItems) >= totalItemCount)
//                    {
//                        isLoading = false;
//                        //Do pagination.. i.e. fetch new data
//                        Log.v("...", "Last Item Wow ! page = " + pageNumber);
//                        pageNumber++;
//                        callWebservice(pageNumber);
//                    }
//                }
//            }
//
//            // init
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            RecyclerView.Adapter adapter = recyclerView.getAdapter();
//
//            if (layoutManager.getChildCount() > 0) {
//                // Calculate
//                int indexOfLastItemViewVisible = layoutManager.getChildCount() -1;
//                View lastItemViewVisible = layoutManager.getChildAt(indexOfLastItemViewVisible);
//                int adapterPosition = layoutManager.getPosition(lastItemViewVisible);
//                boolean isLastItemVisible = (adapterPosition == adapter.getItemCount() -1);
//
//                // check
//                if (isLastItemVisible && isLoading)
//                    onLastItemVisible(); // callback
//            }
//        }
//    };

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Singleton singleton = Singleton.getInstance();

    private void callWebservice(int pageNumber)
    {
        String url;

        if (this.moviesTab == MOVIES_FAMOUS) {
            url = "https://api.themoviedb.org/3/discover/movie?api_key=3c0f130197ce35297d7e7465ceb36da7&language=fr-FR&sort_by=popularity.desc&include_adult=false&include_video=false&primary_release_year=2016&page=" + pageNumber;
        }
        else if (this.moviesTab == MOVIES_TOP_RATED) {
            url = "https://api.themoviedb.org/3/movie/top_rated?api_key=3c0f130197ce35297d7e7465ceb36da7&language=fr-FR&page=" + pageNumber;
        }
        else {
            url = "https://api.themoviedb.org/3/movie/upcoming?api_key=3c0f130197ce35297d7e7465ceb36da7&language=fr-FR&region=FR&page=" + pageNumber;
        }

        Request request = new Request.Builder().url(url).build();
        Webservice.getOkHttpClient().newCall(request).enqueue(this);
    }

    private void fillList()
    {
        this.button.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.GONE);

        if (this.recyclerView.getAdapter() == null)
        {
            final MoviesAdapter moviesAdapter = new MoviesAdapter(this.movies);
            moviesAdapter.setListener(this.movieClickListener);
            this.recyclerView.setAdapter(moviesAdapter);
        }
        else
        {
            this.recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public static MoviesListFragment newInstance(int tab)
    {
        final Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MOVIES_TAB, tab);

        final MoviesListFragment fragment = new MoviesListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private ArrayList<Movie> parserResponse(Response paramResponse) throws IOException, JSONException
    {
        ArrayList moviesList = new ArrayList();

        JSONArray json = new JSONObject(paramResponse.body().string()).getJSONArray("results");

        for (int i = 0; i < json.length(); i++)
        {
            JSONObject localJSONObject = json.getJSONObject(i);
            int id = localJSONObject.getInt("id");
            String title = localJSONObject.getString("title");
            String posterPath = localJSONObject.getString("poster_path");
            String backdropPath = localJSONObject.getString("backdrop_path");
            String releaseDate = localJSONObject.getString("release_date");
            moviesList.add(new Movie(id, title, localJSONObject.getString("overview"), localJSONObject.getDouble("vote_average") + "", releaseDate, posterPath, backdropPath));
        }

        return moviesList;
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
        this.moviesTab = getArguments().getInt(EXTRA_MOVIES_TAB);

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

        gridLayoutManager = new GridLayoutManager(getContext(), 2);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNumber = page + 1;
                Log.v("...", "Last Item Wow ! page = " + pageNumber);
                callWebservice(pageNumber);
            }
        };

        this.recyclerView = ((RecyclerView)view.findViewById(R.id.recycler_view));
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.addOnScrollListener(scrollListener);

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

        callWebservice(pageNumber);

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

