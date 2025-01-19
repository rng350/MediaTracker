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
        imagePath: String?,
        subfolder: String
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            if (imagePath!=null) {
                Log.d("SaveImageUseCase", "1 ImagePath - $imagePath, Subfolder - $subfolder")
                val subfolderDir = File(context.filesDir, subfolder)
                if (!subfolderDir.exists()) {
                    subfolderDir.mkdirs()
                }

                val filename = imagePath.split("/").last()
                Log.d("SaveImageUseCase", "1.1 Filename: $filename")

                val newFile = File(subfolderDir, filename)
                if (newFile.exists()) {
                    Log.d("SaveImageUseCase", "1.5: file already exists...")
                    return@withContext FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", newFile)
                }
                Log.d("SaveImageUseCase", "2 - NewFilePath: ${newFile.path}")
                Log.d("SaveImageUseCase", "2.05 - URL... ${Constants.TMDB_IMAGE_BASE_URL + filename}")

                val request = ImageRequest.Builder(context)
                    .data(Constants.TMDB_IMAGE_BASE_URL + filename)
                    .allowHardware(false)
                    .build()
                Log.d("SaveImageUseCase", "3 for $imagePath")
                val requestResult = imageLoader.execute(request)
                Log.d("SaveImageUseCase", "3.01 for $imagePath - imageLoader.execute(request) executed...")

                when (requestResult) {
                    is ErrorResult -> {
                        Log.d("SaveImageUseCase", "Failed to load. Message: ${requestResult.throwable.message}")
                        Log.d("SaveImageUseCase", "3.05.... failll")
                        return@withContext null
                    }
                    is SuccessResult -> {
                        Log.d("SaveImageUseCase", "3.1 - SuccessFesult - ImageSize: ${requestResult.image.shareable}")
                        (requestResult.image as? BitmapDrawable)?.let { bitmapDrawable ->
                            Log.d("SaveImageUseCase", "3.2")
                            FileOutputStream(newFile).use { outputStream ->
                                Log.d("SaveImageUseCase", "3.3")
                                bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                Log.d("SaveImageUseCase", "3.4")
                            }
                            Log.d("SaveImageUseCase", "4")
                            return@withContext FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", newFile)
                        }
                    }
                }
                Log.d("SaveImageUseCase", "4.5")
                return@withContext null
            }
            else {
                Log.d("SaveImageUseCase", "4.75")
                return@withContext null
            }
        }
        catch(e: CancellationException) {
            Log.e("SaveImageUseCase", "CancellationException (imagepath: $imagePath) -- Job cancelled while saving image. MESSAGE: ${e.message ?: "No message"}")
            throw e
        }
        catch(e: FileNotFoundException) {
            Log.e("SaveImageUseCase", "FileNotFoundException (imagepath: $imagePath): ${e.message ?: "No message"}")
            return@withContext null
        }
        catch(e: IOException) {
            Log.e("SaveImageUseCase", "IOException (imagepath: $imagePath): ${e.message ?: "No message"}")
            return@withContext null
        }
        catch(e: Exception) {
            Log.e("SaveImageUseCase", "Exception (imagepath: $imagePath): ${e.message ?: "No message"}")
            return@withContext null
        }
    }
}