package com.example.payrollmanagement.presentation.view.listview

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.presentation.view.detailview.toCurrency
import com.example.payrollmanagement.presentation.view.detailview.toFormattedString
import com.example.payrollmanagement.ui.theme.IndigoLight
import com.example.payrollmanagement.ui.theme.Purple40
import com.example.payrollmanagement.ui.theme.Purple80
import com.example.payrollmanagement.ui.theme.RoseBg
import com.example.payrollmanagement.ui.theme.RoseBorder
import com.example.payrollmanagement.ui.theme.RoseText

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollListScreen(
    viewModel : PayrollListViewModel = hiltViewModel(),
    onCreatePayrollClick: () -> Unit,
    onPayrollClick: (Long) -> Unit,
    onEditPayrollClick: (Long) -> Unit,
) {


    val isLoading by viewModel.isLoading.collectAsState()
    val payrolls by viewModel.payrolls.collectAsState()
    var payrollToDelete by remember { mutableStateOf<Payroll?>(null) }
    var exitPopup by remember { mutableStateOf(false) }

    val activity = LocalContext.current as? Activity


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(
                            Icons.Default.Calculate,
                            contentDescription = "",
                            tint = Color.Black,
                            modifier = Modifier.padding(end = 10.dp).size(40.dp)
                        )
                        Text(
                            text = "Payroll List",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreatePayrollClick,
                containerColor = Purple40,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "payroll_icon",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        containerColor = Color.White,
        content = { innerpadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding)
            ) {
                if(isLoading){

                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center){
                        CircularProgressIndicator(
                            color = Purple40
                        )
                    }

                }
                else if (payrolls.isEmpty()) {
                    EmptyPayrollView(
                        onCreateClick = onCreatePayrollClick
                    )
                }
                else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            HeaderCard(payrolls)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Recent payrolls",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(
                            items = payrolls,
                            key = { it.id }
                        ) { payrolls ->
                            PayrollItemRow(
                                payroll = payrolls,
                                onClick = { onPayrollClick(payrolls.id) },
                                onEditClick = { onEditPayrollClick(payrolls.id) },
                                onDeleteClick = { payrollToDelete = payrolls },
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    )

    payrollToDelete?.let { payroll ->
        AlertDialog(
            onDismissRequest = { payrollToDelete = null },
            title = { Text(text = "Delete Payroll",
                color = Color.Black,
                fontWeight = FontWeight.Bold) },
            text = {
                Text(text = "Are you sure you want to delete this payroll",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePayroll(payroll.id)
                        payrollToDelete = null
                    },
                    modifier = Modifier.testTag("confirm_delete")
                ) {
                    Text("Delete", color = RoseText)
                }
            },
            dismissButton = {
                TextButton(onClick = { payrollToDelete = null }) {
                    Text("Cancel", color = Color.Black)
                }
            },
            containerColor = Color.White
        )
    }

    if (exitPopup) {
        AlertDialog(
            onDismissRequest = { exitPopup = false },
            title = {
                Text(
                    text = "Exit",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Are you sure you want to Exit the app",
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                activity?.finish()
                                activity?.finishAffinity()
                            },
                            modifier = Modifier.testTag("exit_button")
                        ) {
                            Text(
                                text = "Yes",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        TextButton(
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                Color.Black
                            ),
                            onClick = { exitPopup = false }) {
                            Text(
                                text = "No",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                }
            },
            containerColor = Color.White,
            confirmButton = {

            }
        )
    }

    BackHandler {
        exitPopup = true
    }

}


@Composable
private fun HeaderCard(
    payrolls: List<Payroll>
) {

    val totalPayout = payrolls.sumOf { it.totalNet }
    val totalEmployees = payrolls.sumOf { it.employees.size }
    val totalTaxes = payrolls.sumOf { it.totalTaxes }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Purple40
            ),
            border = BorderStroke(1.dp,Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        text = "TOTAL NET PAY",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Column {
                    Text(
                        text = totalPayout.toCurrency(),
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.4F),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(width = 1.dp,color = Color.DarkGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${totalEmployees}",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "STAFF",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = RoseBg
                ),
                border = BorderStroke(1.dp,RoseText),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = RoseText,
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${totalTaxes.toCurrency()}",
                        color = RoseText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "TAX",
                        color = RoseText.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PayrollItemRow(
    payroll: Payroll,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp,Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = "employee_count",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(20.dp)
                    )
                    Text(
                        text = payroll.creationDate.toFormattedString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.People,
                        contentDescription = "employee_count",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(20.dp)
                    )

                    Text(
                        text = "${payroll.employees.size}",
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.MonetizationOn,
                        contentDescription = "employee_count",
                        tint = Purple40,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(20.dp)
                    )
                    Text(
                        text = "Total Net: ${payroll.totalNet.toCurrency()}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Purple40,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.testTag("edit_button_${payroll.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Payroll",
                    tint = Color.Black
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.testTag("delete_button_${payroll.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete Payroll",
                    tint = RoseText
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}


@Composable
fun EmptyPayrollView(
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.3F),
            modifier = Modifier.size(96.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Payroll Records Yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextButton(
            onClick = onCreateClick,
            modifier = Modifier.testTag("empty_state_create_button"),
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(
                text = "+ Create First Payroll",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}