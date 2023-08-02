package aid.distaid.pricecount.ui.screens

import aid.distaid.pricecount.R
import aid.distaid.pricecount.data.models.Category
import aid.distaid.pricecount.data.sql.AidDbHandler
import aid.distaid.pricecount.ui.AidBackTopAppBar
import aid.distaid.pricecount.ui.CategoryDialog
import aid.distaid.pricecount.ui.CategoryItemBox
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun CategoriesScreen(
    onBack: () -> Unit
) {
    val dbHandler = AidDbHandler(LocalContext.current)

    val categories = dbHandler.categoriesHandler.getAll()

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    fun updateCategories() {
        categories.clear()
        categories.addAll(dbHandler.categoriesHandler.getAll())
    }

    Scaffold(
        topBar = { AidBackTopAppBar(
            titleFromResource = R.string.categories,
            onBack = onBack,
            actions = {
                IconButton(
                    onClick = { dialogOpen = true }
                ) {
                    Icon(painter = painterResource(id = R.drawable.add_24), contentDescription = "addCategory")
                }
            }
        )}
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {
            items(
                items = categories,
                key = { item -> item.id }
            ) {
                CategoryItemBox(
                    category = it,
                    onEdit = { value ->
                        dbHandler.categoriesHandler.update(it.copy(name = value))
                        updateCategories()
                    },
                    onRemove = {
                        if (!dbHandler.productsHandler.containsCategory(it.id)) {
                            dbHandler.categoriesHandler.delete(it)
                            updateCategories()
                        }
                    }
                )
            }
        }
        CategoryDialog(
            open = dialogOpen,
            defaultText = "",
            textLabel = stringResource(id = R.string.category),
            dialogLabel = stringResource(id = R.string.categoryAdd),
            confirmText = stringResource(id = R.string.add),
            onConfirm = { value ->
                dbHandler.categoriesHandler.add(Category(0, value))
                categories.clear()
                categories.addAll(dbHandler.categoriesHandler.getAll())
            },
            onClose = { dialogOpen = false }
        )
    }
}