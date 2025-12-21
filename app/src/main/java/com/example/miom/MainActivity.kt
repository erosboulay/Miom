package com.example.miom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.miom.composables.Recipes
import com.example.miom.ui.theme.GreyLighter
import com.example.miom.ui.theme.MiomTheme
import com.example.miom.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiomTheme {
                MiomApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun MiomApp() {
    // Initialize navigation

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.RECIPES) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), containerColor = GreyLighter) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {


                Title(
                    title = "Recipes",
                )

                val recipeData = Recipes()
                val focusManager = LocalFocusManager.current

                var searchQuery by remember { mutableStateOf("") }
                val filteredItems = recipeData.getRecipes().filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.description.contains(searchQuery, ignoreCase = true)
                }

                BackHandler(enabled = true) {
                    if (searchQuery.isNotEmpty()) {
                        searchQuery = "" // clear search instead of exiting
                        focusManager.clearFocus()
                    } else {
                        // Optionally call default behavior
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    // Search input
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search recipes") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )

                    // LazyColumn with filtered results
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredItems) { recipe ->
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = recipe.name
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        text = recipe.description
                                    )
                                }
                            )
                        }
                    }
                }

            }

        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    RECIPES("Recipes", Icons.Default.Restaurant),
    GROCERIES("Groceries", Icons.Default.ShoppingCart),
}

@Composable
fun Title(title: String) {
    Text(
        text = title,
        style = Typography.titleLarge,
        modifier = Modifier.fillMaxWidth()
    )
}
