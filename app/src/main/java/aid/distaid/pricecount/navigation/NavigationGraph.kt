package aid.distaid.pricecount.navigation

import aid.distaid.pricecount.ui.screens.CreateItemScreen
import aid.distaid.pricecount.ui.screens.EditProductScreen
import aid.distaid.pricecount.ui.screens.HomeScreen
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(
    navHostController: NavHostController
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = NavigationItem.Home.route,
    ) {
        composable(
            NavigationItem.Home.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            HomeScreen(
                navHostController
            )
        }
        composable(
            NavigationItem.CreateProduct.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            CreateItemScreen(
                navHostController
            )
        }
        composable(
            NavigationItem.EditProduct.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            EditProductScreen(
                navHostController,
                backStackEntry.arguments!!.getInt("id")
            )
        }
    }
}