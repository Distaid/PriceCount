package aid.distaid.pricecount.data.sql
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AidDbHandler(
    context: Context?
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    val productsHandler: ProductsHandler = ProductsHandler(this)
    val categoriesHandler: CategoriesHandler = CategoriesHandler(this)

    override fun onCreate(db: SQLiteDatabase) {
        productsHandler.onCreate(db)
        categoriesHandler.onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        productsHandler.onUpgrade(db)
        categoriesHandler.onUpgrade(db)
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "productsdb"
        private const val DB_VERSION = 1
    }
}