package com.example.melearning.examples
import retrofit2.Response
import retrofit2.http.*
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun main() {
    var retrofit: AlbumService =
        RetrofitHelper
            .createRetrofitInstance(RetrofitHelper.BaseUrl)
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
data class AlbumInfoItem(val id: Int = 0, val title: String = "", val userId: Int = 0)
class AlbumInfo : ArrayList<AlbumInfoItem>()
data class UserInfo(val id: Int = 0)

interface AlbumService {
    @GET(value = "/albums/")
    suspend fun getAlbums(): Response<AlbumInfo>

    @GET(value = "/albums/")
    fun getAlbumsRx(): Observable<AlbumInfo>

    @GET(value = "/users/")
    fun getUsersRx(): Observable<List<UserInfo>>

    @GET(value = "/albums/")
    fun getUserAlbumsRx(@Query(value = "userId") userId:Int): Observable<AlbumInfo>

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

        @JvmStatic
        fun createRetrofitInstance(url: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(url)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }
}