package com.example.apitest.api

import com.example.apitest.model.Posts
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiManage {
    @GET("test")
    fun getPosts(): Call<Posts>

    @POST("test")
    fun putPosts(@Body posts: Posts): Call<Posts>
}