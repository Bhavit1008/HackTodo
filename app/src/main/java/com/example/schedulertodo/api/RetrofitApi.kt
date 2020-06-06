package com.example.schedulertodo.api

import com.example.schedulertodo.models.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitApi {
    @FormUrlEncoded
    @POST("/api/user/register")
    fun createUser(
        @Field("fullName") name:String,
        @Field("phoneNumber") number:String,
        @Field("email") email:String,
        @Field("password") password:String
    ): retrofit2.Call<RegistrationResponse>

    @FormUrlEncoded
    @POST("/api/user/login")
    fun loginUser(
        @Field("email") email:String,
        @Field("password") password:String
    ):retrofit2.Call<LoginResponse>

    @FormUrlEncoded
    @POST("/api/todo")
    fun addTask(
        @Field("task") task:String,
        @Field("description") description:String,
        @Field("dueDate") dueDate:String,
        @Field("status") status:String,
        @Field("taskType") taskType:String,
        @Field("Authorization") token:String
        ): retrofit2.Call<TaskResponse>

    @FormUrlEncoded
    @POST("/api/todo/analysis")
    fun taskCount(
        @Field("email") email:String
    ): retrofit2.Call<TaskCountResponse>


    @FormUrlEncoded
    @POST("/api/todo/status")
    fun taskStatusCount(
        @Field("email") email:String
    ): retrofit2.Call<StatusCountResponse>

}