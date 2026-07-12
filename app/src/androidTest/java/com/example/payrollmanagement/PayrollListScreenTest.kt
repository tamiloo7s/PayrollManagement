package com.example.payrollmanagement

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewModelScope
import com.example.payrollmanagement.domain.model.Employee
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.presentation.view.listview.PayrollListScreen
import com.example.payrollmanagement.presentation.view.listview.PayrollListViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test


class PayrollListScreenTest {

    val repository = FakePayrollRepository()
    val viewModel = PayrollListViewModel(repository)

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun emptyPayroll_displays_Test() {

        composeRule.setContent {
            PayrollListScreen(
                viewModel = viewModel,
                onCreatePayrollClick = {},
                onPayrollClick = {},
                onEditPayrollClick = {}
            )
        }

        composeRule.onNodeWithText("Payroll List").assertExists()
        composeRule.onNodeWithText("No Payroll Records Yet").assertExists()

        viewModel.viewModelScope.launch {
            repository.createPayroll(
                Payroll(
                    id = 0,
                    employees = listOf(
                        Employee(
                            name = "Vijay",
                            wages = 1000.0,
                            isExempt = false
                        )
                    )
                )
            )
        }

        composeRule.onNodeWithText("No Payroll Records Yet").assertDoesNotExist()

        viewModel.viewModelScope.launch {
            repository.deletePayroll(1)
        }

        composeRule.onNodeWithText("No Payroll Records Yet").assertExists()

    }

    @Test
    fun samplePayroll_displays_Test() {

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
                        isExempt = true
                    ),
                )
            ))
        }

        composeRule.setContent {
            PayrollListScreen(
                viewModel = viewModel,
                onCreatePayrollClick = {},
                onPayrollClick = {},
                onEditPayrollClick = {}
            )
        }

        composeRule.waitForIdle()

        composeRule.onNodeWithText("TOTAL NET PAY").assertExists()
        composeRule.onNodeWithText("STAFF").assertExists()
        composeRule.onNodeWithText("TAX").assertExists()
        composeRule.onNodeWithText("Recent payrolls").assertExists()
        composeRule.onNodeWithText("Total Net: $6,000.00").assertExists()
        composeRule.onNodeWithTag("delete_button_1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("confirm_delete").performClick()
        composeRule.onNodeWithText("Total Net: $6,000.00").assertDoesNotExist()

    }


    @Test
    fun Edit_Delete_Payroll_Test() {

        var editedPayrollId: Long? = null

        viewModel.viewModelScope.launch {
            repository.createPayroll(Payroll(
                id = 1,
                employees = listOf(
                    Employee(
                        id = 1,
                        name = "Employee1",
                        wages = 5000.0,
                        isExempt = false
                    ),
                    Employee(
                        id = 2,
                        name = "Employee2",
                        wages = 1000.0,
                        isExempt = true
                    )
                )
            ))
        }

        composeRule.setContent {
            PayrollListScreen(
                viewModel = viewModel,
                onCreatePayrollClick = {},
                onPayrollClick = {},
                onEditPayrollClick = {
                    editedPayrollId = it
                }
            )

        }


        composeRule.waitForIdle()

        composeRule.onNodeWithTag("edit_button_1").performClick()
        composeRule.runOnIdle {
            assertEquals(1L, editedPayrollId)
        }

        composeRule.onNodeWithText("Total Net: $5,750.00").assertExists()

        composeRule.onNodeWithTag("delete_button_1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("confirm_delete").performClick()
        composeRule.onNodeWithText("Total Net: $5,750.00").assertDoesNotExist()


    }

}