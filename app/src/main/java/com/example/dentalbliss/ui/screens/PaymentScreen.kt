package com.example.dentalbliss.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onNavigateBack: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("100.00") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Payment") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Payment Amount
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Payment Amount",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$$amount",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            // Card Details
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { 
                    if (it.length <= 16) cardNumber = it.filter { char -> char.isDigit() }
                },
                label = { Text(text = "Card Number") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.CreditCard, contentDescription = null)
                }
            )

            OutlinedTextField(
                value = cardHolder,
                onValueChange = { cardHolder = it },
                label = { Text(text = "Card Holder Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { 
                        if (it.length <= 5) expiryDate = it
                    },
                    label = { Text(text = "MM/YY") },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = "MM/YY") }
                )

                OutlinedTextField(
                    value = cvv,
                    onValueChange = { 
                        if (it.length <= 3) cvv = it.filter { char -> char.isDigit() }
                    },
                    label = { Text(text = "CVV") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Payment Button
            Button(
                onClick = { /* TODO: Implement payment */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = cardNumber.length == 16 && 
                         cardHolder.isNotBlank() && 
                         expiryDate.length == 5 && 
                         cvv.length == 3
            ) {
                Text(text = "Pay Now")
            }
        }
    }
} 