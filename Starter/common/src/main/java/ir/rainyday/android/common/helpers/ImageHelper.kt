@file:Suppress("DEPRECATION", "EXTENSION_SHADOWED_BY_MEMBER")

package ir.rainyday.android.common.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.core.content.ContextCompat
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


@SuppressLint("SdCardPath")
object ImageHelper {

    /**
     * Get Image URI from Bitmap
     *
     * @param context
     * @param photo
     * @return
     */

    fun getImageUri(context: Context, photo: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 80, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, photo, "Title", null)
        return Uri.parse(path)
    }


    /**
     * Bitmap from String
     *
     * @param encodedString
     * @return
     */
    fun StringToBitMap(encodedString: String): Bitmap? {
        try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            return null
        }

    }


    /**
     * Get String from Bitmap
     *
     * @param bitmap
     * @return
     */

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


    /**
     * Compress Imgae
     *
     * @param filePath
     * @param height
     * @param width
     * @return
     */


    fun compressImage(filePath: String, height: Float, width: Float): Bitmap? {

        try {
            var scaledBitmap: Bitmap? = null

            val options = BitmapFactory.Options()

            // by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
            // you try the use the bitmap here, you will getInstance null.

            options.inJustDecodeBounds = true
            var bmp = BitmapFactory.decodeFile(filePath, options)

            var actualHeight = options.outHeight
            var actualWidth = options.outWidth

            // max Height and width values of the compressed image is taken as 816x612

            var imgRatio = (actualWidth / actualHeight).toFloat()
            val maxRatio = width / height

            // width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > height || actualWidth > width) {
                if (imgRatio < maxRatio) {
                    imgRatio = height / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = height.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = width / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = width.toInt()
                } else {
                    actualHeight = height.toInt()
                    actualWidth = width.toInt()

                }
            }

            //  setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

            //  inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false

            // this options allow android to claim the bitmap memory if it runs low on memory

            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)

            try {
                //  load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()

            }

            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }

            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f

            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

            val canvas = Canvas(scaledBitmap!!)
            canvas.matrix = scaleMatrix
            canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

            // check the rotation of the image and display it properly

            val exif: ExifInterface

            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: " + orientation)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: " + orientation)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: " + orientation)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: " + orientation)
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix,
                    true)

            return scaledBitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }


    /**
     * ImageSize Calculation
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

    /**
     * Get image from Uri
     *
     * @param uri
     * @param height
     * @param width
     * @return
     */
    fun getImageByUri(uri: Uri, height: Float, width: Float): Bitmap? {
        var bitmap: Bitmap? = null

        try {
            bitmap = compressImage(uri.toString(), height, width)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }


    /**
     * Get Image from the given path
     *
     * @param file_name
     * @param file_path
     * @return
     */

    fun getImageByPath(file_name: String, file_path: String): Bitmap? {

        val path = File(file_path)
        val file = File(path, file_name)

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inSampleSize = 2
        options.inTempStorage = ByteArray(16 * 1024)

        return BitmapFactory.decodeFile(file.getAbsolutePath(), options)
    }

    /**
     * Create an image
     *
     * @param bitmap
     * @param file_name
     * @param filepath
     * @param file_replace
     */


    fun createImage(bitmap: Bitmap, file_name: String, filepath: String, file_replace: Boolean) {

        var path = File(filepath)

        if (!path.exists()) {
            path.mkdirs()
        }

        var file = File(path, file_name)

        if (file.exists()) {
            if (file_replace) {
                file.delete()
                file = File(path, file_name)
                storeImage(file, bitmap)
                Log.i("file", "replaced")
            }
        } else {
            storeImage(file, bitmap)
        }

    }


    /**
     * @param file
     * @param bmp
     */
    @JvmOverloads
    fun storeImage(file: File, bmp: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): Boolean {
        try {
            val out = FileOutputStream(file)
            bmp.compress(format, quality, out)
            out.flush()
            out.close()
            return true

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }


    fun resizeBitmap(photoPath: String, targetW: Int, ignoreSmaller: Boolean) {
        val bitmap = BitmapFactory.decodeFile(photoPath)
        val photoW = bitmap.width

        if (photoW <= targetW && ignoreSmaller)
            return

        val scaleFactor: Double
        scaleFactor = photoW.toDouble() / targetW
        val photoH = (bitmap.height / scaleFactor).toInt()
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetW, photoH, false)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(photoPath)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (out != null) {
                    out.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    @JvmOverloads
    fun generateCircleBitmap(bitmap: Bitmap, rect: Rect = Rect(0, 0, bitmap.width, bitmap.height)): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width,
                bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = Color.RED
        val paint = Paint()

        val rectF = RectF(rect)
        paint.isFilterBitmap = true
        paint.isAntiAlias = true
        paint.flags = Paint.ANTI_ALIAS_FLAG
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        bitmap.recycle()

        return output
    }


    /**
     * convert Bitmap to byte array
     *
     * @param b
     * @return
     */
    fun bitmapToByte(b: Bitmap?): ByteArray? {
        if (b == null) {
            return null
        }

        val o = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.PNG, 100, o)
        return o.toByteArray()
    }

    /**
     * convert byte array to Bitmap
     *
     * @param b
     * @return
     */
    fun byteToBitmap(b: ByteArray?): Bitmap? {
        return if (b == null || b.isEmpty()) null else BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    /**
     * convert Drawable to Bitmap
     *
     * @param d
     * @return
     */
    fun drawableToBitmap(d: Drawable?): Bitmap? {
        return if (d == null) null else (d as? BitmapDrawable)?.bitmap
    }


    fun drawableResourceToBitmap(context: Context, @DrawableRes drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        } else if (drawable is VectorDrawableCompat) {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&  drawable is VectorDrawable) {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        return null
    }


    /**
     * convert Bitmap to Drawable
     *
     * @param b
     * @return
     */
    fun bitmapToDrawable(b: Bitmap?): Drawable? {
        return if (b == null) null else BitmapDrawable(b)
    }

    /**
     * convert Drawable to byte array
     *
     * @param d
     * @return
     */
    fun drawableToByte(d: Drawable): ByteArray? {
        return bitmapToByte(drawableToBitmap(d))
    }

    /**
     * convert byte array to Drawable
     *
     * @param b
     * @return
     */
    fun byteToDrawable(b: ByteArray): Drawable? {
        return bitmapToDrawable(byteToBitmap(b))
    }

    /**
     * getInstance input stream from network by imageurl, you need to close inputStream yourself
     *
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @param requestProperties http request properties
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    @JvmOverloads
    fun getInputStreamFromUrl(imageUrl: String, readTimeOutMillis: Int,
                              requestProperties: Map<String, String>? = null): InputStream? {
        var stream: InputStream? = null
        try {
            val url = URL(imageUrl)
            val con = url.openConnection() as HttpURLConnection
            setURLConnection(requestProperties, con)
            if (readTimeOutMillis > 0) {
                con.readTimeout = readTimeOutMillis
            }
            stream = con.inputStream
        } catch (e: MalformedURLException) {
            throw RuntimeException("MalformedURLException occurred. ", e)
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        }
        finally {
            stream?.close()
        }

        return stream
    }


    /**
     * set HttpURLConnection property
     *
     * @param requestProperties
     * @param urlConnection
     */
    private fun setURLConnection(requestProperties: Map<String, String>?, urlConnection: HttpURLConnection?) {
        if (requestProperties == null || requestProperties.size == 0 || urlConnection == null) {
            return
        }

        for ((key, value) in requestProperties) {
            if (!key.isNullOrEmpty()) {
                urlConnection.setRequestProperty(key, value)
            }
        }
    }

    /**
     * getInstance drawable by imageUrl
     *
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @param requestProperties http request properties
     * @return
     */
    @JvmOverloads
    fun getDrawableFromUrl(imageUrl: String, readTimeOutMillis: Int,
                           requestProperties: Map<String, String>? = null): Drawable {
        val stream = getInputStreamFromUrl(imageUrl, readTimeOutMillis, requestProperties)
        val d = Drawable.createFromStream(stream, "src")
        stream?.close()
        return d
    }

    /**
     * getInstance Bitmap by imageUrl
     *
     * @param imageUrl
     * @param requestProperties http request properties
     * @return
     */
    @JvmOverloads
    fun getBitmapFromUrl(imageUrl: String, readTimeOut: Int, requestProperties: Map<String, String>? = null): Bitmap {
        val stream = getInputStreamFromUrl(imageUrl, readTimeOut, requestProperties)
        val b = BitmapFactory.decodeStream(stream)
        stream?.close()
        return b
    }


    /**
     * scale image
     *
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    fun scaleImageTo(org: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        return scaleImage(org, newWidth.toFloat() / org.width, newHeight.toFloat() / org.height)
    }

    /**
     * scale image
     *
     * @param org
     * @param scaleWidthPercent  sacle of width
     * @param scaleHeightPercent scale of height
     * @return
     */
    fun scaleImage(org: Bitmap?, scaleWidthPercent: Float, scaleHeightPercent: Float): Bitmap? {
        if (org == null) {
            return null
        }

        val matrix = Matrix()
        matrix.postScale(scaleWidthPercent, scaleHeightPercent)
        return Bitmap.createBitmap(org, 0, 0, org.width, org.height, matrix, true)
    }


    val FLIP_VERTICAL = 1
    val FLIP_HORIZONTAL = 2
    fun flipImage(src: Bitmap, type: Int): Bitmap? {
        // create new matrix for transformation
        val matrix = Matrix()
        // if vertical
        if (type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f)
        } else if (type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f)
            // unknown type
        } else {
            return null
        }// if horizonal

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    fun flipImage(@DrawableRes drawableId: Int, type: Int): Bitmap? {
        drawableToBitmap(GlobalAppContext.instance.applicationContext!!.getDrawableCompat(drawableId))?.let {
            return flipImage(it, type)
        }

        return null
    }
}


//******************************* Extensions *********************//
fun Bitmap.getUri(): Uri {
    return ImageHelper.getImageUri(GlobalAppContext.instance.applicationContext!!, this)
}

fun Bitmap.toFormattedString(): String {
    return ImageHelper.BitMapToString(this)
}

fun Bitmap.toDrawable(): Drawable? {
    return ImageHelper.bitmapToDrawable(this)
}

fun Drawable.toBitmap(): Bitmap? {
    return ImageHelper.drawableToBitmap(this)
}


fun Bitmap.save(file: File, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): Boolean {
    return ImageHelper.storeImage(file, this, format, quality)
}

fun Bitmap.generateCircleBitmap(rect: Rect = Rect(0, 0, width, height)): Bitmap {
    return ImageHelper.generateCircleBitmap(this, rect)
}

fun Bitmap.scale(newWidth: Int, newHeight: Int): Bitmap? {
    return ImageHelper.scaleImageTo(this, newWidth, newHeight)
}

fun Bitmap.scale(scaleWidthPercent: Float, scaleHeightPercent: Float): Bitmap? {
    return ImageHelper.scaleImage(this, scaleWidthPercent, scaleHeightPercent)
}

fun ImageView.setImageCompat(drawable: Int, png: Int) {
    if (CurrentDevice.OS.supportVectorDrawable) {
        setImageResource(drawable)
    } else {
        setImageResource(png)
    }
}


fun ImageView.setColoredDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int) {
    val ctx = GlobalAppContext.instance.applicationContext!!
    if (CurrentDevice.OS.supportVectorDrawable) {
        val mDrawable = ctx.getDrawableCompat(drawableId)
        mDrawable?.setColorFilter(ctx.getColorCompat(colorId), PorterDuff.Mode.SRC_ATOP)

        setImageDrawable(mDrawable)
    } else {
        setImageResource(drawableId)
        setColorFilter(ctx.getColorCompat(colorId), PorterDuff.Mode.SRC_ATOP)
    }

}

