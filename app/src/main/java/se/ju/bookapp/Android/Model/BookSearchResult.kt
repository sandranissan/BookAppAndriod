package se.ju.bookapp.Android.Model

data class BookSearchResult(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)