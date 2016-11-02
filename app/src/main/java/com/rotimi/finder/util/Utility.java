package com.rotimi.finder.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;
import com.marcohc.toasteroid.Toasteroid;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
/**
 * Created by mayowa on 2/16/16.
 */
public class Utility {

    private Context context;
    private static final String LOG = Utility.class.getName();

    public SystemData getSharedPrefs() {
        return sharedPrefs;
    }

    private SystemData sharedPrefs;

    public Utility(Context ctx) {
        context = ctx;
        sharedPrefs
                = new SystemData(ctx);
    }

    /**
     * Check if Current User is logged in
     *
     * @return Boolean
     */
    public boolean isRegistered() {
        SystemData systemData = new SystemData(context);
        return  systemData.getBoolean(Constants.USER, false);
    }

    public void registerUser(String name, String phone){
        SystemData systemData = new SystemData(context);
        systemData.writeString(name, Constants.NAME);
        systemData.writeString(phone, Constants.PHONE);
        systemData.writeBoolean(true, Constants.USER);
    }

    /**
     * Check if user is onBoarded
     * @return Boolean
     */
    public Boolean isUserOnboarded() {
//        User user = SQLite.select().from(User.class).where().querySingle();
//        return (user != null);
        return false;
    }

    public int getStatusBarHeight() {

        int result = 0;

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int getHeight() {
        return (context.getResources().getDisplayMetrics().heightPixels - getStatusBarHeight() - getActionBarHeight() - getBottomBarHeight());
    }

    public int getActionBarHeight() {
        return dpToPx(56); // ?attr/actionBarSize == 56dp
    }

    public int getBottomBarHeight() {
        return dpToPx(72); //standard TabLayout height
    }

    public int getWidth() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenFractionalSizeInPx(int size, double fraction) {
        return (int) (size * fraction);
    }

    public static String getCurrentDate(boolean onlyDate) {

        String format = "yyyy-MM-dd HH:mm:ss";

        if (onlyDate)
            format = "yyyy-MM-dd";

        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }

    public static Date parseDate(String date) {

        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            Log.d(LOG, e.getMessage());
            return null;
        }
    }

    public static Date parseDate(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public InputStream readFile(String filename) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(filename);

            return inputStream;
        } catch (IOException e) {
            //log the exception
        } finally {

        }
        return inputStream;
    }

    public Drawable createDrawable(InputStream inputStream) {

        if (inputStream != null)
            return Drawable.createFromStream(inputStream, null);
        else
            try {
                return Drawable.createFromStream(context.getAssets().open("default.png"), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    public int getResourceId(String filename) {
        filename = filename.substring(0, filename.lastIndexOf("."));
        return context.getResources().getIdentifier(filename, "raw", context.getPackageName());
    }

    /**
     * Util class to round double value
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Double getPercentage(double val, int total) {
        return round((val * 100) / total, 2);
    }

    public boolean isConnected() {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } else {
            return false;
        }
    }

    public int getScreenOrientation() {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int orientation;
        Point point = new Point();
        display.getSize(point);
        if (point.x < point.y) {
            orientation = Configuration.ORIENTATION_PORTRAIT;
        } else {
            orientation = Configuration.ORIENTATION_LANDSCAPE;
        }
        return orientation;
    }


    public boolean onPortrait() {
        return Configuration.ORIENTATION_PORTRAIT == getScreenOrientation();
    }


}
