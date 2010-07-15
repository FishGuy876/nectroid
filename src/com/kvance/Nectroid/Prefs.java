// This file is part of Nectroid.
//
// Nectroid is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Nectroid is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Nectroid.  If not, see <http://www.gnu.org/licenses/>.

package com.kvance.Nectroid;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


class Prefs
{
    // Preference file name
    public static final String PREFS_NAME = "Nectroid";

    // Preference keys
    public static final String PLAYLIST_UPDATE_TIME_KEY = "playlist_update_time";
    public static final String ONELINER_UPDATE_TIME_KEY = "oneliner_update_time";
    public static final String ONELINER_REFRESH_PERIOD_KEY = "oneliner_refresh_period";
    public static final String STREAM_URL_KEY = "stream_url";
    public static final String STREAM_ID_KEY = "stream_id";

    // Timestamp formatter
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    // Defaults
    private static final int ONELINER_REFRESH_PERIOD = 60;

    private static final String TAG = "Nectroid";


    ///
    /// Getter/setter pairs
    ///

    public static Date getPlaylistUpdateTime(Context context)
    {
        return getDate(PLAYLIST_UPDATE_TIME_KEY, context, "playlist update time");
    }

    public static void setPlaylistUpdateTime(Date timeBase, Context context)
    {
        setDate(PLAYLIST_UPDATE_TIME_KEY, timeBase, context);
    }


    public static int getOneLinerRefreshPeriod(Context context)
    {
        Integer retval = getIntFromString(ONELINER_REFRESH_PERIOD_KEY, context,
                "oneliner refresh period");
        if(retval == null) {
            retval = ONELINER_REFRESH_PERIOD;
        }
        Log.d(TAG, String.format("getOneLinerRefreshPeriod (retval=%d)", retval));
        return retval;
    }

    public static void setOneLinerRefreshPeriod(int period, Context context)
    {
        Log.d(TAG, String.format("setOneLinerRefreshPeriod(%d)", period));
        setIntFromString(ONELINER_REFRESH_PERIOD_KEY, period, context);
    }


    public static Date getOneLinerUpdateTime(Context context)
    {
        return getDate(ONELINER_UPDATE_TIME_KEY, context, "oneliner update time");
    }

    public static void setOneLinerUpdateTime(Date timestamp, Context context)
    {
        setDate(ONELINER_UPDATE_TIME_KEY, timestamp, context);
    }


    public static URL getStreamUrl(Context context)
    {
        URL retval = null;
        String urlString = sp(context).getString(STREAM_URL_KEY, null);
        if(urlString != null) {
            try {
                retval = new URL(urlString);
            } catch(MalformedURLException e) {
                Log.w(TAG, String.format("Invalid stream URL \"%s\"", urlString));
            }
        }
        return retval;
    }

    public static Integer getStreamId(Context context)
    {
        Integer retval = null;
        SharedPreferences p = sp(context);
        if(p.contains(STREAM_ID_KEY)) {
            retval = new Integer(p.getInt(STREAM_ID_KEY, -1));
        }
        return retval;
    }


    public static void setStreamUrlAndId(URL url, int id, Context context)
    {
        SharedPreferences.Editor e = esp(context);
        String urlString = url.toString();
        e.putString(STREAM_URL_KEY, urlString);
        e.putInt(STREAM_ID_KEY, id);
        e.commit();
    }


    ///
    /// Utility methods
    ///

    /** Return the shared preferences for this context */
    private static SharedPreferences sp(Context context)
    {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /** Return the shared preferences editor for this context */
    private static SharedPreferences.Editor esp(Context context)
    {
        return sp(context).edit();
    }


    /** Get a date as a string preference. */
    private static Date getDate(String key, Context context, String description)
    {
        Date retval = null;
        String dateString = sp(context).getString(key, null);
        if(dateString != null) {
            try {
                retval = timestampFormat.parse(dateString);
            } catch(ParseException e) {
                Log.w(TAG, String.format("Invalid %s \"%s\"", description, dateString));
            }
        }
        return retval;
    }

    /** Set a date as a string preference. */
    private static void setDate(String key, Date date, Context context)
    {
        String dateString;
        if(date == null) {
            dateString = null;
        } else {
            dateString = timestampFormat.format(date);
        }

        esp(context).putString(key, dateString).commit();
    }


    private static Integer getIntFromString(String key, Context context, String description)
    {
        Integer retval = null;
        String intString = sp(context).getString(key, null);
        if(intString != null) {
            try {
                retval = Integer.parseInt(intString);
            } catch(NumberFormatException e) {
                Log.w(TAG, String.format("Invalid %s \"%s\"", description, intString));
            }
        }
        return retval;
    }

    private static void setIntFromString(String key, int value, Context context)
    {
        String intString = String.valueOf(value);
        esp(context).putString(key, intString).commit();
    }
}
