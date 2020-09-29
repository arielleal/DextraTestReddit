package com.fastnews.service.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RedditAPI {
    private var API_BASE_URL: String = "https://www.reddit.com/r/Android/"
    private var httpClient = OkHttpClient.Builder()

    private var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())

    private var retrofit: Retrofit = builder
        .client(httpClient.build())
        .build()

    var redditService: RedditService = retrofit.create<RedditService>(
        RedditService::class.java)

}
