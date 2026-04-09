package com.example.agrivault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.agrivault.data.DummyData
import com.example.agrivault.data.TransactionEntity
import com.example.agrivault.ui.theme.AgriVaultTheme
import com.example.agrivault.util.isValidAmount
import com.example.agrivault.util.isValidPastDate
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AgriVaultTheme {
                AgriVaultUI()
            }
        }
    }
}

@Composable
fun AgriVaultUI() {

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val transactions = remember {
        mutableStateListOf<TransactionEntity>().apply {
            addAll(com.example.agrivault.data.DummyData.transactions)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = stringResource(R.string.title_agrivault),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.label_title)) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text(stringResource(R.string.label_amount)) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val selectedDate = LocalDate.now()
                if (!isValidAmount(amount)) {
                    validationError = stringResource(R.string.error_invalid_amount)
                    return@Button
                }
                if (!isValidPastDate(selectedDate)) {
                    validationError = stringResource(R.string.error_future_date_not_allowed)
                    return@Button
                }
                validationError = null

                // Add to list (NO validation yet -> intentional bug)
                transactions.add(
                    TransactionEntity(
                        id = transactions.size, // intentional bug
                        title = title,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        timestamp = System.currentTimeMillis()
                    )
                )

                // not clearing input (intentional bug)

            }) {
                Text(stringResource(R.string.action_log_expense))
            }

            validationError?.let { errorMessage ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.total_balance)}: ₹${transactions.sumOf { it.amount }}",
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(transactions) { txn ->
                    Text(
                        text = "${txn.title} - ₹${txn.amount}",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}