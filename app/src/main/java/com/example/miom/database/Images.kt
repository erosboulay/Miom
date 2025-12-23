package com.example.miom.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class Images {

    companion object {

        fun cropToSquare(bitmap: Bitmap): Bitmap {
            val size = minOf(bitmap.width, bitmap.height)

            val xOffset = (bitmap.width - size) / 2
            val yOffset = (bitmap.height - size) / 2

            return Bitmap.createBitmap(
                bitmap,
                xOffset,
                yOffset,
                size,
                size
            )
        }


        fun getRecipeDirectory(context: Context): File {
            val recipeDir = File(context.filesDir, "Recipe")
            if (!recipeDir.exists()) {
                recipeDir.mkdirs()
            }
            return recipeDir
        }


        private fun generateUUIDFileName(): String {
            return "recipe_${UUID.randomUUID()}.jpg"
        }

        fun saveImageToInternalStorage(
            context: Context,
            imageUri: Uri?
        ): String? {
            if (imageUri == null) return null

            return try {
                val recipeDir = getRecipeDirectory(context)
                val name = generateUUIDFileName()

                val imageFile = File(recipeDir, name)

                val inputStream = context.contentResolver.openInputStream(imageUri)
                    ?: return null

                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                val squareBitmap = cropToSquare(originalBitmap)

                FileOutputStream(imageFile).use { output ->
                    squareBitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        90, // quality
                        output
                    )
                }

                name
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }





    }


}