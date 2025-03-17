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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
                title = "Équipe ${(page - 1) * 20 + it}",
                content = "Description de l'équipe ${(page - 1) * 20 + it}",
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
                onFavoriteClick = {
                    article.isFavorite = !article.isFavorite
                }
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

@Composable
fun MainScreen() {
    DevMobileTheme {
        val navController = androidx.navigation.compose.rememberNavController()

        val bottomNavItems = listOf(
            BottomNavItem(
                name = "Home",
                route = "home",
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                name = "Profile",
                route = "profile",
                icon = Icons.Default.Person,
                badgeCount = 2 // Example badge count
            )
        )

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState() // Correctly called here
                            val currentRoute = navBackStackEntry?.destination?.route
                            bottomNavItems.forEach { item ->
                                NavigationBarItem(
                                    icon = {
                                        BadgedBox(
                                            badge = {
                                                if (item.badgeCount > 0) {
                                                    Badge {
                                                        Text(text = item.badgeCount.toString())
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.name
                                            )
                                        }
                                    },
                                    label = { Text(text = item.name) },
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            navController.graph.startDestinationRoute?.let { route ->
                                                popUpTo(route) {
                                                    saveState = true
                                                }
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost( // This is the composable function, not the interface
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { InfiniteArticleList() }
                        composable("profile") { UserProfileScreen() }
                    }
                }
            }
        }
    }
}