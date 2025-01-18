package com.rng350.mediatracker.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SaveImageFromURLUseCase @Inject constructor(
    private val context: Context
) {
    suspend operator fun invoke(imagePath: String): Uri? = withContext(Dispatchers.IO) {
        val filename = imagePath.split("/").last()

        // TODO: Check if file already exists. If so, return Uri right away

        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(Constants.IMAGE_BASE_URL + imagePath)
            .allowHardware(false)
            .build()

        (imageLoader.execute(request) as? SuccessResult)?.let { result ->
            (result.image as? BitmapImage)?.let { bitmapDrawable ->
                val file = File(context.filesDir, filename)
                FileOutputStream(file).use { outputStream ->
                    bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                return@withContext FileProvider.getUriForFile(context, "", file)
            }
        }
        return@withContext null
    }
}