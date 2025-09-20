package com.example.dentalbliss.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dentalbliss.ui.components.ActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDashboard(
    onNavigateToBooking: () -> Unit,
    onNavigateToPayment: () -> Unit,
    onLogout: () -> Unit
) {
    var showNotImplemented by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Patient Dashboard") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
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
            // Upcoming Appointment
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Next Appointment",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Dental Cleaning",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Tomorrow, 10:00 AM",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(onClick = { showNotImplemented = "Reschedule" }) {
                            Icon(Icons.Default.Edit, contentDescription = "Reschedule")
                        }
                    }
                }
            }

            // Action Buttons
            ActionButton(
                text = "Book Appointment",
                icon = Icons.Default.Event,
                onClick = onNavigateToBooking
            )
            ActionButton(
                text = "Make Payment",
                icon = Icons.Default.Payment,
                onClick = onNavigateToPayment
            )
            ActionButton(
                text = "View Medical History",
                icon = Icons.Default.History,
                onClick = { showNotImplemented = "Medical History" }
            )
            ActionButton(
                text = "Contact Support",
                icon = Icons.Default.Help,
                onClick = { showNotImplemented = "Support" }
            )
        }
        if (showNotImplemented != null) {
            AlertDialog(
                onDismissRequest = { showNotImplemented = null },
                title = { Text("Not Implemented") },
                text = { Text("The ${showNotImplemented} feature is not implemented yet.") },
                confirmButton = {
                    TextButton(onClick = { showNotImplemented = null }) {
                        Text("OK")
                    }
                }
            )
        }
    }
} 