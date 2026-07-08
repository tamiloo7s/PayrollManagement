package com.example.payrollmanagement.presentation.view.listview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.domain.repository.PayrollRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PayrollListViewModel(private val repository: PayrollRepository): ViewModel() {

    val payrolls: StateFlow<List<Payroll>> = repository.getAllPayroll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deletePayroll(id:Long){
        viewModelScope.launch {
            repository.deletePayroll(id)
        }
    }

    class Factory(private val repository: PayrollRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PayrollListViewModel::class.java)) {
                return PayrollListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}