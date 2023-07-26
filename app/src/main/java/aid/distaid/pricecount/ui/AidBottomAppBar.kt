package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import aid.distaid.pricecount.format
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AidBottomAppBar(
    onAddButtonClick: () -> Unit,
    itemsSum: Float,
    itemsCount: Int
) {
    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(topStartPercent = 10, topEndPercent = 10)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 1.dp, end = 0.dp, bottom = 0.dp)
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(topStartPercent = 10, topEndPercent = 10)
                )
                .padding(16.dp)
        ) {
            OutlinedButton(onClick = onAddButtonClick) {
                Box(contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_24),
                            contentDescription = "add"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.addProduct).uppercase(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.fillMaxWidth().height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(id = R.string.productsSum).uppercase()}: ${itemsSum.format(2)}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${stringResource(id = R.string.productsCount).uppercase()}: $itemsCount",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}