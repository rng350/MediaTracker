package com.rng350.mediatracker.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveImageFromURLUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageLoader: ImageLoader
) {
    suspend operator fun invoke(
        imageUrl: String,
        subfolder: String
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val subfolderDir = File(context.filesDir, subfolder)
            if (!subfolderDir.exists()) {
                subfolderDir.mkdirs()
            }

            val filename = imageUrl.split("/").last()

            val newFile = File(subfolderDir, filename)
            if (newFile.exists()) {
                return@withContext FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", newFile)
            }

            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            val requestResult = imageLoader.execute(request)

            when (requestResult) {
                is ErrorResult -> {
                    return@withContext null
                }
                is SuccessResult -> {
                    (requestResult.image as? BitmapImage)?.let { bitmapDrawable ->
                        FileOutputStream(newFile).use { outputStream ->
                            bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        }
                        return@withContext FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", newFile)
                    }
                }
            }
            return@withContext null
        }
        catch(e: CancellationException) {
            Log.e("SaveImageUseCase", "CancellationException (imagepath: $imageUrl) -- Job cancelled while saving image. MESSAGE: ${e.message ?: "No message"}")
            throw e
        }
        catch(e: FileNotFoundException) {
            Log.e("SaveImageUseCase", "FileNotFoundException (imagepath: $imageUrl): ${e.message ?: "No message"}")
            return@withContext null
        }
        catch(e: IOException) {
            Log.e("SaveImageUseCase", "IOException (imagepath: $imageUrl): ${e.message ?: "No message"}")
            return@withContext null
        }
        catch(e: Exception) {
            Log.e("SaveImageUseCase", "Exception (imagepath: $imageUrl): ${e.message ?: "No message"}")
            return@withContext null
        }
    }
}