package com.example.palmscannermachine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.palmscannermachine.model.AppState
import com.example.palmscannermachine.ui.theme.PalmScannerMachineTheme
import com.example.palmscannermachine.view.NewDataDetectedScreen
import com.example.palmscannermachine.view.PaymentSuccessfulScreen
import com.example.palmscannermachine.view.TransactionSummaryScreen
import com.example.palmscannermachine.view.WaitingScreen
import com.example.palmscannermachine.viewmodel.TransactionViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()

        setContent {
            PalmScannerMachineTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PalmScannerApp()
                }
            }
        }
    }
}

@Composable
fun PalmScannerApp() {
    val viewModel: TransactionViewModel = viewModel()
    val appState by viewModel.appState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (appState) {
                is AppState.WaitingForTransaction -> {
                    WaitingScreen()
                }
                is AppState.NewDataDetected -> {
                    NewDataDetectedScreen((appState as AppState.NewDataDetected).transaction)
                }
                is AppState.PaymentSuccessful -> {
                    PaymentSuccessfulScreen((appState as AppState.PaymentSuccessful).transaction)
                }
                is AppState.ShowSummary -> {
                    TransactionSummaryScreen(
                        transaction = (appState as AppState.ShowSummary).transaction,
                        onReset = { viewModel.resetApp() }
                    )
                }
            }
        }
    }
}