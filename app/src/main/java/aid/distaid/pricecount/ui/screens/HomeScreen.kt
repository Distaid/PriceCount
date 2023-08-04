package aid.distaid.pricecount.ui.screens

import aid.distaid.pricecount.R
import aid.distaid.pricecount.data.models.Category
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
private fun ActiveProducts(
    dbHandler: AidDbHandler,
    categoryForSelect: Category?,
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit
) {
    val products = dbHandler.productsHandler.getAll(category = categoryForSelect)

    fun updateProducts() {
        products.clear()
        products.addAll(dbHandler.productsHandler.getAll(category = categoryForSelect))
    }

    fun getTotalSum(): Float {
        return products.fold(0f) { acc, item -> acc + item.sum }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AidBottomAppBar(
                onAddButtonClick = onAddProduct,
                itemsSum = getTotalSum(),
                itemsCount = products.size
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).padding(bottom = 8.dp)
        ) {
            items(
                items = products,
                key = { item -> item.id }
            ) { item ->
                ProductItemBox(
                    item,
                    onEdit = { onEditProduct(item.id) },
                    onRemove = {
                        dbHandler.productsHandler.delete(item)
                        updateProducts()
                    },
                    onChangeActive = {
                        dbHandler.productsHandler.changeActive(item)
                        updateProducts()
                    }
                )
            }
        }
    }
}

@Composable
private fun FinishedProducts(
    dbHandler: AidDbHandler,
    categoryForSelect: Category?,
) {
    val finishedProducts = dbHandler.productsHandler.getAll(category = categoryForSelect, isActive = false)

    fun updateProducts() {
        finishedProducts.clear()
        finishedProducts.addAll(dbHandler.productsHandler.getAll(category = categoryForSelect, isActive = false))
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
                    updateProducts()
                },
                onChangeActive = {
                    dbHandler.productsHandler.changeActive(item)
                    updateProducts()
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
    val tabs = listOf(
        stringResource(id = R.string.activeTab),
        stringResource(id = R.string.finishedTab)
    )

    val dbHandler = AidDbHandler(LocalContext.current)

    var tabState by remember { mutableStateOf(0) }
    var categoryState by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AidTopAppBar(
                dbHandler = dbHandler,
                onOpenCategories = onOpenCategories,
                onSelectByCategory = { category ->
                    categoryState = if (category.id == 0) null else category
                },
                onSelectedCategoryItem = { category ->
                    if (categoryState == null && category.id == 0) {
                        true
                    }
                    else if (categoryState != null) {
                        categoryState!!.id == category.id
                    }
                    else {
                        false
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column {
                TabRow(
                    selectedTabIndex = tabState,
                    indicator = { tabPositions ->
                        if (tabState < tabPositions.size) {
                            TabRowDefaults.Indicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[tabState])
                                    .padding(horizontal = 40.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = tabState == index,
                            onClick = { tabState = index },
                            text = { Text(text = title, overflow = TextOverflow.Ellipsis) }
                        )
                    }
                }
                when(tabState) {
                    0 -> ActiveProducts(
                        dbHandler = dbHandler,
                        categoryForSelect = categoryState,
                        onAddProduct = onAddProduct,
                        onEditProduct = onEditProduct
                    )
                    1 -> FinishedProducts(
                        dbHandler = dbHandler,
                        categoryForSelect = categoryState
                    )
                }
            }
        }
    }
}