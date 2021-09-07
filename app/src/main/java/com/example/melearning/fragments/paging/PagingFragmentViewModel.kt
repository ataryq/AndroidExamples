package com.example.melearning.fragments.paging

import androidx.lifecycle.ViewModel
import com.example.melearning.examples.AlbumService
import com.example.melearning.examples.PostInfo
import com.example.melearning.examples.RetrofitHelper

class PagingFragmentViewModel: ViewModel() {
    private val albumApi: AlbumService = RetrofitHelper
        .createRetrofitInstance(RetrofitHelper.BaseUrl)
        .create(AlbumService::class.java)

    private val repo = ItemKeyPagingRepository(albumApi).create(3)

    fun getPosts() = repo.flow

    var chosenPost: PostInfo? = null
}