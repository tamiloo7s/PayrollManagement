package com.example.payrollmanagement

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.payrollmanagement.domain.model.Employee
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.domain.repository.PayrollRepository
import com.example.payrollmanagement.presentation.view.createview.CreatePayrollScreen
import com.example.payrollmanagement.presentation.view.createview.CreatePayrollViewModel
import com.example.payrollmanagement.presentation.view.detailview.PayrollDetailScreen
import com.example.payrollmanagement.presentation.view.detailview.PayrollDetailViewModel
import com.example.payrollmanagement.presentation.view.listview.PayrollListScreen
import com.example.payrollmanagement.presentation.view.listview.PayrollListViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.payrollmanagement", appContext.packageName)
    }
}

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

}


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

class CreatePayrollScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    val repository = FakePayrollRepository()

    val viewModel = CreatePayrollViewModel(repository)

    @Test
    fun Add_Employee_Test(){

        composeRule.setContent {
            CreatePayrollScreen(viewModel) { }
        }

        composeRule.onNodeWithTag("employee_name_textfield").performTextInput("Harish")
        composeRule.onNodeWithTag("employee_name_textfield").assertTextContains("Harish")

        composeRule.onNodeWithTag("wages_textfield").performTextInput("2000")
        composeRule.onNodeWithTag("wages_textfield").assertTextContains("2000")

        composeRule.onNodeWithTag("switch_button").performClick()

        composeRule.onNodeWithTag("add_employee_button").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Harish").assertExists()
        composeRule.onNodeWithText("EXEMPT").assertExists()

    }

    @Test
    fun Create_Payroll_Test(){

        composeRule.setContent {
            CreatePayrollScreen(viewModel, onBackClick = {})
        }

        composeRule.onNodeWithTag("employee_name_textfield").performTextInput("Employee_1")
        composeRule.onNodeWithTag("wages_textfield").performTextInput("1500")
        composeRule.onNodeWithTag("add_employee_button").performClick()

        composeRule.onNodeWithTag("employee_name_textfield").performTextInput("Employee_2")
        composeRule.onNodeWithTag("wages_textfield").performTextInput("100")
        composeRule.onNodeWithTag("switch_button").performClick()
        composeRule.onNodeWithTag("add_employee_button").performClick()

        composeRule.onNodeWithTag("employee_name_textfield").performTextInput("Employee_3")
        composeRule.onNodeWithTag("wages_textfield").performTextInput("5678")
        composeRule.onNodeWithTag("switch_button").performClick()
        composeRule.onNodeWithTag("add_employee_button").performClick()

        composeRule.onNodeWithTag("submit_button").performClick()

    }

    @Test
    fun Edit_Delete_Employee_Test(){
        composeRule.setContent {
            CreatePayrollScreen(viewModel, onBackClick = {})
        }

        composeRule.onNodeWithTag("employee_name_textfield").performTextInput("Employee_1")
        composeRule.onNodeWithTag("employee_name_textfield").assertTextContains("Employee_1")
        composeRule.onNodeWithTag("wages_textfield").performTextInput("5000")
        composeRule.onNodeWithTag("wages_textfield").assertTextContains("5000")
        composeRule.onNodeWithTag("add_employee_button").performClick()

        composeRule.onNodeWithTag("employee_edit").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("employee_name_textfield").assertTextContains("Employee_1")
        composeRule.onNodeWithTag("wages_textfield").assertTextContains("5000.0")

        composeRule.onNodeWithTag("employee_name_textfield").performTextClearance()
        composeRule.onNodeWithTag("employee_name_textfield").performTextInput("Employee_2")
        composeRule.onNodeWithTag("employee_name_textfield").assertTextContains("Employee_2")
        composeRule.onNodeWithTag("add_employee_button").performClick()

        composeRule.onNodeWithText("Draft Staff List (${viewModel.employee.value.size})").assertExists()

        composeRule.onNodeWithTag("employee_delete").performClick()
        composeRule.onNodeWithText("Draft Staff List (${viewModel.employee.value.size})").assertDoesNotExist()

    }



}


class FakePayrollRepository : PayrollRepository {

    private val payrolls = MutableStateFlow<List<Payroll>>(emptyList())
    private var nextId = 1L

    override fun getAllPayroll(): Flow<List<Payroll>> = payrolls

    override fun getPayrollById(id: Long): Flow<Payroll?> {
        return payrolls.map { list ->
            list.find { it.id == id }
        }
    }

    override suspend fun createPayroll(payroll: Payroll): Long {
        val id = if (payroll.id == 0L) nextId++ else payroll.id

        payrolls.value = payrolls.value + payroll.copy(id = id)

        return id
    }

    override suspend fun updatePayroll(payroll: Payroll) {
        payrolls.value = payrolls.value.map {
            if (it.id == payroll.id) payroll else it
        }
    }

    override suspend fun deletePayroll(id: Long) {
        payrolls.value = payrolls.value.filterNot {
            it.id == id
        }
    }
}