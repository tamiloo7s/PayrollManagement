package com.example.payrollmanagement.presentation.view.listview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.domain.repository.PayrollRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.jvm.Throws

class PayrollListViewModel(private val repository: PayrollRepository): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val payrolls = repository.getAllPayroll()
        .onEach {
            _isLoading.value = false
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun deletePayroll(id:Long){
        viewModelScope.launch {
            repository.deletePayroll(id)
        }
    }

    class Factory(private val repository: PayrollRepository): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(PayrollListViewModel::class.java))
                return PayrollListViewModel(repository) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}