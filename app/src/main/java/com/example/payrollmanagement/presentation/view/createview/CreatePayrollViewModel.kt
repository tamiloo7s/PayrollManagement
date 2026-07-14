package com.example.payrollmanagement.presentation.view.createview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.payrollmanagement.domain.model.Employee
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.domain.repository.PayrollRepository
import com.example.payrollmanagement.presentation.view.listview.PayrollListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CreatePayrollViewModel @Inject constructor(
    val repository: PayrollRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val payrollId =
        savedStateHandle.get<Long>("payrollId") ?: -1L

    val isEditmode = payrollId != -1L
    private val _existingPayroll = MutableStateFlow<Payroll?>(null)
    private val _employee = MutableStateFlow<List<Employee>>(emptyList())
    val employee: StateFlow<List<Employee>> = _employee.asStateFlow()

    val employeeName = MutableStateFlow("")
    val employeeWages = MutableStateFlow("")

    val editingEmployeeIndex = MutableStateFlow<Int?>(null)
    val employeeIsExempt = MutableStateFlow(false)

    val nameError = MutableStateFlow<String?>(null)
    val wagesError = MutableStateFlow<String?>(null)

    val _savesuccess = MutableSharedFlow<Boolean>()
    val savesuccess : SharedFlow<Boolean> = _savesuccess.asSharedFlow()

    init {
        if(isEditmode){
            viewModelScope.launch {
                repository.getPayrollById(payrollId).collect{ payroll ->
                    if(payroll != null && _existingPayroll.value == null){
                        _existingPayroll.value = payroll
                        _employee.value = payroll.employees
                    }

                }
            }
        }
    }

    fun startEditingEmployee(index:Int){
        val list = _employee.value
        if(index in list.indices){
            val employee = list[index]
            editingEmployeeIndex.value = index
            employeeName.value = employee.name
            employeeWages.value = employee.wages.toInt().toString()
            employeeIsExempt.value = employee.isExempt

            nameError.value = null
            wagesError.value = null
        }
    }

    fun cancelEditingEmployee(){
        editingEmployeeIndex.value = null
        employeeName.value = ""
        employeeWages.value = ""
        employeeIsExempt.value = false
        nameError.value = null
        wagesError.value = null
    }


    fun addEmployee() {
        val name = employeeName.value.trim()
        val wagesStr = employeeWages.value.trim()
        val isExempt = employeeIsExempt.value

        var hasError = false
        if(name.isEmpty()){
            nameError.value = "Name cannot be empty"
            hasError = true
        }
        else {
            nameError.value = null
        }

        val wages = wagesStr.toDoubleOrNull()
        if(wages == null || wages <= 0.0) {
            wagesError.value = "Invalid wages"
            hasError = true
        }
        else {
            wagesError.value  = null
        }

        if(hasError) return

        val index = editingEmployeeIndex.value
        if(index != null){
            val currentList = _employee.value.toMutableList()

            if(index in currentList.indices){
                val existing = currentList[index]
                currentList[index] = existing.copy(
                    name = name,
                    wages = wages!!,
                    isExempt = isExempt
                )
                _employee.value = currentList
            }
            editingEmployeeIndex.value = null
        }
        else {
            val newEmployee = Employee(
                name = name,
                wages = wages!!,
                isExempt = isExempt
            )
            _employee.value = _employee.value + newEmployee
        }

        employeeName.value = ""
        employeeWages.value = ""
        employeeIsExempt.value = false
    }


    fun removeEmployee(index:Int){
        val currentList = _employee.value.toMutableList()
        if(index in currentList.indices){
            currentList.removeAt(index)
            _employee.value = currentList

            if(editingEmployeeIndex.value == index){
                cancelEditingEmployee()
            }
            else if(editingEmployeeIndex.value != null && editingEmployeeIndex.value!! > index){
                editingEmployeeIndex.value = editingEmployeeIndex.value!! -1
            }
        }
    }

    fun savePayroll(){
        if(_employee.value.isEmpty()) return

        viewModelScope.launch {
            if(isEditmode) {
                val existing = _existingPayroll.value
                val updatedPayroll = Payroll(
                    id = payrollId,
                    creationDate = existing?.creationDate ?: Date(),
                    employees = _employee.value
                )
                repository.updatePayroll(updatedPayroll)
            }
            else {
                val newPayroll = Payroll(
                    creationDate = Date(),
                    employees = _employee.value
                )
                repository.createPayroll(newPayroll)
            }
            _savesuccess.emit(true)

        }
    }

}