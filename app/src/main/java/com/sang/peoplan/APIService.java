package com.sang.peoplan;

import com.sang.peoplan.group_pack.Group;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("/api/users/{user}/calendars") // 유저 캘린더 데이터 가져오기
    Call<List<Event>> getUserEvents(@Path("user") String userUID);

    @PUT("/api/users/{user}/calendars")
    Call<Event> createEvent(@Path("user") String userUID, @Body Event event);

    @POST("/api/users/{user}/businesscards")
    Call<BusinessCard> createBusinessCard(@Path("user") String userUID, @Body BusinessCard businessCard);

    @GET("/api/user/{user}/businesscards")
    Call<List<BusinessCard>> getBusinessCards(@Path("user") String userUID);

    @PUT("/api/businesscards/{id}")
    Call<BusinessCard> updateBusinessCard(@Path("id") String id, @Body BusinessCard businessCard);

    @DELETE("/api/businesscards/{id}")
    Call<BusinessCard> removeBusinessCard(@Path("id") String id);

    @POST("/api/groups") // 그룹 생성
    Call<Group> createGroup(@Body Group group);

    @GET("/api/users/{user}/groups") // 속한 그룹 찾기
    Call<List<Group>> getGroups(@Path("user") String userUID);
}
