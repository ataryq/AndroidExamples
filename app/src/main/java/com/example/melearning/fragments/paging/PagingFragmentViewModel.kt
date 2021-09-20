package com.example.melearning.fragments.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.melearning.examples.AlbumService
import com.example.melearning.examples.RetrofitHelper

class PagingFragmentViewModel: ViewModel() {
    private val api: AlbumService = RetrofitHelper
        .createRetrofitInstance(RetrofitHelper.BaseUrl)
        .create(AlbumService::class.java)

    private val repo = ItemKeyPagingRepository(api)
    private val posts = repo.create(3).cachedIn(viewModelScope)

    fun getPosts() = posts
}