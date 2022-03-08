package se.ju.bookapp.Android.Api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import se.ju.bookapp.Android.Model.BookSearchResult

interface GoogleBooksApi {
    @GET("/books/v1/volumes?q=lord+of+the+rings")
    suspend fun getBooks(): Response<BookSearchResult>

    @GET("/books/v1/volumes")
    suspend fun getBooksByQuery(
        @Query("q") query: String
    ): Response<BookSearchResult>
}