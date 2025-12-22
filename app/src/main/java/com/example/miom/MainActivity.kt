package com.example.miom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.miom.composables.Recipes
import com.example.miom.ui.theme.BrandDark
import com.example.miom.ui.theme.GreyDark
import com.example.miom.ui.theme.GreyDarkest
import com.example.miom.ui.theme.GreyLight
import com.example.miom.ui.theme.GreyLighter
import com.example.miom.ui.theme.GreyLightest
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

    val myItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent,
            selectedIconColor = GreyDarkest,
            unselectedIconColor = GreyDark
        )
    )

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
                    onClick = { currentDestination = it },
                    colors = myItemColors
                )
            }
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = GreyLight,
        )
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), containerColor = GreyLighter) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp, 20.dp, 20.dp, 0.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                when (currentDestination){
                    AppDestinations.RECIPES -> RecipesScreen()
                    AppDestinations.GROCERIES -> GroceriesScreen()
                }

            }

        }
    }
}

@Composable
fun RecipesScreen() {

    Box(modifier=Modifier.fillMaxSize()){

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
            Title(title = "Recipes")

            // Search input
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search recipes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = GreyLight,
                    unfocusedContainerColor = GreyLight,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(percent = 50),
                enabled = true,
                isError = false
            )

            // LazyColumn with filtered results
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(filteredItems) { recipe ->
                    Box(
                        modifier = Modifier
                            .background(color = Color.Transparent)
                            .padding(0.dp, 0.dp, 0.dp, 10.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GreyLightest)
                                .padding(10.dp)
                        ) {

                            // TODO: Use user defined image if it exists, otherwise use default icon
                            Icon(
                                painter = painterResource(id = R.drawable.image),
                                contentDescription = "a placeholder",
                                modifier = Modifier.size(96.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(text = recipe.name, color = GreyDarkest)
                                Text(text = recipe.description, color = GreyDark)
                            }
                            HorizontalDivider(color = Color.Transparent, thickness = 10.dp)

                        }


                    }
                }
            }

        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(0.dp, 0.dp, 10.dp, 30.dp)              // Margin from the edges
                .size(64.dp)                // Standard FAB size
                .clip(CircleShape)          // Makes it round
                .background(BrandDark)          // Your theme color
                .clickable { /* TODO: Add recipe action */ },

            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Recipe",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }


    }


}


@Composable
fun GroceriesScreen(){
    Title(title = "Grocery list")
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp)
    )
}
