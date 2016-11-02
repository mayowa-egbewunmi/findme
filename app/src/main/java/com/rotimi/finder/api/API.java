package com.rotimi.finder.api;


import com.rotimi.finder.main.Status;
import com.rotimi.finder.main.publicreports.ReportItem;

import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mayowa on 9/2/16.
 */
public interface API {

    @GET("reports/")
    Call<ReportItem> getReports();

    @GET("reports/{id}")
    Call<ReportItem> getReportDetails(@Path("id") int id);

    @POST("reports/")
    Call<Status> postReport(@Body Map<String, String> a);

    @POST("reports/{id}/sighting")
    Call<Status> postReportSighting(@Body Map<String, String> a);

//    @POST("auth/teacher/firstAccess")
//    Call<_Verify> verify(@Body Map<String, String> details);

//    @POST("auth/teacher/login")
//    Call<_Login> login(@Body Map<String, String> details);


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
