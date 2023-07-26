package aid.distaid.pricecount

import aid.distaid.pricecount.navigation.NavigationGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import aid.distaid.pricecount.ui.theme.PriceCountTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PriceCountTheme {
                MainComponent()
            }
        }
    }
}

@Composable
fun MainComponent() {
    val navController = rememberNavController()

    Surface {
        NavigationGraph(
            navHostController = navController
        )
    }
}