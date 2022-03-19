package se.ju.bookapp.Android.Api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import se.ju.bookapp.Android.SearchResultModel.BookSearchResult

interface GoogleBooksApi {
    @GET("/books/v1/volumes?printType=books")
    suspend fun getBooksByQuery(
        @Query("q") query: String
    ): Response<BookSearchResult>
}