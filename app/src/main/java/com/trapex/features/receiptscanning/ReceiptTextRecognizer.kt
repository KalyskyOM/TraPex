package com.trapex.features.receiptscanning

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptTextRecognizer @Inject constructor() {
    
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    suspend fun processReceiptImage(bitmap: Bitmap): String {
        val image = InputImage.fromBitmap(bitmap, 0)
        return try {
            val result = textRecognizer.process(image).await()
            result.text
        } catch (e: Exception) {
            throw ReceiptProcessingException("Failed to process receipt image", e)
        }
    }
    
    fun compressBitmap(bitmap: Bitmap, quality: Int = 80): ByteArray {
        return ByteArrayOutputStream().apply {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, this)
        }.toByteArray()
    }
}

class ReceiptProcessingException(message: String, cause: Throwable? = null) : 
    Exception(message, cause)
