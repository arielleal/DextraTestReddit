package com.fastnews.service.api

import com.fastnews.service.model.CommentResponse
import com.fastnews.service.model.PostResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditService {

    @GET("new/.json?")
    fun getPostList(@Query("limit") limit: Int, @Query("after") after: String): Deferred<Response<PostResponse>>

    @GET("comments/{id}.json")
    fun getCommentList(@Path("id") postId: String): Deferred<Response<List<CommentResponse>>>


}