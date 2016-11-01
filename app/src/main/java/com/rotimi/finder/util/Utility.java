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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.marcohc.toasteroid.Toasteroid;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softcom.npdn.NPDN;
import com.softcom.npdn.R;
import com.softcom.npdn.api.API;
import com.softcom.npdn.api.RequestInterceptor;
import com.softcom.npdn.api.models._Status;
import com.softcom.npdn.db.models.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public Boolean isUserLoggedIn() {
        User user = SQLite.select().from(User.class).where().querySingle();
        return (user != null && user.isActive == 1);
    }

    /**
     * Check if user is onBoarded
     * @return Boolean
     */
    public Boolean isUserOnboarded() {
        User user = SQLite.select().from(User.class).where().querySingle();
        return (user != null);
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

    public boolean isContentAvailable() {

        Log.d(LOG, Environment.getDataDirectory().getPath());
        File directory = new File(Environment.getDataDirectory().getPath() + Constants.DATA_DIRECTORY);
        File[] contents = directory.listFiles();

        if (contents == null) return false;
        else if (contents.length == 0) return false;
        else return true;
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

    public void shareOnFacebook(String message) {
        Bundle params = new Bundle();
        params.putString("message", message);

            /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                response -> {
                    //send a broadcast
                    Toasteroid.show((Activity)context, response.getRawResponse(), Toasteroid.STYLES.INFO);
                }
        ).executeAsync();
    }

    public void connectToFacebook(Activity activity, CallbackManager callbackManager){
        SystemData systemData = new SystemData(context);
        LoginManager.getInstance().logInWithPublishPermissions(activity, Arrays.asList("publish_actions"));

        //Request user permission to publish post
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                systemData.writeBoolean(true, Constants.FACEBOOK_CONNECTED);

                //TODO: API to update social status.
                Toasteroid.show((Activity)context, R.string.access_granted, Toasteroid.STYLES.ERROR);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                Toasteroid.show((Activity)context, exception.getMessage(), Toasteroid.STYLES.ERROR);
            }
        });
    }
    public void connectToFacebook(Fragment fragment, CallbackManager callbackManager){
        SystemData systemData = new SystemData(context);

        LoginManager.getInstance().logInWithPublishPermissions(fragment, Arrays.asList("publish_actions"));

        //Request user permission to publish post
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                systemData.writeBoolean(true, Constants.FACEBOOK_CONNECTED);
                if(NPDN.user!=null) { //at this point user may or may not have logged in
                    NPDN.user.facebookDefault = 1;
                    NPDN.user.update();
                }
                fragment.onResume();
                Toast.makeText(context, R.string.access_granted, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                Toast.makeText(context, R.string.try_again, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateSocialConnectStatus(HashMap<String, String> userDetails){
        RequestInterceptor requestInterceptor = new RequestInterceptor(context);
        API api = requestInterceptor.retrofit.create(API.class);
        Call<_Status> call = api.updateUser(NPDN.user.id, userDetails);

        call.enqueue(new Callback<_Status>() {
            @Override
            public void onResponse(Call<_Status> call, Response<_Status> response) {
                if (response.isSuccessful()){
                    Toasteroid.show((Activity) context, response.message(), Toasteroid.STYLES.SUCCESS);
                    if(NPDN.user!=null){
                        if(userDetails.containsKey(Constants.FACEBOOK_DEFAULT))
                            NPDN.user.facebookDefault = (userDetails.get(Constants.FACEBOOK_DEFAULT).equalsIgnoreCase("true"))? 1:0;
                        if(userDetails.containsKey(Constants.TWITTER_DEFAULT))
                            NPDN.user.twitterDefault = (userDetails.get(Constants.TWITTER_DEFAULT).equalsIgnoreCase("true"))? 1:0;
                        NPDN.user.update();
                    }

                } else {
                    Toasteroid.show((Activity)context, response.message(), Toasteroid.STYLES.ERROR);
                }
            }
            @Override
            public void onFailure(Call<_Status> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
