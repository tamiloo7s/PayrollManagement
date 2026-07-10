package com.example.payrollmanagement

import com.example.payrollmanagement.domain.model.Employee
import com.example.payrollmanagement.domain.model.Payroll
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


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

class PayrollUnitTest {

    @Test
    fun `tax and net pay calculation`(){

        val company1 = Payroll(
            employees = listOf(
                Employee(
                    name = "Employee_1",
                    wages = 1000.0,
                    isExempt = true
                ),
                Employee(
                    name = "Employee_2",
                    wages = 2000.0,
                    isExempt = false
                ),
                Employee(
                    name = "Employee_3",
                    wages = 4000.0,
                    isExempt = true
                ),
                Employee(
                    name = "Employee_4",
                    wages = 8000.0,
                    isExempt = false
                )
            )
        )

        assertEquals(500.0,company1.totalTaxes,0.001)
        assertEquals(14500.0,company1.totalNet,0.001)
        assertEquals(15000.0,company1.totalWages,0.001)

    }

    @Test
    fun `maximum wages calculation`(){

        val company1 = Payroll(
            employees = listOf(
                Employee(
                    name = "Employee_1",
                    wages = 9876.0,
                    isExempt = false
                ),
                Employee(
                    name = "Employee_2",
                    wages = 5678944365.0,
                    isExempt = false
                )
            )
        )

        assertEquals(283947712.05,company1.totalTaxes,0.001)

    }

    @Test
    fun `calculate payroll totals`() {

        val employees = listOf(
            Employee(
                name = "Employee_1",
                wages = 900.0,
                isExempt = false
            ),
            Employee(
                name = "Employee_2",
                wages = 1900.0,
                isExempt = true
            ),
            Employee(
                name = "Employee_3",
                wages = 2000.0,
                isExempt = false
            )
        )

        val payroll = Payroll(
            creationDate = Date(),
            employees = employees
        )

        assertEquals(4800.0, payroll.totalWages, 0.001)
        assertEquals(100.0, payroll.totalTaxes, 0.001)
        assertEquals(4700.0, payroll.totalNet, 0.001)
    }

    @Test
    fun `empty payroll check`() {

        val payroll = Payroll(
            employees = emptyList()
        )

        assertEquals(0.0, payroll.totalWages, 0.001)
        assertEquals(0.0, payroll.totalTaxes, 0.001)
        assertEquals(0.0, payroll.totalNet, 0.001)
    }

    @Test
    fun `all employee has tax exempt`() {

        val payroll = Payroll(
            employees = listOf(
                Employee(
                    name = "employee_1",
                    wages = 3000.0,
                    isExempt = true
                ),
                Employee(
                    name = "employee_2",
                    wages = 4000.0,
                    isExempt = true
                )
            )
        )

        assertEquals(7000.0, payroll.totalWages, 0.001)
        assertEquals(0.0, payroll.totalTaxes, 0.001)
        assertEquals(7000.0, payroll.totalNet, 0.001)
    }

}


