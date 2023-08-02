package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import aid.distaid.pricecount.data.models.Category
import aid.distaid.pricecount.data.sql.AidDbHandler
import android.annotation.SuppressLint
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AidTopAppBar(
    dbHandler: AidDbHandler,
    onOpenCategories: () -> Unit,
    onSelectByCategory: (Category) -> Unit,
    onSelectedCategoryItem: (Category) -> Boolean
) {
    var dropDownMenuExpanded by remember {
        mutableStateOf(false)
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.appName).uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = {
                dialogOpen = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.category_24),
                    contentDescription = "category"
                )
            }
            IconButton(onClick = {
                dropDownMenuExpanded = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.more_vert_24),
                    contentDescription = "menu"
                )
            }
            DropdownMenu(
                expanded = dropDownMenuExpanded,
                onDismissRequest = {
                    dropDownMenuExpanded = false
                }
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.categories)) },
                    onClick = onOpenCategories
                )
            }
            SelectDialog(
                open = dialogOpen,
                dialogLabel = "Выберите категорию",
                items = mutableStateListOf(Category(0, "Все категории")).apply {
                    addAll(dbHandler.categoriesHandler.getAll())
                },
                onSelectedItem = onSelectedCategoryItem,
                onConfirm = onSelectByCategory,
                onClose = { dialogOpen = false }
            )
        }
    )
}