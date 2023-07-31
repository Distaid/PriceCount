package aid.distaid.pricecount.ui.screens

import aid.distaid.pricecount.R
import aid.distaid.pricecount.ui.AidBackTopAppBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun CategoriesScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { AidBackTopAppBar(
            titleFromResource = R.string.categories,
            onBack = onBack,
            actions = {
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(painter = painterResource(id = R.drawable.add_24), contentDescription = "addCategory")
                }
            }
        )}
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

        }
    }
}