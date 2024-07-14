package com.example.apiintegration.network

import com.example.apiintegration.models.LoginRequest
import com.example.apiintegration.models.LoginResponse
import com.example.apiintegration.models.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/api/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/users")
    fun getUsers(@Query("page") page: Int = 2): Call<UserResponse>
}
