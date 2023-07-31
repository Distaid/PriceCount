package aid.distaid.pricecount.ui.screens

import aid.distaid.pricecount.data.sql.AidDbHandler
import aid.distaid.pricecount.ui.AidBottomAppBar
import aid.distaid.pricecount.ui.AidTopAppBar
import aid.distaid.pricecount.ui.ProductItemBox
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
private fun ActiveProducts(
    dbHandler: AidDbHandler,
    onEditProduct: (Int) -> Unit,
    productsChanged: (sum: Float, count: Int) -> Unit
) {
    val products = remember {
        dbHandler.productsHandler.getAll()
    }

    fun getTotalSum(): Float {
        return products.fold(0f) { acc, item -> acc + item.sum }
    }
    productsChanged(getTotalSum(), products.size)

    LazyColumn {
        items(
            items = products,
            key = { item -> item.id }
        ) { item ->
            ProductItemBox(
                item,
                onEdit = { onEditProduct(item.id) },
                onRemove = {
                    dbHandler.productsHandler.delete(item)
                    products.clear()
                    products.addAll(dbHandler.productsHandler.getAll())
                },
                onChangeActive = {
                    dbHandler.productsHandler.changeActive(item)
                    products.clear()
                    products.addAll(dbHandler.productsHandler.getAll())
                }
            )
        }
    }
}

@Composable
private fun FinishedProducts(
    dbHandler: AidDbHandler
) {
    val finishedProducts = remember {
        dbHandler.productsHandler.getAll(isActive = false)
    }

    LazyColumn {
        items(
            items = finishedProducts,
            key = { item -> item.id }
        ) { item ->
            ProductItemBox(
                item,
                onEdit = { },
                onRemove = {
                    dbHandler.productsHandler.delete(item)
                    finishedProducts.clear()
                    finishedProducts.addAll(dbHandler.productsHandler.getAll(isActive = false))
                },
                onChangeActive = {
                    dbHandler.productsHandler.changeActive(item)
                    finishedProducts.clear()
                    finishedProducts.addAll(dbHandler.productsHandler.getAll(isActive = false))
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit,
    onOpenCategories: () -> Unit
) {
    val tabs = listOf("Активные", "Завершенные")
    val dbHandler = AidDbHandler(LocalContext.current)

    var tabState by remember { mutableStateOf(0) }

    var productsSum by remember { mutableStateOf(0f) }
    var productsCount by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AidTopAppBar(onOpenCategories = onOpenCategories) },
        bottomBar = {
            if (tabState == 0) {
                AidBottomAppBar(
                    onAddButtonClick = onAddProduct,
                    itemsSum = productsSum,
                    itemsCount = productsCount
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                TabRow(
                    selectedTabIndex = tabState,
                    indicator = { tabPositions ->
                        if (tabState < tabPositions.size) {
                            TabRowDefaults.Indicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[tabState])
                                    .padding(horizontal = 40.dp)
                                    .clip(CircleShape),
                            )
                        }
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = tabState == index,
                            onClick = { tabState = index },
                            unselectedContentColor = MaterialTheme.colorScheme.secondary,
                            text = { Text(text = title, overflow = TextOverflow.Ellipsis) }
                        )
                    }
                }
                when(tabState) {
                    0 -> ActiveProducts(
                        dbHandler = dbHandler,
                        onEditProduct = onEditProduct,
                        productsChanged = { sum, count ->
                            productsSum = sum
                            productsCount = count
                        }
                    )
                    1 -> FinishedProducts(dbHandler)
                }
            }
        }
    }
}