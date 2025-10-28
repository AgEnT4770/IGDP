package com.example.igdp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.igdp.ui.theme.IGDPTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IGDPTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val navItems = listOf("Home", "Search", "Discover", "Profile")
    val navIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Search,
        Icons.Filled.Explore,
        Icons.Filled.Person
    )
    val pagerState = rememberPagerState(pageCount = { navItems.size })
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.gamingbook),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            "Home",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },

                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 12.dp)) {
                NavigationBar(modifier = Modifier.clip(RoundedCornerShape(24.dp))) {
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(navIcons[index], contentDescription = item) },
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> ScrollContent(innerPadding)
                1 -> SearchPage(modifier = Modifier.padding(innerPadding))
                2 -> DiscoverPage(modifier = Modifier.padding(innerPadding))
                3 -> ProfilePage(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun ScrollContent(innerPadding: PaddingValues) {
    val categories = mapOf(
        "Trending" to List(10) { "Game ${it + 1}" },
        "Action" to List(10) { "Game ${it + 1}" },
        "Adventure" to List(10) { "Game ${it + 1}" },
        "RPG" to List(10) { "Game ${it + 1}" },
        "Strategy" to List(10) { "Game ${it + 1}" },
        "Indie" to List(10) { "Game ${it + 1}" }
    )

    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        categories.forEach { (category, games) ->
            item {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(games) { game ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.gamingbook),
                                contentDescription = game,
                                modifier = Modifier.size(120.dp),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = game,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    IGDPTheme {
        MainScreen()
    }
}
