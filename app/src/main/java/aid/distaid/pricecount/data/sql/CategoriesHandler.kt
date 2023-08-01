package aid.distaid.pricecount.data.sql

import aid.distaid.pricecount.data.models.Category
import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class CategoriesHandler(private val dbHandler: AidDbHandler) {
    fun add(category: Category) {
        val values = ContentValues()

        values.put(NAME_COL, category.name)

        dbHandler.writableDatabase.insert(TABLE_NAME, null, values)
        dbHandler.writableDatabase.close()
    }

    fun update(category: Category) {
        val values = ContentValues()

        values.put(NAME_COL, category.name)

        dbHandler.writableDatabase.update(TABLE_NAME, values, "id=${category.id}", null)
        dbHandler.writableDatabase.close()
    }

    fun delete(category: Category) {
        dbHandler.writableDatabase.delete(TABLE_NAME, "id=${category.id}", null)
        dbHandler.writableDatabase.close()
    }

    @SuppressLint("Range")
    fun getAll(): SnapshotStateList<Category> {
        val cursor = dbHandler.readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val categories = mutableStateListOf<Category>()

        if (cursor.moveToFirst()) {
            do {
                categories.add(
                    Category(
                        id = cursor.getInt(cursor.getColumnIndex(ID_COL)),
                        name = cursor.getString(cursor.getColumnIndex(NAME_COL))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()

        return categories
    }

    @SuppressLint("Range")
    fun getById(id: Int): Category? {
        val cursor = dbHandler.readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME WHERE id=$id", null)
        var category: Category? = null

        if (cursor.moveToFirst()) {
            category = Category(
                id = cursor.getInt(cursor.getColumnIndex(ID_COL)),
                name = cursor.getString(cursor.getColumnIndex(NAME_COL))
            )
        }

        cursor.close()
        return category
    }

    fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE $TABLE_NAME (" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$NAME_COL TEXT NOT NULL);"

        db.execSQL(query)
    }

    fun onUpgrade(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val TABLE_NAME = "categories"
        private const val ID_COL = "id"
        private const val NAME_COL = "name"
    }
}