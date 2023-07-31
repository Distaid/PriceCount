package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AidBackTopAppBar(
    @StringRes titleFromResource: Int,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = titleFromResource).uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_24),
                    contentDescription = "back"
                )
            }
        }
    )
}