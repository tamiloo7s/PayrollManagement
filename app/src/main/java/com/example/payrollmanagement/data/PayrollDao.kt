package com.example.payrollmanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import javax.crypto.ExemptionMechanismSpi

@Dao
interface PayrollDao {

    @Transaction
    @Query("SELECT * FROM payrolls ORDER BY creationDate DESC")
    fun getAllPayrolls(): Flow<List<payrollWithEmployee>>

    @Transaction
    @Query("SELECT * FROM payrolls WHERE id = :id")
    fun getPayrollById(id:Long): Flow<payrollWithEmployee?>

    @Query("DELETE FROM payrolls WHERE id = :id")
    fun deletePayrollById(id:Long)

    @Query("DELETE FROM employees WHERE payrollId = :payrollId")
    fun deleteEmployeesByPayrollId(payrollId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayroll(payroll: payrollEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employees:List<EmployeeEntity>)

    @Transaction
    suspend fun updatePayrollwithEmployee(payroll: payrollEntity,employees: List<EmployeeEntity>){
        insertPayroll(payroll)
        deleteEmployeesByPayrollId(payroll.id)
        insertEmployee(employees)
    }

}