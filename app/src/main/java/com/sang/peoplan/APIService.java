package com.sang.peoplan;

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
    Call<Void> createUser(@Body User user);

    @PUT("/api/users/token")
    Call<Token> refreshToken(@Body Token token);

    @GET("/api/users/{user}/events") // 유저 event 데이터 가져오기
    Call<List<Event>> getUserEvents(@Path("user") String userUID);

    @POST("/api/users/{user}/events")
    Call<Event> createEvent(@Path("user") String userUID, @Body Event event);

    @POST("/api/users/{user}/businesscards")
    Call<BusinessCard> createBusinessCard(@Path("user") String userUID, @Body BusinessCard businessCard);

    @GET("/api/users/{user}/businesscards")
    Call<List<BusinessCard>> getBusinessCards(@Path("user") String userUID);

    @PUT("/api/businesscards/{id}")
    Call<BusinessCard> updateBusinessCard(@Path("id") String id, @Body BusinessCard businessCard);

    @DELETE("/api/businesscards/{id}")
    Call<BusinessCard> removeBusinessCard(@Path("id") String id);

    @POST("/api/groups") // 그룹 생성
    Call<Group> createGroup(@Body Group group);

    @GET("/api/users/{user}/groups") // 속한 그룹 찾기
    Call<List<Group>> getGroups(@Path("user") String userUID);

    @GET("/api/groups/{name}") // 그룹 찾기
    Call<List<Group>> searchGroups(@Path("name") String input);

    @DELETE("/api/groups/{group}") // 그룹 삭제
    Call<Group> removeGroup(@Path("group") String group_id);

    @PUT("/api/groups/{group}/members/{user}") // 그룹 탈퇴
    Call<Group> leaveGroup(@Path("group") String group_id, @Path("user") String user_id);
}
