package com.sang.peoplan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Sangjun on 2018-02-22.
 */


public interface APIService { // retrofit 사용을 위한 http api 인터페이스, get, post, put 등등

    @GET("/api/users") //모든 유저 데이터
    Call<List<User>> getUserList();

    @GET("/api/users/{user}") // 특정 유저 데이터
    Call<List<User>> getUser(@Path("user") String userUID);

    @POST("/api/users") // 유저 생성
    Call<User> createUser(@Body User user);

    @GET("/api/calendars/user/{user}") // 유저 캘린더 데이터 가져오기
    Call<List<Event>> getUserEvents(@Path("user") String userUID);

    @PUT("/api/calendars/user/{user}/event")
    Call<Event> createEvent(@Path("user") String userUID, @Body Event event);

    @PUT("/api/businesscards/user/{user}")
    Call<BusinessCard> createBusinessCard(@Path("user") String userUID, @Body BusinessCard businessCard);

    @GET("/api/businesscards/user/{user}")
    Call<List<BusinessCard>> getBusinessCards(@Path("user") String userUID);

    @PUT("/api/businesscards/update/{id}")
    Call<BusinessCard> updateBusinessCard(@Path("id") String id, @Body BusinessCard businessCard);

    @PUT("/api/businesscards/remove/{id}")
    Call<BusinessCard> removeBusinessCard(@Path("id") String id);

    @POST("/api/groups") // 그룹 생성
    Call<Group> createGroup(@Body Group group);

    @GET("/api/groups/user/{user}") // 속한 그룹 찾기
    Call<List<Group>> getGroups(@Path("user") String userUID);
}
