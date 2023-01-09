package com.example.bibliotheque

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://henri-potier.techx.fr/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface BookApi {
    @GET("books")
    fun getBooks(): Call<List<Book>>

    companion object {
        fun create(): BookApi {
            return retrofit.create(BookApi::class.java)
        }
    }
}
