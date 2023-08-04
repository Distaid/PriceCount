package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import aid.distaid.pricecount.data.models.Category
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDialog(
    open: Boolean,
    dialogLabel: String,
    items: MutableList<Category>,
    onSelectedItem: ((Category) -> Boolean)? = null,
    onConfirm: (Category) -> Unit,
    onClose: () -> Unit
) {
    if (open) {
        Dialog(
            onDismissRequest = {
                onClose()
            }
        ) {
            Surface(
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (dialogLabel != "") {
                        Text(text = dialogLabel,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Divider(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 4.dp)
                        )
                    }
                    LazyColumn(
                        modifier = Modifier.requiredHeightIn(0.dp, 500.dp)
                    ) {
                        items(
                            items = items,
                            key = { item -> item.id }
                        ) { category ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    onConfirm(category)
                                    onClose()
                                }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = category.name,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(start = 4.dp).weight(1f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (onSelectedItem != null) {
                                        if (onSelectedItem(category)) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.check_24),
                                                contentDescription = "selected"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            onClose()
                        }) {
                            Text(text = stringResource(id = R.string.cancel).uppercase())
                        }
                    }
                }
            }
        }
    }
}