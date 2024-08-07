package com.example.cryptowatcher.service

import com.example.cryptowatcher.model.ApiDetailsResponse
import com.example.cryptowatcher.model.ApiListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("assets")
    suspend fun getList(): Response<ApiListResponse>

    @GET("assets/{id}")
    suspend fun getDetails(@Path("id") id: String): Response<ApiDetailsResponse>
}
