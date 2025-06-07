package com.trapex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.trapex.navigation.NavGraph
import com.trapex.ui.theme.TraPexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TraPeXApp()
        }
    }
}

@Composable
fun TraPeXApp() {
    val navController = rememberNavController()
    
    TraPexTheme {
        NavGraph(navController = navController)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TraPeXAppPreview() {
    TraPeXApp()
}