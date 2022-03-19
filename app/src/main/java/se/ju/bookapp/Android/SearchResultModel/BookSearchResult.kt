package se.ju.bookapp.Android.SearchResultModel

data class BookSearchResult(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)