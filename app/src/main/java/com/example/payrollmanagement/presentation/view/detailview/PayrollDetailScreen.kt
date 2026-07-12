package com.example.payrollmanagement.presentation.view.detailview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payrollmanagement.domain.model.Employee
import com.example.payrollmanagement.domain.model.Payroll
import com.example.payrollmanagement.ui.theme.IndigoLight
import com.example.payrollmanagement.ui.theme.Purple40
import com.example.payrollmanagement.ui.theme.RoseBg
import com.example.payrollmanagement.ui.theme.RoseBorder
import com.example.payrollmanagement.ui.theme.RoseText
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollDetailScreen(
    viewModel: PayrollDetailViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val payrollState by viewModel.payroll.collectAsState()

    Scaffold(
        modifier = modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Payroll Details",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("back_button")
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val payroll = payrollState
            if (payroll == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Purple40)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Fetching payroll details...", color = Color.Black)
                }
            }
            else {
                Column(modifier = Modifier.fillMaxSize()) {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            HeaderInfoCard(payroll = payroll)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Staff List",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        items(payroll.employees){ employee ->
                            EmployeeDetailRow(
                                employee
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }


                    }

                    PayrollBottomSheet(
                        totalWages = payroll.totalWages,
                        totalTaxes = payroll.totalTaxes,
                        totalNet = payroll.totalNet
                    )
                }
            }
        }
    }

}


@Composable
private fun EmployeeDetailRow(
    employee: Employee,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp,Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1.1f)
            ) {
                val (bgCol, fgCol) = getInitialsColors(employee.name)

                val taxlable = when {
                    employee.isExempt -> "EXEMPT"
                    employee.taxes > 0.0 -> "5% TAX"
                    else -> "NO TAX"

                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Surface(
                        shape = CircleShape,
                        color = bgCol,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "PersonIcon",
                                tint = fgCol,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = RoseBg
                    ) {
                        Text(
                            text = "${taxlable}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = employee.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Wages: ${employee.wages.toCurrency()}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Tax: ${employee.taxes.toCurrency()}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(0.9f)
            ) {
                Text(
                    text = "NET PAY",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${employee.netWages.toCurrency()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )

            }
        }
    }
}

@Composable
private fun HeaderInfoCard(
    payroll: Payroll
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)){

            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = "employee_count",
                tint = Color.DarkGray,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(25.dp)
            )

            Text(
                text = "${payroll.creationDate.toFormattedString()}",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier
            )
        }
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
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(25.dp).testTag("payment_icon")
                    )
                    Text(
                        text = "TOTAL NET PAY",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Column {
                    Text(
                        text = payroll.totalNet.toCurrency(),
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
                border = BorderStroke(1.dp,Color.DarkGray),
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
                        modifier = Modifier.size(25.dp).testTag("people_icon")
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${payroll.employees.size}",
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
                        modifier = Modifier.size(25.dp).testTag("wallet_icon")
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${payroll.totalTaxes.toCurrency()}",
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
private fun PayrollBottomSheet(
    totalWages: Double,
    totalTaxes: Double,
    totalNet: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "PAYROLL SUMMARY",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.Gray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Wages",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = totalWages.toCurrency(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
            }

            if (totalTaxes > 0.0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Taxes",
                        fontSize = 14.sp,
                        color = RoseText
                    )
                    Text(
                        text = "- ${totalTaxes.toCurrency()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
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
                    text = "Total Net",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple40
                )
                Text(
                    text = totalNet.toCurrency(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Purple40
                )
            }
        }
    }
}

fun Double.toCurrency(): String {
    return try {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        formatter.format(this)
    } catch (e: Exception) {
        "$${String.format("%,.2f", this)}"
    }
}

fun Date.toFormattedString(): String {
    return try {
        val sdf = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        sdf.format(this)
    } catch (e: Exception) {
        this.toString()
    }
}

private fun getInitialsColors(name: String): Pair<Color, Color> {
    val hash = kotlin.math.abs(name.hashCode()) % 3
    return when (hash) {
        0 -> Color(0xFFFFEDD5) to Color(0xFFC2410C)
        1 -> Color(0xFFDBEAFE) to Color(0xFF1D4ED8)
        else -> Color(0xFFCCFBF1) to Color(0xFF0F766E)
    }
}