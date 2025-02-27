package com.example.devmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.devmobile.ui.theme.DevMobileTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

// Sample Article Data Class
data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val imageUrl: Int, // Resource ID for the image
    var isFavorite: Boolean = false // Add a favorite property
)
@Composable
fun MainScreen() {
    DevMobileTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            InfiniteArticleList()
        }
    }
}

@Composable
fun InfiniteArticleList() {
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var currentPage by remember { mutableIntStateOf(1) }
    var hasMoreArticles by remember { mutableStateOf(true) }

    // Simulate fetching articles from a data source
    suspend fun fetchArticles(page: Int): List<Article> {
        delay(1000) // Simulate network delay
        val newArticles = (1..20).map {
            Article(
                id = (page - 1) * 20 + it,
                title = "Article ${(page - 1) * 20 + it}",
                content = "Content for article ${(page - 1) * 20 + it}",
                imageUrl = 0,
                isFavorite = false
            )
        }
        return newArticles
    }

    // Load initial articles
    LaunchedEffect(key1 = Unit) {
        isLoading = true
        val initialArticles = fetchArticles(currentPage)
        articles = initialArticles
        isLoading = false
    }

    // Load more articles when needed
    LaunchedEffect(key1 = currentPage) {
        if (currentPage > 1) {
            isLoading = true
            val newArticles = fetchArticles(currentPage)
            if (newArticles.isNotEmpty()) {
                articles = articles + newArticles
            } else {
                hasMoreArticles = false
            }
            isLoading = false
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(articles) { article ->
            ArticleItem(
                article,
                onFavoriteClick = { article.isFavorite = true }
            )
            HorizontalDivider()
        }

        // Loading indicator at the end
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        // Trigger loading more when near the end
        if (hasMoreArticles && !isLoading) {
            item {
                LaunchedEffect(key1 = Unit) {
                    currentPage++
                }
            }
        }
    }
}

@Composable
fun ArticleItem(article: Article, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Article Image

        Spacer(modifier = Modifier.width(8.dp))
        // Article Details
        Column(modifier = Modifier.weight(1f)) {
            if (article.isFavorite) {
                Text(text = article.title, style = MaterialTheme.typography.titleLarge)
            } else {
                Text(text = article.title, style = MaterialTheme.typography.titleMedium)
            }
            // Text(text = article.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = article.content, style = MaterialTheme.typography.bodyMedium)
        }
        // Favorite Button
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (article.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}