package com.nmandica.dev.moviie;

import io.realm.RealmObject;

/**
 * Created by nico on 18/01/2017.
 */

/**
 * Realm string to be used in RealmList
 */
public class RealmString extends RealmObject {
    private String val;

    public String getValue() {
        return val;
    }

    public void setValue(String value) {
        this.val = value;
    }
}