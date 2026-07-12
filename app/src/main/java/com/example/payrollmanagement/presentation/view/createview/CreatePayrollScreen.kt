package com.example.payrollmanagement.presentation.view.createview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payrollmanagement.domain.model.Employee
import com.example.payrollmanagement.presentation.view.detailview.toCurrency
import com.example.payrollmanagement.ui.theme.Purple40
import com.example.payrollmanagement.ui.theme.RoseBg
import com.example.payrollmanagement.ui.theme.RoseText
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePayrollScreen(
    viewModel: CreatePayrollViewModel,
    onBackClick: () -> Unit
) {

    val name by viewModel.employeeName.collectAsState()
    val wages by viewModel.employeeWages.collectAsState()
    val isExempt by viewModel.employeeIsExempt.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val wagesError by viewModel.wagesError.collectAsState()

    val focusManager = LocalFocusManager.current

    val employees by viewModel.employee.collectAsState()
    val editingIndex by viewModel.editingEmployeeIndex.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.savesuccess.collectLatest { succes ->
            if(succes){
                onBackClick()
            }
        }
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (viewModel.isEditmode) "Edit Payroll Batch" else "New Payroll Batch",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = if (editingIndex != null) "Edit Employee Details" else "Add Employee",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )

                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    if(it.length < 30) {
                                        viewModel.employeeName.value = it
                                    }
                                                },
                                label = { Text("Full Name") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null
                                    )
                                },
                                isError = nameError != null,
                                supportingText = {
                                    nameError?.let {
                                        Text(
                                            it,
                                            color = RoseText
                                        )
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Purple40,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedLabelColor = Color.Black,
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("employee_name_textfield")
                            )

                            OutlinedTextField(
                                value = wages,
                                onValueChange = {
                                    if(it.length < 8) {
                                        viewModel.employeeWages.value = it
                                    }
                                                },
                                label = { Text("Base Wages") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AttachMoney,
                                        contentDescription = null
                                    )
                                },
                                isError = wagesError != null,
                                supportingText = {
                                    wagesError?.let {
                                        Text(
                                            it,
                                            color = RoseText
                                        )
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Purple40,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedLabelColor = Color.Black,
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("wages_textfield")
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Tax Exempt Staff",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }

                                Switch(
                                    checked = isExempt,
                                    onCheckedChange = { viewModel.employeeIsExempt.value = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Purple40,
                                        checkedTrackColor = Color.Gray.copy(
                                            alpha = 0.3f
                                        )
                                    ),
                                    modifier = Modifier.testTag("switch_button")
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (editingIndex != null) {
                                    Button(
                                        onClick = {
                                            focusManager.clearFocus()
                                            viewModel.cancelEditingEmployee()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = RoseBg,
                                            contentColor = RoseText
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .weight(0.5f)
                                            .height(48.dp)
                                    ) {
                                        Text("Cancel", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    }
                                }

                                Button(
                                    onClick = {
                                        focusManager.clearFocus()
                                        viewModel.addEmployee()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Purple40,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("add_employee_button")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = if (editingIndex != null) Icons.Default.CheckCircle else Icons.Default.Add,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (editingIndex != null) "Update Employee" else "Add Employee",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (employees.isNotEmpty()) {
                    item {
                        Text(
                            text = "Draft Staff List (${employees.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }
                }

                itemsIndexed(employees){ index, item ->
                    DraftEmployeeItem(
                        employee = item,
                        onEdit = {
                            viewModel.startEditingEmployee(index)
                        },
                        onDelete = {
                            viewModel.removeEmployee(index)
                        },
                        isEditing = editingIndex == index
                    )
                }

                item {
                    Spacer(Modifier.height(10.dp))
                }
            }
            BottomActionBoard(
                employee = employees,
                onSaveClick = { viewModel.savePayroll() },
                isEditMode = viewModel.isEditmode
            )
        }
    }
}



@Composable
private fun DraftEmployeeItem(
    employee: Employee,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    isEditing: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isEditing) Purple40 else Color.LightGray),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Surface(
                    shape = CircleShape,
                    color = if (isEditing) Color.DarkGray else Color.LightGray,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "PersonIcon",
                            tint = if (isEditing) Color.White else Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (employee.isExempt) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = "EXEMPT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = employee.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Column(
                ) {
                    Text(
                        text = "Wages: ${employee.wages.toCurrency()}",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    if (employee.taxes > 0.0) {
                        Text(
                            text = "Tax: ${employee.taxes.toCurrency()}",
                            fontSize = 12.sp,
                            color = RoseText
                        )
                    }
                    Text(
                        text = "Net: ${employee.netWages.toCurrency()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Purple40
                    )
                }
            }

            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(36.dp).testTag("employee_edit")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Draft Employee",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp).testTag("employee_delete")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete Draft Employee",
                    tint = RoseText,
                    modifier = Modifier.size(18.dp).testTag("employee_delete")
                )
            }
        }
    }
}

@Composable
fun BottomActionBoard(
    employee: List<Employee>,
    onSaveClick: () -> Unit,
    isEditMode: Boolean = false
) {

    val totalWages = employee.sumOf { it.wages }
    val totalTaxes = employee.sumOf { it.taxes }
    val totalNet = employee.sumOf { it.netWages }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp,Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Calculation Summary",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.LightGray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Gross Pay",
                    fontSize = 13.sp,
                    color = Color.Black
                )
                Text(
                    text = "${totalWages.toCurrency()}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            if (totalTaxes > 0.0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Tax",
                        fontSize = 13.sp,
                        color = RoseText
                    )
                    Text(
                        text = "- ${totalTaxes.toCurrency()}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = RoseText
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Net Pay",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple40
                )
                Text(
                    text = totalNet.toCurrency(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Purple40
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onSaveClick,
                enabled = employee.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEditMode) "Update Payroll" else "Submit Payroll",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}