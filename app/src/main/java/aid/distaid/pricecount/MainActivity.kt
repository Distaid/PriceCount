package aid.distaid.pricecount

import aid.distaid.pricecount.navigation.NavigationGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import aid.distaid.pricecount.ui.theme.PriceCountTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PriceCountTheme {
                NavigationGraph()
            }
        }
    }
}