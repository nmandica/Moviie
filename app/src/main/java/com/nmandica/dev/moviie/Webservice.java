package com.nmandica.dev.moviie;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by nico on 16/01/2017.
 */

public class Webservice {
    private static final String TAG = "Webservice";
    private static final OkHttpClient client;

    static {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        // Ajout de log
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.v(TAG, message);
            }
        });

        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(logger);

        client = clientBuilder.build();
    }

    public static OkHttpClient getOkHttpClient() {
        return client;
    }
}
