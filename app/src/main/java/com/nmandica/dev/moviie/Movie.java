package com.nmandica.dev.moviie;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nico on 16/01/2017.
 */

public class Movie extends RealmObject implements Serializable
{
    @PrimaryKey
    private int id;
    private String title;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private String posterPath;
    private String backdropPath;
    //private RealmList<RealmString> genres;
    private String homepage;
    private int runtime;
    private boolean seen;

    public Movie()
    {

    }

    public Movie(int id, String title, String overview, String voteAverage, String releaseDate, String posterPath, String backdropPath)
    {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    public Movie(int id, String title, String overview, String voteAverage, String releaseDate, String posterPath, String backdropPath, String homePage, int runTime)
    {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.homepage = homePage;
        this.runtime = runTime;
    }

    public Movie(int id, String title, String overview, String voteAverage, String releaseDate, String posterPath, String backdropPath, boolean seen)
    {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.seen = seen;
    }

    public String getBackdropPath()
    {
        return this.backdropPath;
    }

    public String getHomepage()
    {
        return this.homepage;
    }

    public int getId()
    {
        return this.id;
    }

    public String getOverview()
    {
        return this.overview;
    }

    public String getPosterPath()
    {
        return this.posterPath;
    }

    public String getReleaseDate()
    {
        return this.releaseDate;
    }

    public int getRuntime()
    {
        return this.runtime;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getVoteAverage()
    {
        return this.voteAverage;
    }

    public boolean isSeen()
    {
        return this.seen;
    }

    public void setBackdropPath(String backdropPath)
    {
        this.backdropPath = backdropPath;
    }

    public void setHomepage(String homepage)
    {
        this.homepage = homepage;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath)
    {
        this.posterPath = posterPath;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public void setRuntime(int runtime)
    {
        this.runtime = runtime;
    }

    public void setSeen(boolean seen)
    {
        this.seen = seen;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setVoteAverage(String voteAverage)
    {
        this.voteAverage = voteAverage;
    }
}
