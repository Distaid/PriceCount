package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AidBackTopAppBar(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title.uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_24),
                    contentDescription = "back"
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun AidBackTopAppBar(
    titleFromResource: Int,
    onBackClick: () -> Unit
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
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_24),
                    contentDescription = "back"
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.secondary
    )
}