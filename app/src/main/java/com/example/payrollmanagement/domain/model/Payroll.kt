package com.example.payrollmanagement.domain.model

import java.util.Date

data class Payroll(
    val id: Long = 0,
    val creationDate: Date = Date(),
    val employees: List<Employee> = emptyList()
) {
    val totalWages: Double
        get() = employees.sumOf { it.wages }

    val totalTaxes: Double
        get() = employees.sumOf { it.taxes }

    val totalNet: Double
        get() = employees.sumOf { it.netWages }
}
