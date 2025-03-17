package com.example.devmobile

data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val imageUrl: Int, // Resource ID for the image
    var isFavorite: Boolean = false // Add a favorite property
)