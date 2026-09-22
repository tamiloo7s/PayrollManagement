package com.example.payrollmanagement.domain.usecase

import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.domain.repository.PayrollRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class createUsecase @Inject constructor(private val repo: PayrollRepository){

    suspend operator fun invoke(payroll: Payroll): Long{
        return repo.createPayroll(payroll)
    }
}

class updateUsecase @Inject constructor(private val repo: PayrollRepository){

    suspend operator fun invoke(payroll: Payroll){
        repo.updatePayroll(payroll)
    }
}

class getPayrollByIdUsecase @Inject constructor(private val repo: PayrollRepository){

    operator fun invoke(id:Long):Flow<Payroll?>{
        return repo.getPayrollById(id)
    }
}