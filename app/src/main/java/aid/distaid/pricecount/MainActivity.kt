package aid.distaid.pricecount

import aid.distaid.pricecount.navigation.NavigationGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import aid.distaid.pricecount.ui.theme.PriceCountTheme
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Surface
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainComponent() {
    val navController = rememberAnimatedNavController()

    Surface {
        NavigationGraph(
            navHostController = navController
        )
    }
}