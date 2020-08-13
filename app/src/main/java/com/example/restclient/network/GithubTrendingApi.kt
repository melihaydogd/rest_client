package com.example.restclient.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://github-trending-api.now.sh/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

    //To test getting JSON.
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
//    .baseUrl(BASE_URL)
//    .build()


enum class TimeFilter(val value: String) {DAILY("daily"), WEEKLY("weekly"), MONTHLY("monthly")}

interface GithubApiService {
    @GET("repositories")
    fun getProperties(@Query("language") language: String, @Query("since") since: String):
            Deferred<List<GithubRepository>>
}

object GithubApi {
    val retrofitService: GithubApiService by lazy {
        retrofit.create(GithubApiService::class.java)
    }
}