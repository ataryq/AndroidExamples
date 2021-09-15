package com.example.melearning.fragments.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.melearning.examples.AlbumService

class ItemKeyPagingRepository(private val api: AlbumService) {
    fun create(pageSize: Int) = Pager(
        PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        )
    ) {
        println("ItemKeyPagingRepository: create")
        ItemKeyPagingLoader(api)
    }.flow
}