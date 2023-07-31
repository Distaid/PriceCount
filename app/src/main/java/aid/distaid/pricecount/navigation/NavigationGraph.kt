package aid.distaid.pricecount.navigation

import aid.distaid.pricecount.composableNoAnimation
import aid.distaid.pricecount.ui.screens.CategoriesScreen
import aid.distaid.pricecount.ui.screens.CreateItemScreen
import aid.distaid.pricecount.ui.screens.EditProductScreen
import aid.distaid.pricecount.ui.screens.HomeScreen
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(
    navHostController: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = NavigationItem.Home.route,
    ) {
        composableNoAnimation(
            route = NavigationItem.Home.route
        ) {
            HomeScreen(
                onAddProduct = {
                    navHostController.navigate(NavigationItem.CreateProduct.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onEditProduct = {
                    navHostController.navigate("${NavigationItem.EditProduct.path}${it}") {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onOpenCategories = {
                    navHostController.navigate(NavigationItem.Categories.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
        composableNoAnimation(
            route = NavigationItem.CreateProduct.route
        ) {
            CreateItemScreen(
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        composableNoAnimation(
            route = NavigationItem.EditProduct.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
        ) { backStackEntry ->
            EditProductScreen(
                backStackEntry.arguments!!.getInt("id"),
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        composableNoAnimation(
            route = NavigationItem.Categories.route
        ) {
            CategoriesScreen(
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}