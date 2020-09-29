package com.fastnews.viewmodel

import android.app.Application
import androidx.annotation.UiThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fastnews.mechanism.Coroutines
import com.fastnews.repository.PostRepository
import com.fastnews.service.model.PostChildrenAfter

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var posts: MutableLiveData<PostChildrenAfter>

    @UiThread
    fun getPosts(after: String, limit: Int): MutableLiveData<PostChildrenAfter> {

        posts = MutableLiveData()

        Coroutines.ioThenMain({
            PostRepository.getPosts(after, limit)
        }) {
            posts.postValue(it)
        }

        return posts
    }

}