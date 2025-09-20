package com.example.dentalbliss.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dentalbliss.ui.screens.*
import com.example.dentalbliss.data.AppointmentRepository
import com.example.dentalbliss.data.AppointmentData
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { role ->
                    when (role) {
                        UserRole.ADMIN -> navController.navigate("admin_dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                        UserRole.PATIENT -> navController.navigate("patient_dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onNavigateToAdmin = { 
                    navController.navigate("admin_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToPatient = { 
                    navController.navigate("patient_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegistration = { navController.navigate("registration") }
            )
        }
        
        composable("registration") {
            RegistrationScreen(
                onNavigateToLogin = { navController.navigateUp() },
                onRegister = { fullName, phoneNumber, email, password ->
                    navController.navigate("login") {
                        popUpTo("registration") { inclusive = true }
                    }
                }
            )
        }

        composable("admin_dashboard") {
            AdminDashboard(
                onLogout = { 
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToAppointments = { navController.navigate("appointments") },
                onNavigateToPrescriptions = { navController.navigate("prescriptions") },
                onNavigateToLoginHistory = { navController.navigate("login_history") },
                onNavigateToPayments = { navController.navigate("payments") },
                appointments = emptyList(),
                totalAppointments = AppointmentRepository.appointments.size,
                totalSchedules = 0,
                totalUsers = 0,
                onNavigateToBooking = { navController.navigate("booking") },
                onNavigateToPayment = { navController.navigate("payment") }
            )
        }

        composable("patient_dashboard") {
            PatientDashboard(
                onLogout = { 
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToBooking = { navController.navigate("booking") },
                onNavigateToPayment = { navController.navigate("payment") }
            )
        }

        // Add missing screens as placeholders
        composable("appointments") { AppointmentsScreen() }
        composable("prescriptions") { PlaceholderScreen("Prescriptions") }
        composable("login_history") { PlaceholderScreen("Login History") }
        composable("payments") { PlaceholderScreen("Payments") }
        composable("booking") { BookingScreen(onNavigateBack = { navController.popBackStack() }) }
        composable("payment") { PaymentScreen(onNavigateBack = { navController.popBackStack() }) }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "$title screen coming soon", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun AppointmentsScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "All Appointments", style = MaterialTheme.typography.headlineMedium)
                if (AppointmentRepository.appointments.isEmpty()) {
                    Text("No appointments yet.")
                } else {
                    AppointmentRepository.appointments.forEach {
                        Text("${it.patientName}: ${it.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))} at ${it.time.format(DateTimeFormatter.ofPattern("hh:mm a"))} - ${it.service}")
                    }
                }
            }
        }
    }
} 