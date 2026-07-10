package com.example.payrollmanagement

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.payrollmanagement.presentation.view.createview.CreatePayrollScreen
import com.example.payrollmanagement.presentation.view.createview.CreatePayrollViewModel
import org.junit.Rule
import org.junit.Test

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
