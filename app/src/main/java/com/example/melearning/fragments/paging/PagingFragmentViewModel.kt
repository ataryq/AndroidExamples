package com.example.melearning.fragments.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.example.melearning.examples.AlbumService
import com.example.melearning.examples.PostInfo
import com.example.melearning.examples.RetrofitHelper
import kotlinx.coroutines.flow.flow

class PagingFragmentViewModel: ViewModel() {
    private val albumApi: AlbumService = RetrofitHelper
        .createRetrofitInstance(RetrofitHelper.BaseUrl)
        .create(AlbumService::class.java)

    private val repo = ItemKeyPagingRepository(albumApi)
    private val posts = repo.create(3).cachedIn(viewModelScope)
    fun getPosts() = posts
}