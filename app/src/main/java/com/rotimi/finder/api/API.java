package com.rotimi.finder.api;

import com.softcom.npdn.api.models._Announcement;
import com.softcom.npdn.api.models._Feed;
import com.softcom.npdn.api.models._Feeds;
import com.softcom.npdn.api.models._Login;
import com.softcom.npdn.api.models._Status;
import com.softcom.npdn.api.models._Token;
import com.softcom.npdn.api.models._Validate;
import com.softcom.npdn.api.models._Verify;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mayowa on 9/2/16.
 */
public interface API {

//    @POST("auth/teacher/firstAccess")
//    Call<_Verify> verify(@Body Map<String, String> details);

    @POST("auth/teacher/login")
    Call<_Login> login(@Body Map<String, String> details);


//    @PUT("teacher/{id}")
//    Call<_Status> updateUser(@Path("id") int id, @Body Map<String, String> details);
//
//    @POST("teacher/credential/validate")
//    Call<_Validate> validate(@Body Map<String, String> details);
//
//    @GET("newsfeed/")
//    Call<List<_Feeds>> getFeeds(@QueryMap() Map<String, Integer> query);
//
//    @GET("newsfeed/{id}")
//    Call<_Feed> getFeed(@Path("id") int id);
//
//    @GET("announcement/")
//    Call<List<_Announcement>> getAnnouncements(@QueryMap() Map<String, Integer> query);
//
//    @POST("auth/teacher/token")
//    Call<_Token> getToken(@Body() Map<String, String> token);
//
//    @POST("newsfeed")
//    Call<_Status> postHelp(@Body() Map<String, String> body);
//
//    @POST("/newsfeed/{id}/comment")
//    Call<_Status> postComment(@Path("id") int id, @Body() Map<String, String> body);
//
//    @POST("/newsfeed/{id}/like")
//    Call<_Status> postLike(@Path("id") int id, @Body() Map<String, Integer> body);


}
