package com.example.payrollmanagement.presentation.view.detailview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.domain.repository.PayrollRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PayrollDetailViewModel @Inject constructor(
    private val repository: PayrollRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {


    private val payrollId =
        savedStateHandle.get<Long>("payrollId") ?: -1L

    val payroll: StateFlow<Payroll?> = repository.getPayrollById(payrollId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

}