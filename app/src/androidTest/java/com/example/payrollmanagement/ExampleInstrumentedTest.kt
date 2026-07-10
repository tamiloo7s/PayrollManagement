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