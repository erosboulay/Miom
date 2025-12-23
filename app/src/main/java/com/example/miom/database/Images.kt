package com.example.miom.database

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class Images {

    companion object {

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

                context.contentResolver.openInputStream(imageUri)?.use { input ->
                    FileOutputStream(imageFile).use { output ->
                        input.copyTo(output)
                    }
                }
                name
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }




    }


}