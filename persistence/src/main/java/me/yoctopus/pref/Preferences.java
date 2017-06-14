/*
 * Copyright 2017, Solutech RMS
 * Licensed under the Apache License, Version 2.0, "Solutech Limited".
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    private SharedPreferences preferences;

    public Preferences(Context context) {
        this(context, null);
    }

    public Preferences(Context context,
                       final PreferenceChange preferenceChange) {
        preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        preferences.registerOnSharedPreferenceChangeListener(
                new SharedPreferences
                        .OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(
                            SharedPreferences sharedPreferences, String key) {
                        if (preferenceChange != null) {
                            preferenceChange.onChange(sharedPreferences, key);
                        }
                    }
                });
    }

    public void savePreference(Pref pref) throws InvalidPref {
        SharedPreferences.Editor editor = preferences.edit();
        if (pref.get() instanceof String) {
            editor.putString(pref.getName(),
                    (String) pref.get());
        } else if (pref.get() instanceof Integer) {
            editor.putInt(pref.getName(),
                    (Integer) pref.get());
        } else if (pref.get() instanceof Boolean) {
            editor.putBoolean(pref.getName(),
                    (Boolean) pref.get());
        } else if (pref.get() instanceof Long) {
            editor.putLong(pref.getName(),
                    (Long) pref.get());
        } else if (pref.get() instanceof Float) {
            editor.putFloat(pref.getName(),
                    (Float) pref.get());
        } else {
            throw new InvalidPref(pref);
        }
        editor.apply();
    }

    public void clearAll() {
        preferences.edit().clear().apply();
    }

    public <T> T getPreference(Pref<T> pref) throws InvalidPrefType {
        Object data;
        if (pref.get() instanceof String) {
            data = preferences.getString(pref.getName(),
                    (String) pref.get());
        } else if (pref.get() instanceof Integer) {
            data = preferences.getInt(pref.getName(),
                    (Integer) pref.get());
        } else if (pref.get() instanceof Boolean) {
            data = preferences.getBoolean(pref.getName(),
                    (Boolean) pref.get());
        } else if (pref.get() instanceof Long) {
            data = preferences.getLong(pref.getName(),
                    (Long) pref.get());
        } else if (pref.get() instanceof Float) {
            data = preferences.getFloat(pref.getName(),
                    (Float) pref.get());
        } else {
            throw new InvalidPrefType(pref.get());
        }
        return (T) data;
    }

    public interface PreferenceChange {
        void onChange(SharedPreferences preferences, String key);
    }
}
