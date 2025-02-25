package com.example.devmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
    }
}

// Sample Article Data Class
data class Article(val id: Int, val title: String, val content: String)

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
                title = "Article ${ (page - 1) * 20 + it }",
                content = "Content for article ${ (page - 1) * 20 + it }"
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
            ArticleItem(article)
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
fun ArticleItem(article: Article) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = article.title)
        Text(text = article.content)
    }
}