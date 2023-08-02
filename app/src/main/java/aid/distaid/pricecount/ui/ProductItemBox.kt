package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import aid.distaid.pricecount.data.models.Product
import aid.distaid.pricecount.format
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ProductItemBox(
    product: Product,
    onEdit: () -> Unit,
    onRemove: () -> Unit,
    onChangeActive: () -> Unit
) {
    var dropDownMenuExpanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                product.image?.let { image ->
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = "userImage",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                    product.category?.let { category ->
                        Text(
                            text = category.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (product.link != null) {
                        Text(
                            text = product.link!!,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = TextDecoration.Underline
                        )
                    } else {
                        Text(text = stringResource(id = R.string.noLink))
                    }
                    if (product.description != null && product.description != "") {
                        Text(
                            text = product.description!!,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
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
                        if (product.isActive) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.edit)) },
                                onClick = {
                                    dropDownMenuExpanded = false
                                    onEdit()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.finish)) },
                                onClick = {
                                    dropDownMenuExpanded = false
                                    onChangeActive()
                                }
                            )
                        }
                        else {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.activate)) },
                                onClick = {
                                    dropDownMenuExpanded = false
                                    onChangeActive()
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.remove), color = Color.Red) },
                            onClick = {
                                dropDownMenuExpanded = false
                                onRemove()
                            }
                        )
                    }
                }
            }
            Divider(thickness = 1.dp, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.productItemCount, product.count, product.price).uppercase(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.productItemPrice, product.sum.format(2)).uppercase(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}