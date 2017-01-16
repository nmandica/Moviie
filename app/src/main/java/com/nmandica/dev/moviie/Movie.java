package com.nmandica.dev.moviie;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nico on 16/01/2017.
 */

public class Movie implements Serializable
{
    private static final String TAG = "com.nmandica.moviie.Movie";
    private String backdropPath;
    private ArrayList<String> genres;
    private String homepage;
    private int id;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private int runtime;
    private boolean seen;
    private String title;
    private String voteAverage;

    public Movie(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
    {
        this.id = paramInt;
        this.title = paramString1;
        this.overview = paramString2;
        this.voteAverage = paramString3;
        this.releaseDate = paramString4;
        this.posterPath = paramString5;
        this.backdropPath = paramString6;
    }

    public Movie(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, ArrayList<String> paramArrayList, String paramString7, int paramInt2)
    {
        this.id = paramInt1;
        this.title = paramString1;
        this.overview = paramString2;
        this.voteAverage = paramString3;
        this.releaseDate = paramString4;
        this.posterPath = paramString5;
        this.backdropPath = paramString6;
        this.genres = paramArrayList;
        this.homepage = paramString7;
        this.runtime = paramInt2;
    }

    public Movie(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, boolean paramBoolean)
    {
        this.id = paramInt;
        this.title = paramString1;
        this.overview = paramString2;
        this.voteAverage = paramString3;
        this.releaseDate = paramString4;
        this.posterPath = paramString5;
        this.backdropPath = paramString6;
        this.seen = paramBoolean;
    }

    public String getBackdropPath()
    {
        return this.backdropPath;
    }

    public int getBudget()
    {
        return 2131165223;
    }

    public ArrayList<String> getGenres()
    {
        return this.genres;
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

    public void setBackdropPath(String paramString)
    {
        this.backdropPath = paramString;
    }

    public void setGenres(ArrayList<String> paramArrayList)
    {
        this.genres = paramArrayList;
    }

    public void setHomepage(String paramString)
    {
        this.homepage = paramString;
    }

    public void setId(int paramInt)
    {
        this.id = paramInt;
    }

    public void setOverview(String paramString)
    {
        this.overview = paramString;
    }

    public void setPosterPath(String paramString)
    {
        this.posterPath = paramString;
    }

    public void setReleaseDate(String paramString)
    {
        this.releaseDate = paramString;
    }

    public void setRuntime(int paramInt)
    {
        this.runtime = paramInt;
    }

    public void setSeen(boolean paramBoolean)
    {
        this.seen = paramBoolean;
    }

    public void setTitle(String paramString)
    {
        this.title = paramString;
    }

    public void setVoteAverage(String paramString)
    {
        this.voteAverage = paramString;
    }
}
