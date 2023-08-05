package aid.distaid.pricecount.data.sql

import aid.distaid.pricecount.toBitmap
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AidDbHandler(
    private val context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    val productsHandler: ProductsHandler = ProductsHandler(this)
    val categoriesHandler: CategoriesHandler = CategoriesHandler(this)

    fun writeImage(image: Bitmap, imageName: String) {
        val imagesPath = File(context.filesDir, "images")
        val imagePath = File(imagesPath, imageName)

        val file = FileOutputStream(imagePath)
        image.compress(
            Bitmap.CompressFormat.WEBP_LOSSY,
            75, file
        )
        file.flush()
        file.close()
    }

    fun readImage(imageName: String): Bitmap {
        val imagesPath = File(context.filesDir, "images")
        val imagePath = File(imagesPath, imageName)

        val file = FileInputStream(imagePath)
        val image = file.readBytes()
        file.close()
        return image.toBitmap()
    }

    fun deleteImage(imageName: String) {
        val imagesPath = File(context.filesDir, "images")
        val imagePath = File(imagesPath, imageName)
        imagePath.delete()
    }

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