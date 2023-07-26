package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AidTopAppBar(
    onCategories: () -> Unit
) {
    var dropDownMenuExpanded by remember {
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
                DropdownMenuItem(onClick = onCategories) {
                    Text(text = "Категории")
                }
            }
        },
        backgroundColor = MaterialTheme.colorScheme.secondary
    )
}