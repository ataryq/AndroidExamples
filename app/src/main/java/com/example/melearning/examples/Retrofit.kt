package com.example.melearning.examples
import retrofit2.Response
import retrofit2.http.*
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun main() {
    var retrofit: AlbumService = RetrofitHelper.createRetrofitInstance(RetrofitHelper.BaseUrl)
            .create(AlbumService::class.java)

    runBlocking {
        val albumInfo = retrofit.getUserAlbums(3).body() as AlbumInfo
        for(album in albumInfo)
            println(album.title)
    }
}
/*
fun withLiveDataExample(retrofit: AlbumService) {
    var responseLiveData = liveData {
        emit(retrofit.getUserAlbums(3).body() as ArrayList<AlbumInfoItem>)
    }

    responseLiveData.observe(this, Observer {

    })
}
*/
data class AlbumInfoItem(val id: Int, val title: String, val userId: Int)
class AlbumInfo : ArrayList<AlbumInfoItem>()

interface AlbumService {
    @GET(value = "/albums/")
    suspend fun getAlbums(): Response<AlbumInfo>

    @GET(value = "/albums/")
    suspend fun getUserAlbums(@Query(value = "userId") userId:Int): Response<AlbumInfo>

    @GET(value = "/albums/{id}")
    suspend fun getAlbumById(@Path(value = "id") id:Int): Response<AlbumInfoItem>

    @POST("/albums")
    suspend fun insertAlbum(@Body albumInfoItem: AlbumInfoItem): Response<AlbumInfoItem>
}

class RetrofitHelper {
    companion object {
        const val BaseUrl = "https://jsonplaceholder.cypress.io/"

        private fun createInterceptor() = HttpLoggingInterceptor()
                .apply { this.level = HttpLoggingInterceptor.Level.BODY }

        private fun createOkHttpClient() = OkHttpClient.Builder()
                .addInterceptor(createInterceptor()).build()

        fun createRetrofitInstance(url: String): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(url)
                    //.client(createOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .build()
        }
    }
}