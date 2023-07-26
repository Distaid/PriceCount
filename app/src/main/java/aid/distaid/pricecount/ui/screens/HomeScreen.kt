package aid.distaid.pricecount.ui.screens

import aid.distaid.pricecount.data.sql.AidDbHandler
import aid.distaid.pricecount.navigation.NavigationItem
import aid.distaid.pricecount.ui.AidBottomAppBar
import aid.distaid.pricecount.ui.AidTopAppBar
import aid.distaid.pricecount.ui.ProductItemBox
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val dbHandler = AidDbHandler(LocalContext.current)

    val products = remember {
        dbHandler.productsHandler.getAll()
    }

    fun getTotalSum(): Float {
        return products.fold(0f) { acc, item -> acc + item.sum }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AidTopAppBar {} },
        bottomBar = {
            AidBottomAppBar(
                onAddButtonClick = {
                    navController.navigate(NavigationItem.CreateProduct.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                itemsSum = getTotalSum(),
                itemsCount = products.size
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn(modifier = Modifier.padding(bottom = 16.dp)) {
                items(
                    items = products,
                    key = { item -> item.id }
                ) { item ->
                    ProductItemBox(
                        item,
                        onEdit = { id ->
                            navController.navigate(NavigationItem.EditProduct.route.replace("{id}", id.toString())) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        },
                        onRemove = {
                            dbHandler.productsHandler.delete(item)
                            products.clear()
                            products.addAll(dbHandler.productsHandler.getAll())
                        }
                    )
                }
            }
        }
    }
}