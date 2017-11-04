package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "WaterRate";
    private static final String ID_USER = "USER_ID";
    private static final String SYARAT_USER = "USER_SYARAT";
    private static final String EMAIL_USER = "USER_EMAIL";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = preferences.edit();
    }

    public void setEmail(String email) {
        editor.putString(EMAIL_USER, email);
        editor.commit();
    }

    public void setUserID(String id) {
        editor.putString(ID_USER, id);
        editor.commit();
    }

    public void setUserSyarat(int id) {
        editor.putInt(SYARAT_USER, id);
        editor.commit();
    }

    public String getUserID() {
        return preferences.getString(ID_USER, "");
    }

    public int getSyarat() {
        return preferences.getInt(SYARAT_USER, 0);
    }

    public String getEmailUser() { return preferences.getString(EMAIL_USER, ""); }

    public boolean isSyaratExist() {
        return preferences.getInt(SYARAT_USER, 0) != 0;
    }

    public void clearUserID() {
        editor.remove(ID_USER);
        editor.commit();
    }

    public void clearUserSyarat() {
        editor.remove(SYARAT_USER);
        editor.commit();
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }
}