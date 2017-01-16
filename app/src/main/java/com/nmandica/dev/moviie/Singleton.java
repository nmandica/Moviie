package com.nmandica.dev.moviie;

import java.util.ArrayList;

/**
 * Created by nico on 16/01/2017.
 */

class Singleton
{
    private static Singleton INSTANCE = new Singleton();
    private static final String TAG = "com.nmandica.moviie.Singleton";
    private ArrayList<Movie> famous;
    private ArrayList<Movie> topRated;
    private ArrayList<Movie> upComing;

    static Singleton getInstance()
    {
        return INSTANCE;
    }

    ArrayList<Movie> getFamousMovies()
    {
        return this.famous;
    }

    ArrayList<Movie> getTopRated()
    {
        return this.topRated;
    }

    ArrayList<Movie> getUpComing()
    {
        return this.upComing;
    }

    void setFamous(ArrayList<Movie> famous)
    {
        this.famous = famous;
    }

    void setTopRated(ArrayList<Movie> topRated)
    {
        this.topRated = topRated;
    }

    void setUpComing(ArrayList<Movie> upComing)
    {
        this.upComing = upComing;
    }
}
