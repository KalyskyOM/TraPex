package com.trapex.features.receiptscanning

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trapex.features.receiptscanning.ReceiptTextRecognizer.ReceiptProcessingException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptScanningViewModel @Inject constructor(
    private val receiptTextRecognizer: ReceiptTextRecognizer
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiptScanningUiState())
    val uiState: StateFlow<ReceiptScanningUiState> = _uiState.asStateFlow()

    fun processReceiptImage(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )
                
                val processedText = receiptTextRecognizer.processReceiptImage(bitmap)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    processedText = processedText
                )
                
            } catch (e: ReceiptProcessingException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to process receipt"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = ReceiptScanningUiState()
    }
}

data class ReceiptScanningUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val processedText: String? = null
)
