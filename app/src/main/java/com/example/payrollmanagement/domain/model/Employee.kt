package com.example.payrollmanagement.domain.model

data class Employee(
    val id: Long = 0,
    val name: String,
    val wages: Double,
    val isExempt: Boolean
) {
    val taxes: Double
        get() = if (wages > 1000.0 && !isExempt) wages * 0.05 else 0.0

    val netWages: Double
        get() = wages - taxes
}
