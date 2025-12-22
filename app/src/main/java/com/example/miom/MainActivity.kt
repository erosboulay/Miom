package com.example.miom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.miom.screens.GroceriesScreen
import com.example.miom.screens.RecipeCreationScreen
import com.example.miom.screens.RecipesScreen
import com.example.miom.ui.theme.GreyDark
import com.example.miom.ui.theme.GreyDarkest
import com.example.miom.ui.theme.GreyLight
import com.example.miom.ui.theme.GreyLighter
import com.example.miom.ui.theme.MiomTheme

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

    AnimatedContent(
        targetState = currentDestination,
        transitionSpec = {
            if (targetState == AppDestinations.RECIPE_CREATION) {
                // Slide up when entering creation screen
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                ) togetherWith fadeOut(animationSpec = tween(400))
            } else if (initialState == AppDestinations.RECIPE_CREATION) {
                // Slide down when going back from creation screen
                fadeIn(animationSpec = tween(400)) togetherWith slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            } else {
                // Default crossfade for other transitions
                fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200))
            }
        },
        label = "ScreenTransition"
    ) { state ->
        if (state == AppDestinations.RECIPE_CREATION) {
            RecipeCreationScreen(onBack = { currentDestination = AppDestinations.RECIPES })
        } else {
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    AppDestinations.entries.filter { it.showInNavBar }.forEach {
                        item(
                            icon = {
                                Icon(
                                    it.icon,
                                    contentDescription = it.label
                                )
                            },
                            selected = it == state,
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

                        when (state) {
                            AppDestinations.RECIPES -> RecipesScreen(
                                onAddRecipe = { currentDestination = AppDestinations.RECIPE_CREATION }
                            )
                            AppDestinations.GROCERIES -> GroceriesScreen()
                            else -> {}
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
    val showInNavBar: Boolean = true
) {
    RECIPES("Recipes", Icons.Default.Restaurant),
    GROCERIES("Groceries", Icons.Default.ShoppingCart),
    RECIPE_CREATION("Create", Icons.Default.Add, showInNavBar = false),
}
