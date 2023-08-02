package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import aid.distaid.pricecount.format
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AidBottomAppBar(
    itemsSum: Float,
    itemsCount: Int,
    onAddButtonClick: () -> Unit
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            OutlinedButton(onClick = onAddButtonClick) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24),
                    contentDescription = "add",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                Text(
                    text = stringResource(id = R.string.addProduct).uppercase(),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.fillMaxWidth().height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.productsSum, itemsSum.format(2)).uppercase(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.productsCount, itemsCount.toString()).uppercase(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}