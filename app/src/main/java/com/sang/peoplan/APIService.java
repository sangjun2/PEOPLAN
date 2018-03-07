package com.sang.peoplan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Sangjun on 2018-02-22.
 */

public interface APIService { // retrofit 사용을 위한 http api 인터페이스, get, post, put 등등
    @GET("/api/users/{user}")
    Call<List<User>> getUser(@Path("user") String userUID);

    @POST("/api/users")
    Call<User> createUser(@Body User user);
}
