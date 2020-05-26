package com.example.clinicenrolment.common

import android.content.Context
import android.content.ContextWrapper
import android.graphics.*
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.media.ExifInterface
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.net.Uri
import java.io.FileNotFoundException
import android.provider.MediaStore

import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory




class Utility {




    companion object {

        //var DOWNLOADED_FILESPATH_CAMERA = activity!!.getExternalFilesDir("ClinicApp/Camera")!!.absoluteFile


        fun saveToInternalStorage(context: Context, filePath: String?, location: String): File? {

            var path: String? = null
            var mypath: File? = null
            try {
                if (filePath != null) {
                    val image = File(filePath)
                    val bmOptions = BitmapFactory.Options()
                    var bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions)
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true)

                    val cw = ContextWrapper(context)
                    // path to /data/data/yourapp/app_data/imageDir

                    //File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    // Create imageDir


                    val createDir = File(location + File.separator)
                    if (!createDir.exists()) {
                        createDir.mkdirs()
                    }
                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                        .format(Date())
                    mypath = File(location, timeStamp + ".jpg")

                    path = mypath!!.getAbsolutePath()

                    var fos: FileOutputStream? = null
                    try {
                        fos = FileOutputStream(mypath)
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        try {
                            fos!!.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                } else {
                    Log.e("Error in Capture", "Captured Failed")
                }
            } catch (e: Exception) {
                Log.e("Error in Capture", "" + e)
            }

            return mypath
        }


        fun compressImage(context: Context, imageUri: String): String? {

            val filePath = getRealPathFromURI(context, imageUri)
            var scaledBitmap: Bitmap? = null

            val options = BitmapFactory.Options()

            //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
            //      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true

            var bmp = BitmapFactory.decodeFile(filePath, options)

            var actualHeight = options.outHeight
            var actualWidth = options.outWidth

            //      max Height and width values of the compressed image is taken as 816x612

            val maxHeight = 816.0f
            val maxWidth = 612.0f
            var imgRatio = (actualWidth / actualHeight).toFloat()
            val maxRatio = maxWidth / maxHeight

            //      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()

                }
            }

            //      setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

            //      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false

            //      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)

            try {
                //          load the bitmap from its path
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
            canvas.setMatrix(scaleMatrix)
            canvas.drawBitmap(
                bmp, middleX - bmp.width / 2, middleY - bmp.height / 2,
                Paint(FILTER_BITMAP_FLAG)
            )

            //      check the rotation of the image and display it properly
            val exif: ExifInterface
            try {
                exif = ExifInterface(filePath)

                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0
                )
                Log.e("EXIF", "Exif: $orientation")
                val matrix = Matrix()
                if (orientation == 6) {
                    matrix.postRotate(90F)
                    Log.e("EXIF", "Exif: $orientation")
                } else if (orientation == 3) {
                    matrix.postRotate(180F)
                    Log.e("EXIF", "Exif: $orientation")
                } else if (orientation == 8) {
                    matrix.postRotate(270F)
                    Log.e("EXIF", "Exif: $orientation")
                }
                scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap!!, 0, 0,
                    scaledBitmap!!.width, scaledBitmap.height, matrix,
                    true
                )
            } catch (e: IOException) {
                Log.e("Error in", "" + e)
            }

            var out: FileOutputStream? = null
            val filename :String?=null
            try {
                out = FileOutputStream(filename)

                //          write the compressed bitmap at the destination specified by filename.
                scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

            } catch (e: FileNotFoundException) {
                Log.e("Error in", "" + e)
            }

            return filename

        }



        fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int,
            reqHeight: Int
        ): Int {
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

       /* fun getFilename(context:Context): String {
            val file = File(DOWNLOADED_FILESPATH_CAMERA)
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png"

        }*/



        fun getRealPathFromURI(context: Context, contentURI: String): String? {
            val contentUri = Uri.parse(contentURI)
            val cursor = context.contentResolver.query(contentUri, null, null, null, null)
            if (cursor == null) {
                return contentUri.getPath()
            } else {
                cursor.moveToFirst()
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                return cursor.getString(index)
            }
        }

    }










}