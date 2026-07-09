package com.example.payrollmanagement

import com.example.payrollmanagement.domain.model.Employee
import com.example.payrollmanagement.domain.model.Payroll
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun Check_TaxCaluclation(){
        val Harish = Employee(
            name = "harish",
            wages = 2000.0,
            isExempt = false
        )

        assertEquals(100.0, Harish.taxes,0.001)
        assertEquals(1900.0,Harish.netWages,0.001)

    }

    @Test
    fun Check_TaxExempt(){
        val Ajay = Employee(
            name = "Ajay",
            wages = 900.0,
            isExempt = false
        )

        assertEquals(0.0,Ajay.taxes,0.001)
        assertNotEquals(10.0,Ajay.taxes,0.001)

        val Krishnan = Employee(
            name = "Krishnan",
            wages = 5000.0,
            isExempt = true
        )

        assertNotEquals(250.0,Krishnan.taxes,0.001)
        assertEquals(5000.0,Krishnan.netWages,0.001)

    }

    @Test
    fun Check_Payroll(){

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
    fun Check_Payroll_Tax(){

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





}
