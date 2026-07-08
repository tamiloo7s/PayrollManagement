package com.example.payrollmanagement

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.payrollmanagement.data.AppDatabase
import com.example.payrollmanagement.data.repository.PayrollRepositoryImpl
import com.example.payrollmanagement.domain.repository.PayrollRepository
import com.example.payrollmanagement.ui.theme.PayrollManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PayrollManagementTheme {
                PayrollNavigation()
            }
        }
    }
}

class PayrollApplication: Application(){
    private val database by lazy { AppDatabase.getInstance(this) }

    val repository: PayrollRepository by lazy {
        PayrollRepositoryImpl(database.payrollDao())
    }
}