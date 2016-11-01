package com.rotimi.finder.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softcom.npdn.R;
import com.softcom.npdn.api.models._Token;
import com.softcom.npdn.db.models.User;
import com.softcom.npdn.utils.Constants;
import com.softcom.npdn.utils.Utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mayowa on 9/30/16.
 */
public class NetworkRequest {

    private static final String LOG = NetworkRequest.class.getName();
    private Context context;
    private Utility utility;

    private static int responseStatus;
    private static String responseMessage;

    public NetworkRequest(Context context){
        this.context = context;
        this.utility = new Utility(context);
    }

    public static String execute(Map<String, String> params, String endPoint, String method) {

        String response = "";
        try {
            URL url = new URL(endPoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);

            //TODO: add authentication header

            Uri.Builder builder = new Uri.Builder();

            for (Map.Entry<String, String> key_value : params.entrySet()){
                builder.appendQueryParameter(key_value.getKey(), key_value.getValue());
            }

            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            responseMessage = conn.getResponseMessage();
            responseStatus = conn.getResponseCode();

            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line=br.readLine()) != null) {
                response+=line;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public boolean isSuccessful(){
        return (responseStatus >=200 && responseStatus <= 299);
    }

    public String getResponseMessage(){
        return responseMessage;
    }

    public void getToken(String intentFilter, String key){

        RequestInterceptor requestInterceptor = new RequestInterceptor(context);
        API api = requestInterceptor.retrofit.create(API.class);

        Log.d(LOG, SQLite.select().from(User.class).where().querySingle().key);
        Map<String, String> token = new HashMap<>();
        token.put(Constants.KEY, key);
        Call<_Token> call = api.getToken(token);

        call.enqueue(new Callback<_Token>() {
            @Override
            public void onResponse(Call<_Token> call, Response<_Token> response) {
                if(response.isSuccessful()) {
                    //update user token
                    User user =  SQLite.select().from(User.class).querySingle();
                    if(user == null) user = new User();
                    user.token = response.body().token;
                    user.save();

                    Log.d(LOG, user.token);

                    //send broadcast for retry
                    Intent intent = new Intent(intentFilter);
                    context.sendBroadcast(intent);

                }else{
                    Toast.makeText(context, response.message()+" for get token"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<_Token> call, Throwable t) {
                Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
