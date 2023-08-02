package aid.distaid.pricecount.data.sql

import aid.distaid.pricecount.data.models.Product
import aid.distaid.pricecount.getBoolean
import aid.distaid.pricecount.toBitmap
import aid.distaid.pricecount.toByteArray
import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.database.getBlobOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull

class ProductsHandler(private val dbHandler: AidDbHandler) {
    fun add(product: Product) {
        val values = ContentValues()

        values.put(NAME_COL, product.name)
        values.put(SUM_COL, product.sum)
        values.put(PRICE_COL, product.price)
        values.put(COUNT_COL, product.count)
        values.put(CATEGORY_ID_COL, product.categoryId)
        values.put(IS_ACTIVE_COL, product.isActive)
        values.put(LINK_COL, product.link)
        values.put(DESCRIPTION_COL, product.description)
        values.put(IMAGE_COL, product.image?.toByteArray())

        dbHandler.writableDatabase.insert(TABLE_NAME, null, values)
        dbHandler.writableDatabase.close()
    }

    fun update(product: Product) {
        val values = ContentValues()

        values.put(NAME_COL, product.name)
        values.put(SUM_COL, product.sum)
        values.put(PRICE_COL, product.price)
        values.put(COUNT_COL, product.count)
        values.put(CATEGORY_ID_COL, product.categoryId)
        values.put(IS_ACTIVE_COL, product.isActive)
        values.put(LINK_COL, if (product.link == "") null else product.link)
        values.put(DESCRIPTION_COL, if (product.description == "") null else product.description)
        values.put(IMAGE_COL, product.image?.toByteArray())

        dbHandler.writableDatabase.update(TABLE_NAME, values, "id=${product.id}", null)
        dbHandler.writableDatabase.close()
    }

    fun delete(product: Product) {
        dbHandler.writableDatabase.delete(TABLE_NAME, "id=${product.id}", null)
        dbHandler.writableDatabase.close()
    }

    @SuppressLint("Range")
    fun getAll(category: Category?, isActive: Boolean = true): SnapshotStateList<Product> {
        val query = if (category == null) "SELECT * FROM $TABLE_NAME WHERE isActive=${if (isActive) 1 else 0}"
        else "SELECT * FROM $TABLE_NAME WHERE isActive=${if (isActive) 1 else 0} AND categoryId=${category.id}"

        val cursor = dbHandler.readableDatabase.rawQuery(query, null)
        val products = mutableStateListOf<Product>()

        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    id = cursor.getInt(cursor.getColumnIndex(ID_COL)),
                    name = cursor.getString(cursor.getColumnIndex(NAME_COL)),
                    sum = cursor.getFloat(cursor.getColumnIndex(SUM_COL)),
                    price = cursor.getString(cursor.getColumnIndex(PRICE_COL)),
                    count = cursor.getString(cursor.getColumnIndex(COUNT_COL)),
                    categoryId = cursor.getIntOrNull(cursor.getColumnIndex(CATEGORY_ID_COL)),
                    category = null,
                    isActive = cursor.getBoolean(cursor.getColumnIndex(IS_ACTIVE_COL)),
                    link = cursor.getStringOrNull(cursor.getColumnIndex(LINK_COL)),
                    description = cursor.getStringOrNull(cursor.getColumnIndex(DESCRIPTION_COL)),
                    image = cursor.getBlobOrNull(cursor.getColumnIndex(IMAGE_COL))?.toBitmap()
                )

                product.categoryId?.let {
                    product.category = dbHandler.categoriesHandler.getById(it)
                }

                products.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return products
    }

    @SuppressLint("Range")
    fun getById(id: Int): Product? {
        val cursor = dbHandler.readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME WHERE id=$id", null)
        var product: Product? = null

        if (cursor.moveToFirst()) {
            product = Product(
                id = cursor.getInt(cursor.getColumnIndex(ID_COL)),
                name = cursor.getString(cursor.getColumnIndex(NAME_COL)),
                sum = cursor.getFloat(cursor.getColumnIndex(SUM_COL)),
                price = cursor.getString(cursor.getColumnIndex(PRICE_COL)),
                count = cursor.getString(cursor.getColumnIndex(COUNT_COL)),
                categoryId = cursor.getInt(cursor.getColumnIndex(CATEGORY_ID_COL)),
                category = null,
                isActive = cursor.getBoolean(cursor.getColumnIndex(IS_ACTIVE_COL)),
                link = cursor.getStringOrNull(cursor.getColumnIndex(LINK_COL)),
                description = cursor.getStringOrNull(cursor.getColumnIndex(DESCRIPTION_COL)),
                image = cursor.getBlobOrNull(cursor.getColumnIndex(IMAGE_COL))?.toBitmap()
            )

            product.categoryId?.let {
                product.category = dbHandler.categoriesHandler.getById(it)
            }
        }

        cursor.close()
        return product
    }



    @SuppressLint("Range")
    fun containsCategory(categoryId: Int): Boolean {
        val cursor = dbHandler.readableDatabase.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME WHERE categoryId=$categoryId", null)
        var count = 0

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        return count > 0
    }

    fun changeActive(product: Product) {
        val values = ContentValues()

        values.put(IS_ACTIVE_COL, !product.isActive)

        dbHandler.writableDatabase.update(TABLE_NAME, values, "id=${product.id}", null)
        dbHandler.writableDatabase.close()
    }

    fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE $TABLE_NAME (" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$NAME_COL TEXT NOT NULL," +
                "$SUM_COL REAL NOT NULL," +
                "$PRICE_COL TEXT NOT NULL," +
                "$COUNT_COL TEXT NOT NULL," +
                "$CATEGORY_ID_COL INTEGER," +
                "$IS_ACTIVE_COL INTEGER NOT NULL," +
                "$LINK_COL TEXT," +
                "$DESCRIPTION_COL TEXT," +
                "$IMAGE_COL BLOB);"

        db.execSQL(query)
    }

    fun onUpgrade(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val TABLE_NAME = "products"
        private const val ID_COL = "id"
        private const val NAME_COL = "name"
        private const val SUM_COL = "sum"
        private const val PRICE_COL = "price"
        private const val COUNT_COL = "count"
        private const val CATEGORY_ID_COL = "categoryId"
        private const val IS_ACTIVE_COL = "isActive"
        private const val LINK_COL = "link"
        private const val DESCRIPTION_COL = "description"
        private const val IMAGE_COL = "image"
    }
}