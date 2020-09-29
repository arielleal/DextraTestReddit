package com.fastnews.repository

import com.fastnews.service.api.RedditAPI
import com.fastnews.service.model.PostChildrenAfter
import com.fastnews.service.model.PostData

object PostRepository: BaseRepository() {

    suspend fun getPosts(after: String, limit: Int): PostChildrenAfter {

        val postResponse = safeApiCall(
            call = { RedditAPI.redditService.getPostList(limit, after).await() },
            errorMessage = "Error to fetching posts"
        )

        val afterId = postResponse?.data?.let { it.after }?: kotlin.run { "" }

        val listPostData: MutableList<PostData> = mutableListOf()
        postResponse?.data?.children?.map { postChildren -> listPostData.add(postChildren.data) }

        return PostChildrenAfter(listPostData, afterId)
    }
}