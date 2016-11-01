package com.rotimi.finder.api;

import android.content.Context;

import com.softcom.npdn.utils.Constants;
import com.softcom.npdn.utils.Utility;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mayowa on 7/31/16.
 */
public class RequestInterceptor {

    public Retrofit retrofit;
    public Utility utility;

    public RequestInterceptor(Context context){

        utility = new Utility(context);
        final String token = utility.getSharedPrefs().getString(Constants.TOKEN);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        chain -> {
                            okhttp3.Request original = chain.request();

                            // RequestInterceptor customization: add request headers
                            okhttp3.Request.Builder requestBuilder = original.newBuilder()
                                    .method(original.method(), original.body());

                            if(token !=null){
                                requestBuilder.addHeader(Constants.AUTHORIZATION_BEARER, "Bearer "+token);
                            }

                            okhttp3.Request request = requestBuilder.build();
                            return chain.proceed(request);
                        })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.END_POINT)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }
}
