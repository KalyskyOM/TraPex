package com.trapex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.trapex.features.receiptscanning.ReceiptScanningScreen
import com.trapex.features.receiptscanning.ReceiptScanningViewModel
import com.trapex.ui.screens.DashboardScreen

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object ReceiptScanning : Screen("receipt_scanning")
    
    companion object {
        const val KEY_PROCESSED_TEXT = "processed_text"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Dashboard.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onScanReceiptClick = {
                    navController.navigate(Screen.ReceiptScanning.route)
                }
            )
        }
        
        composable(Screen.ReceiptScanning.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(Screen.Dashboard.route)
            }
            val viewModel = hiltViewModel<ReceiptScanningViewModel>(it)
            
            ReceiptScanningScreen(
                onNavigateBack = { navController.popBackStack() },
                onReceiptProcessed = { text ->
                    // Navigate back with result
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(KEY_PROCESSED_TEXT, text)
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }
}
