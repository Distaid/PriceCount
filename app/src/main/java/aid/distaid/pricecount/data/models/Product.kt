package aid.distaid.pricecount.data.models

import android.graphics.Bitmap

data class Product(
    var id: Int,
    var name: String,
    var sum: Float,
    var price: String,
    var count: String,
    var categoryId: Int?,
    var category: Category?,
    var isActive: Boolean,
    var link: String?,
    var description: String?,
    var image: Bitmap?
)

fun createEmptyProduct(): Product {
    return Product(0, "", 0f, "", "1", null, null, true, null, null, null)
}
