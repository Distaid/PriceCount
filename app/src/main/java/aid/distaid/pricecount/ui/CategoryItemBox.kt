package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import aid.distaid.pricecount.data.models.Category
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItemBox(
    category: Category,
    onEdit: (String) -> Unit,
    onRemove: () -> Unit
) {
    var dropDownMenuExpanded by remember {
        mutableStateOf(false)
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    var cardExpanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = { cardExpanded = !cardExpanded }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = if (cardExpanded) Int.MAX_VALUE else 1
            )
            Column {
                IconButton(onClick = { dropDownMenuExpanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vert_24),
                        contentDescription = "actions"
                    )
                }
                DropdownMenu(
                    expanded = dropDownMenuExpanded,
                    onDismissRequest = {
                        dropDownMenuExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.edit)) },
                        onClick = {
                            dropDownMenuExpanded = false
                            dialogOpen = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.remove), color = Color.Red) },
                        onClick = {
                            dropDownMenuExpanded = false
                            onRemove()
                        }
                    )
                }
                CategoryDialog(
                    open = dialogOpen,
                    defaultText = category.name,
                    textLabel = stringResource(id = R.string.category),
                    dialogLabel = stringResource(id = R.string.categoryEdit),
                    confirmText = stringResource(id = R.string.edit),
                    onConfirm = { value ->
                        onEdit(value)
                    },
                    onClose = { dialogOpen = false }
                )
            }
        }
    }
}