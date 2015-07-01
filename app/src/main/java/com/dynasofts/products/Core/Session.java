package com.dynasofts.products.Core;

import android.content.Context;

import com.dynasofts.products.Util.Preferences;

/**
 * Created by Keshav K on 6/30/2015.
 */
public class Session {

    private static final String SESSION_FILE = "session";
    private static final String ID = "user_id";
    private static final String NAME = "user_name";
    private static final String EMAIL = "user_email";
    private Context mContext;

    public Session(Context mContext) {
        this.mContext = mContext;
    }

    public Boolean is_logged() {
        String id = getUserId();
        if (id.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public String getUserId()
    {
        String id = Preferences.readFromPreferences(mContext, SESSION_FILE, ID, "");
        return id;
    }

    public void setSession(String id, String name, String email) {
        Preferences.saveToPreferences(mContext, SESSION_FILE, ID, id);
        Preferences.saveToPreferences(mContext, SESSION_FILE, NAME, name);
        Preferences.saveToPreferences(mContext, SESSION_FILE, EMAIL, email);
    }
}
