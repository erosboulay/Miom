package com.example.miom.data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class Recipes {

    @Composable
    public fun RecipesList(
        modifier: Modifier = Modifier,
        onRecipeSelected: (recipe: Recipe) -> Unit
    ){
        LazyColumn() {
            items(recipes){recipe ->
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

data class Recipe(
    val name: String,
    val description: String
)

private val recipes: List<Recipe> = listOf(
    Recipe(name = "Strawberry Shortcake", description = "A strawberry and cream based treat."),
    Recipe(name = "Chocolate Cake", description = "Rich and moist chocolate cake."),
    Recipe(name = "Apple Pie", description = "Classic apple pie with a flaky crust."),
    Recipe(name = "Banana Bread", description = "Sweet and soft banana bread."),
    Recipe(name = "Lemon Tart", description = "Tangy lemon tart with a buttery crust."),
    Recipe(name = "Cheesecake", description = "Creamy cheesecake with a graham cracker crust."),
    Recipe(name = "Carrot Cake", description = "Moist carrot cake with cream cheese frosting."),
    Recipe(name = "Blueberry Muffins", description = "Soft muffins packed with blueberries."),
    Recipe(name = "Pumpkin Pie", description = "Traditional spiced pumpkin pie."),
    Recipe(name = "Pecan Pie", description = "Sweet and nutty pecan pie."),
    Recipe(name = "Cinnamon Rolls", description = "Soft rolls with cinnamon and sugar."),
    Recipe(name = "Chocolate Chip Cookies", description = "Classic cookies loaded with chocolate chips."),
    Recipe(name = "Vanilla Cupcakes", description = "Fluffy vanilla cupcakes with buttercream frosting."),
    Recipe(name = "Mango Sorbet", description = "Refreshing mango sorbet."),
    Recipe(name = "Tiramisu", description = "Coffee-flavored Italian dessert."),
    Recipe(name = "Crepes", description = "Thin pancakes that can be filled with sweet or savory fillings."),
    Recipe(name = "Brownies", description = "Fudgy chocolate brownies."),
    Recipe(name = "Strawberry Smoothie", description = "Fresh strawberry smoothie with yogurt."),
    Recipe(name = "Lemon Meringue Pie", description = "Tangy lemon pie topped with fluffy meringue."),
    Recipe(name = "Chocolate Mousse", description = "Light and airy chocolate dessert.")
)
