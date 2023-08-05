package aid.distaid.pricecount

import aid.distaid.pricecount.navigation.NavigationGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import aid.distaid.pricecount.ui.theme.PriceCountTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imagesPath = File(filesDir, "images")
        imagesPath.mkdir()

        setContent {
            PriceCountTheme {
                NavigationGraph()
            }
        }
    }
}