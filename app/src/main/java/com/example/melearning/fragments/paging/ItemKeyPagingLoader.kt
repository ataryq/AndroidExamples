package com.example.melearning.fragments.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.melearning.examples.AlbumService
import com.example.melearning.examples.PostInfo
import java.io.IOException
import kotlin.math.max

class ItemKeyPagingLoader(private val api: AlbumService): PagingSource<Int, PostInfo>() {
    override fun getRefreshKey(state: PagingState<Int, PostInfo>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostInfo> {
        return try {
            val items = api.getPosts(
                params.key ?: 0,
                params.loadSize
            ).body()

            if(items == null)
                LoadResult.Error(Throwable("empty"))
            else {
                val curKey: Int = params.key ?: 0
                val nextKey: Int = curKey.plus(params.loadSize)
                val prevKey = curKey - params.loadSize
                LoadResult.Page(
                    items,
                    prevKey = if(prevKey < 0) null else prevKey,
                    nextKey = if(items.isEmpty()) null else nextKey
                )
            }
        }
        catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}