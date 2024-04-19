package utils;

import android.content.SharedPreferences;

import java.util.Set;

public class SharedPref {

    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;

    public SharedPref(SharedPreferences sharedPref) {

        mSharedPref = sharedPref;
        mEditor = mSharedPref.edit();
    }

    public void save(String key, String value) {
            mEditor.putString(key, value);
            mEditor.commit();
    }

    public void saveBool(String key, boolean value) {
            mEditor.putBoolean(key, value);
            mEditor.commit();
    }

    public void save(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void save(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void save(String key, Set<String> set) {
        mEditor.putStringSet(key, set);
        mEditor.commit();
    }

    public String getString(String key) {

        return mSharedPref.getString(key, "");
    }

    public boolean getBool(String key) {

        return mSharedPref.getBoolean(key,false);
    }


    public int getInt(String key) {
        return mSharedPref.getInt(key, -1);
    }

    public long getLong(String key) {

        return mSharedPref.getLong(key, -1);
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }
}