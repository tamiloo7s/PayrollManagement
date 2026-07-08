package com.example.payrollmanagement.presentation.view.detailview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.domain.repository.PayrollRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PayrollDetailViewModel(
    private val repository: PayrollRepository,
    private val payrollId:Long
): ViewModel() {

    val payroll: StateFlow<Payroll?> = repository.getPayrollById(payrollId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    class Factory(
        private val repository: PayrollRepository,
        private val payrollId:Long
    ): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(PayrollDetailViewModel::class.java)){
                return PayrollDetailViewModel(repository,payrollId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}