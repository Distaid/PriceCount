package aid.distaid.pricecount.data.sql

import aid.distaid.pricecount.data.models.Product
import aid.distaid.pricecount.toBitmap
import aid.distaid.pricecount.toByteArray
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.database.getBlobOrNull
import androidx.core.database.getStringOrNull

class AidDbHandler(
    context: Context?
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    fun addProduct(product: Product) {
        val values = ContentValues()

        values.put(NAME_COL, product.name)
        values.put(SUM_COL, product.sum)
        values.put(PRICE_COL, product.price)
        values.put(COUNT_COL, product.count)
        values.put(LINK_COL, product.link)
        values.put(DESCRIPTION_COL, product.description)
        values.put(IMAGE_COL, product.image?.toByteArray())

        writableDatabase.insert(TABLE_NAME, null, values)
        writableDatabase.close()
    }

    fun updateProduct(product: Product) {
        val values = ContentValues()

        values.put(NAME_COL, product.name)
        values.put(SUM_COL, product.sum)
        values.put(PRICE_COL, product.price)
        values.put(COUNT_COL, product.count)
        values.put(LINK_COL, if (product.link == "") null else product.link)
        values.put(DESCRIPTION_COL, if (product.description == "") null else product.description)
        values.put(IMAGE_COL, product.image?.toByteArray())

        writableDatabase.update(TABLE_NAME, values, "id=${product.id}", null)
        writableDatabase.close()
    }

    fun deleteProduct(product: Product) {
        writableDatabase.delete(TABLE_NAME, "id=${product.id}", null)
        writableDatabase.close()
    }

    @SuppressLint("Recycle", "Range")
    fun getAllProducts(): SnapshotStateList<Product> {
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val products = mutableStateListOf<Product>()

        if (cursor.moveToFirst()) {
            do {
                products.add(
                    Product(
                        id = cursor.getInt(cursor.getColumnIndex(ID_COL)),
                        name = cursor.getString(cursor.getColumnIndex(NAME_COL)),
                        sum = cursor.getFloat(cursor.getColumnIndex(SUM_COL)),
                        price = cursor.getString(cursor.getColumnIndex(PRICE_COL)),
                        count = cursor.getString(cursor.getColumnIndex(COUNT_COL)),
                        link = cursor.getStringOrNull(cursor.getColumnIndex(LINK_COL)),
                        description = cursor.getStringOrNull(cursor.getColumnIndex(DESCRIPTION_COL)),
                        image = cursor.getBlobOrNull(cursor.getColumnIndex(IMAGE_COL))?.toBitmap(),
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()

        return products
    }

    @SuppressLint("Recycle", "Range")
    fun getProductById(id: Int): Product? {
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME WHERE id=$id", null)

        if (cursor.moveToFirst()) {
            val product = Product(
                id = cursor.getInt(cursor.getColumnIndex(ID_COL)),
                name = cursor.getString(cursor.getColumnIndex(NAME_COL)),
                sum = cursor.getFloat(cursor.getColumnIndex(SUM_COL)),
                price = cursor.getString(cursor.getColumnIndex(PRICE_COL)),
                count = cursor.getString(cursor.getColumnIndex(COUNT_COL)),
                link = cursor.getStringOrNull(cursor.getColumnIndex(LINK_COL)),
                description = cursor.getStringOrNull(cursor.getColumnIndex(DESCRIPTION_COL)),
                image = cursor.getBlobOrNull(cursor.getColumnIndex(IMAGE_COL))?.toBitmap(),
            )
            cursor.close()
            return product
        }
        cursor.close()
        return null
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE $TABLE_NAME (" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$NAME_COL TEXT NOT NULL," +
                "$SUM_COL REAL NOT NULL," +
                "$PRICE_COL TEXT NOT NULL," +
                "$COUNT_COL TEXT NOT NULL," +
                "$LINK_COL TEXT," +
                "$DESCRIPTION_COL TEXT," +
                "$IMAGE_COL BLOB);"

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "productsdb"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "products"
        private const val ID_COL = "id"
        private const val NAME_COL = "name"
        private const val SUM_COL = "sum"
        private const val PRICE_COL = "price"
        private const val COUNT_COL = "count"
        private const val LINK_COL = "link"
        private const val DESCRIPTION_COL = "description"
        private const val IMAGE_COL = "image"
    }
}