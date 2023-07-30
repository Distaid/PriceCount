package aid.distaid.pricecount

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun Float.format(digits: Int) = "%.${digits}f".format(this).replace(',', '.')

fun Bitmap.toByteArray(): ByteArray = ByteArrayOutputStream().apply {
    compress(Bitmap.CompressFormat.PNG, 0, this)
}.toByteArray()

fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)

fun Cursor.getBoolean(columnIndex: Int): Boolean = getInt(columnIndex) > 0