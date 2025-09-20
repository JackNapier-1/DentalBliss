package com.example.dentalbliss.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dentalbliss.ui.screens.AdminDashboard
import com.example.dentalbliss.ui.screens.BookingScreen
import com.example.dentalbliss.ui.screens.LoginScreen
import com.example.dentalbliss.ui.screens.PatientDashboard
import com.example.dentalbliss.ui.screens.PaymentScreen
import com.example.dentalbliss.ui.screens.UserRole

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object AdminDashboard : Screen("admin_dashboard")
    object PatientDashboard : Screen("patient_dashboard")
    object Booking : Screen("booking")
    object Payment : Screen("payment")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    when (role) {
                        UserRole.ADMIN -> navController.navigate(Screen.AdminDashboard.route)
                        UserRole.PATIENT -> navController.navigate(Screen.PatientDashboard.route)
                    }
                },
                onNavigateToAdmin = TODO(),
                onNavigateToPatient = TODO(),
                onNavigateToRegistration = TODO()
            )
        }

        composable(Screen.AdminDashboard.route) {
            AdminDashboard(
                onNavigateToBooking = { navController.navigate(Screen.Booking.route) },
                onNavigateToPayment = { navController.navigate(Screen.Payment.route) },
                onNavigateToAppointments = TODO(),
                onNavigateToPrescriptions = TODO(),
                onNavigateToLoginHistory = TODO(),
                onNavigateToPayments = TODO(),
                onLogout = TODO(),
                appointments = TODO(),
                totalAppointments = TODO(),
                totalSchedules = TODO(),
                totalUsers = TODO()
            )
        }

        composable(Screen.PatientDashboard.route) {
            PatientDashboard(
                onNavigateToBooking = { navController.navigate(Screen.Booking.route) },
                onNavigateToPayment = { navController.navigate(Screen.Payment.route) }
            ) { navController.navigate("login") }
        }

        composable(Screen.Booking.route) {
            BookingScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Payment.route) {
            PaymentScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
} 