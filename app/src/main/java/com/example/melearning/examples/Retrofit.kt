package com.example.melearning.examples
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

fun main() {
    val retrofit: AlbumService =
        RetrofitHelper
            .createRetrofitInstance(RetrofitHelper.BaseUrl)
            .create(AlbumService::class.java)

    runBlocking {
        if(false) {
            val albumInfo = retrofit.getUserAlbums(3).body() as AlbumInfo
            for(album in albumInfo)
                println(album.title)
        }

        val posts = retrofit.getPosts(1, 20).body() as List<PostInfo>
        for(item in posts) println(item)
    }
}

data class AlbumInfoItem(val id: Int = 0, val title: String = "", val userId: Int = 0)
class AlbumInfo : ArrayList<AlbumInfoItem>()
data class UserInfo(val id: Int = 0)

data class PostInfo(var userId: Int = 0,
                    var id: Int = 0,
                    var title: String = "",
                    var body: String = "",
                    //means it will not be used in retrofit, just internal needs
                    @Transient var listOrder: Int = -1)

data class ImageInfo(var albumId: Int = 0,
                     var id: Int = 0,
                     var title: String = "",
                     var url: String = "",
                     var thumbnailUrl: String = "",
                     @Transient var listOrder: Int = -1)

enum class Order(val param: String) {
    Ascending("asc"),
    Descending("desc")
}

interface AlbumService {
    @GET(value = "/posts/")
    suspend fun getPosts(@Query(value = "_page") page:Int,
                         @Query(value = "_limit") limit:Int,
                         @Query(value = "_sort") sortBy:String = "id",
                         @Query(value = "_order") order:String = Order.Ascending.param
        ): Response<List<PostInfo>>

    @GET(value = "/photos/")
    suspend fun getPhoto(@Query(value = "id") id:Int): Response<List<ImageInfo>>

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
        //see documentation
        //https://jsonplaceholder.typicode.com/
        const val BaseUrl = "https://jsonplaceholder.cypress.io/"

        private fun createInterceptor() = HttpLoggingInterceptor()
                .apply { this.level = HttpLoggingInterceptor.Level.BODY }

        private fun createOkHttpClient() = OkHttpClient.Builder()
                .addInterceptor(createInterceptor()).build()

        @JvmStatic
        fun createRetrofitInstance(url: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(url)
//                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }
}