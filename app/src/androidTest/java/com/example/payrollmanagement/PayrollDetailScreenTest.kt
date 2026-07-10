package com.example.payrollmanagement

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.viewModelScope
import com.example.payrollmanagement.domain.model.Employee
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.presentation.view.detailview.PayrollDetailScreen
import com.example.payrollmanagement.presentation.view.detailview.PayrollDetailViewModel
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test

class PayrollDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    val repository = FakePayrollRepository()
    val viewModel = PayrollDetailViewModel(repository,1)


    @Test
    fun Payroll_Details_Test(){

        viewModel.viewModelScope.launch {
            repository.createPayroll(Payroll(
                id = 1,
                employees = listOf(
                    Employee(
                        id = 1,
                        name = "Employee1",
                        wages = 1000.0,
                        isExempt = false
                    ),
                    Employee(
                        id = 2,
                        name = "Employee2",
                        wages = 2000.0,
                        isExempt = true
                    ),
                    Employee(
                        id = 3,
                        name = "Employee3",
                        wages = 3000.0,
                        isExempt = false
                    ),
                )
            ))
        }

        composeRule.setContent {
            PayrollDetailScreen(
                viewModel,
                onBackClick = {}
            )
        }

        composeRule.onNodeWithText("Payroll Details").assertExists()
        composeRule.onNodeWithText("TOTAL NET PAY").assertExists()
        composeRule.onNodeWithTag("payment_icon").assertExists()
        composeRule.onNodeWithText("STAFF").assertExists()
        composeRule.onNodeWithTag("people_icon").assertExists()
        composeRule.onNodeWithText("TAX").assertExists()
        composeRule.onNodeWithTag("wallet_icon").assertExists()
        composeRule.onNodeWithText("Staff List").assertExists()
        composeRule.onNodeWithText("NO TAX").assertExists()
        composeRule.onNodeWithText("EXEMPT").assertExists()
        composeRule.onNodeWithText("5% TAX").assertExists()
        composeRule.onNodeWithText("PAYROLL SUMMARY").assertExists()
        composeRule.onNodeWithText("Total Wages").assertExists()
        composeRule.onNodeWithText("Total Taxes").assertExists()
        composeRule.onNodeWithText("Total Net").assertExists()

    }
}
