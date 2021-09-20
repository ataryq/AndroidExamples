package com.example.melearning.fragments.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.melearning.examples.AlbumService
import com.example.melearning.examples.ImageInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.io.IOException

class ItemKeyPagingLoader(private val api: AlbumService): PagingSource<Int, PostData>() {
    override fun getRefreshKey(state: PagingState<Int, PostData>): Int = 0

    private suspend fun loadPostImageInfo(postId: Int): ImageInfo {
        return api.getPhoto(postId).body()?.firstOrNull() ?: ImageInfo()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostData> {
        return try {
            val items = mutableListOf<PostData>()

            api.getPosts(
                params.key ?: 0,
                params.loadSize
            ).body()?.asFlow()?.map {
                PostData(it, loadPostImageInfo(it.id))
            }?.collect{
                items.add(it)
            }

            println("ItemKeyPagingLoader: items: ${items.size}")

            val curKey: Int = params.key ?: 0
            val nextKey: Int = curKey.plus(params.loadSize)
            val prevKey = curKey - params.loadSize
            LoadResult.Page(
                items,
                prevKey = if(prevKey < 0) null else prevKey,
                nextKey = if(items.isEmpty()) null else nextKey
            )
        }
        catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}