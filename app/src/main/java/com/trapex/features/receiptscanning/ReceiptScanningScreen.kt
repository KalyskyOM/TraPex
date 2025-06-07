package com.trapex.features.receiptscanning

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trapex.R
import com.trapex.ui.components.LoadingIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScanningScreen(
    onNavigateBack: () -> Unit,
    onReceiptProcessed: (String) -> Unit,
    viewModel: ReceiptScanningViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Handle side effects
    LaunchedEffect(uiState.processedText) {
        uiState.processedText?.let { text ->
            onReceiptProcessed(text)
            onNavigateBack()
        }
    }
    
    // Show camera preview or loading state
    when {
        uiState.isLoading -> {
            LoadingIndicator("Processing receipt...")
        }
        uiState.error != null -> {
            // Show error state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        else -> {
            // Show camera preview
            CameraPreview(
                onImageCaptured = { bitmap ->
                    scope.launch {
                        viewModel.processReceiptImage(bitmap)
                    }
                },
                onClose = onNavigateBack
            )
        }
    }
}
