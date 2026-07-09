package com.example.payrollmanagement.data.repository

import com.example.payrollmanagement.data.EmployeeEntity
import com.example.payrollmanagement.data.PayrollDao
import com.example.payrollmanagement.data.payrollEntity
import com.example.payrollmanagement.data.payrollWithEmployee
import com.example.payrollmanagement.domain.model.Employee
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.domain.repository.PayrollRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class PayrollRepositoryImpl(private val payrollDao: PayrollDao): PayrollRepository {
    override fun getAllPayroll(): Flow<List<Payroll>> {
        return payrollDao.getAllPayrolls().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPayrollById(id: Long): Flow<Payroll?> {
        return payrollDao.getPayrollById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun createPayroll(payroll: Payroll): Long {

        val payrollId = payrollDao.insertPayroll(payrollEntity(
            creationDate = payroll.creationDate.time
        ))

        val employeeEntities = payroll.employees.map {
            employee ->
            EmployeeEntity(
                payrollId = payrollId,
                name = employee.name,
                wages = employee.wages,
                isExempt = employee.isExempt
            )
        }

        payrollDao.insertEmployee(employeeEntities)

        return payrollId

    }

    override suspend fun updatePayroll(payroll: Payroll) {
        val payrollEntity = payrollEntity(
            id = payroll.id,
            creationDate = payroll.creationDate.time
        )
        val employeeEntities = payroll.employees.map { employee ->
            EmployeeEntity(
                payrollId = payroll.id,
                name = employee.name,
                wages = employee.wages,
                isExempt = employee.isExempt
            )
        }
        payrollDao.updatePayrollwithEmployee(payrollEntity,employeeEntities)
    }

    override suspend fun deletePayroll(id: Long) {
        payrollDao.deletePayrollById(id)
    }

    private fun payrollWithEmployee.toDomain(): Payroll {
        return Payroll(
            id = payroll.id,
            creationDate = Date(payroll.creationDate),
            employees = employees.map {
                Employee(
                    id = it.id,
                    name = it.name,
                    wages = it.wages,
                    isExempt = it.isExempt
                )
            }
        )
    }

}