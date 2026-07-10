package com.example.payrollmanagement

import com.example.payrollmanagement.domain.model.Employee
import org.junit.Assert.assertEquals
import org.junit.Test

class EmployeeUnitTest {

    @Test
    fun `zero tax and wages should be below 1000`(){

        val employee_1 = Employee(
            name = "employee_1",
            wages = 900.0,
            isExempt = false
        )

        assertEquals(0.0,employee_1.taxes,0.00)

    }

    @Test
    fun `wages above 1000 and check 5% tax`(){

        val employee_1 = Employee(
            name = "employee_1",
            wages = 1900.0,
            isExempt = false
        )

        assertEquals(95.0,employee_1.taxes,0.00)
        assertEquals(1805.0,employee_1.netWages,0.0)

    }

    @Test
    fun `zero tax and wages equal to 1000`(){

        val employee_1 = Employee(
            name = "employee_1",
            wages = 1000.0,
            isExempt = false
        )

        assertEquals(0.0,employee_1.taxes,0.00)

    }

    @Test
    fun `zero tax for enable exempt for employee`(){

        val employee_1 = Employee(
            name = "employee_1",
            wages = 5555.0,
            isExempt = true
        )

        assertEquals(0.0,employee_1.taxes,0.00)
        assertEquals(5555.0,employee_1.netWages,0.00)

    }

    @Test
    fun `zero tax and zero wages`(){
        val employee_1 = Employee(
            name = "employee_1",
            wages = 0.0,
            isExempt = false
        )

        assertEquals(0.0,employee_1.taxes,0.00)

    }





}
