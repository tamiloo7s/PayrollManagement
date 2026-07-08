package com.example.payrollmanagement.domain.repository

import com.example.payrollmanagement.data.payrollWithEmployee
import com.example.payrollmanagement.domain.model.Payroll
import kotlinx.coroutines.flow.Flow

interface PayrollRepository {

    fun getAllPayroll(): Flow<List<Payroll>>

    fun getPayrollById(id:Long): Flow<Payroll?>

    suspend fun createPayroll(payroll: Payroll): Long

    suspend fun updatePayroll(payroll: Payroll)

    suspend fun deletePayroll(id:Long)

}