package aid.distaid.pricecount.navigation

import aid.distaid.pricecount.ui.screens.CreateItemScreen
import aid.distaid.pricecount.ui.screens.EditProductScreen
import aid.distaid.pricecount.ui.screens.HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavigationGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = NavigationItem.Home.route,
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(
                navHostController
            )
        }
        composable(NavigationItem.CreateProduct.route) {
            CreateItemScreen(
                navHostController
            )
        }
        composable(
            NavigationItem.EditProduct.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            EditProductScreen(
                navHostController,
                backStackEntry.arguments!!.getInt("id")
            )
        }
    }
}