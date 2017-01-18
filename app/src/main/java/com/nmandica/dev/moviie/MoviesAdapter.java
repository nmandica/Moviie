package com.nmandica.dev.moviie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nico on 16/01/2017.
 */

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder>
{
    private static final String ROOT_URL_IMAGE = "https://image.tmdb.org/t/p/w300/";
    private MovieClickListener listener;
    private ArrayList<Movie> movies;

    MoviesAdapter(ArrayList<Movie> movies)
    {
        this.movies = movies;
    }

    @Override
    public int getItemCount()
    {
        return this.movies.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final Movie movie = this.movies.get(position);
        Picasso.with(holder.poster.getContext()).load(ROOT_URL_IMAGE + movie.getPosterPath()).into(holder.poster);

        if (movie.isSeen())
        {
            holder.seen.setVisibility(View.VISIBLE);
            holder.holder.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.seen.setVisibility(View.GONE);
            holder.holder.setVisibility(View.GONE);
        }
    }

    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MyViewHolder(view);
    }

    void setListener(MovieClickListener listener)
    {
        this.listener = listener;
    }

    interface MovieClickListener
    {
        void onMovieClickListener(Movie movie);
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ViewGroup holder;
        ImageView poster;
        ImageView seen;

        MyViewHolder(final View view)
        {
            super(view);
            this.poster = ((ImageView)view.findViewById(R.id.poster));
            this.seen = ((ImageView)view.findViewById(R.id.seen));
            this.holder = ((ViewGroup)view.findViewById(R.id.subtitle_holder));
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View paramAnonymousView)
                {
                    listener.onMovieClickListener(movies.get(getAdapterPosition()));
                }
            });
        }
    }
}
