package com.example.dentalbliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dentalbliss.data.model.Appointment
import com.example.dentalbliss.ui.components.DentalBlissTopBar
import com.example.dentalbliss.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    onNavigateToAppointments: () -> Unit,
    onNavigateToPrescriptions: () -> Unit,
    onNavigateToLoginHistory: () -> Unit,
    onNavigateToPayments: () -> Unit,
    onLogout: () -> Unit,
    appointments: List<Appointment> = emptyList(),
    totalAppointments: Int = 0,
    totalSchedules: Int = 0,
    totalUsers: Int = 0,
    onNavigateToBooking: () -> Unit,
    onNavigateToPayment: () -> Unit
) {
    Scaffold(
        topBar = {
            DentalBlissTopBar(
                title = "DentalBliss Dental Care",
                actions = {
                    IconButton(onClick = onLogout) {
                        Text(
                            text = "LOG OUT",
                            color = White,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(DentalBlue)
                .padding(16.dp)
        ) {
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("APPOINTMENTS", totalAppointments, Icons.Default.CalendarToday)
                StatCard("SCHEDULES", totalSchedules, Icons.Default.Schedule)
                StatCard("USERS", totalUsers, Icons.Default.Group)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Menu
            NavigationMenu(
                onNavigateToAppointments = onNavigateToAppointments,
                onNavigateToPrescriptions = onNavigateToPrescriptions,
                onNavigateToLoginHistory = onNavigateToLoginHistory,
                onNavigateToPayments = onNavigateToPayments
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DentalBlue
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun NavigationMenu(
    onNavigateToAppointments: () -> Unit,
    onNavigateToPrescriptions: () -> Unit,
    onNavigateToLoginHistory: () -> Unit,
    onNavigateToPayments: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NavigationItem(
            icon = Icons.Default.Dashboard,
            text = "dashboard",
            onClick = { /* Already on dashboard */ }
        )
        NavigationItem(
            icon = Icons.Default.Add,
            text = "appointments",
            onClick = onNavigateToAppointments
        )
        NavigationItem(
            icon = Icons.Default.MedicalServices,
            text = "prescriptions",
            onClick = onNavigateToPrescriptions
        )
        NavigationItem(
            icon = Icons.Default.History,
            text = "history login",
            onClick = onNavigateToLoginHistory
        )
        NavigationItem(
            icon = Icons.Default.Payment,
            text = "payment",
            onClick = onNavigateToPayments
        )
    }
}

@Composable
private fun NavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) White.copy(alpha = 0.1f) else Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = White
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 