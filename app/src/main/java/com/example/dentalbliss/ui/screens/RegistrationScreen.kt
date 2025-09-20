package com.example.dentalbliss.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.dentalbliss.data.User
import com.example.dentalbliss.data.UserRepository
import com.example.dentalbliss.ui.components.DentalBlissTopBar
import com.example.dentalbliss.ui.theme.DeclinedRed
import com.example.dentalbliss.ui.theme.DentalBlue
import com.example.dentalbliss.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onNavigateToLogin: () -> Unit,
    onRegister: (String, String, String, String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    fun validateAndRegister() {
        when {
            fullName.isEmpty() -> {
                isError = true
                errorMessage = "Please enter your full name"
            }
            phoneNumber.isEmpty() -> {
                isError = true
                errorMessage = "Please enter your phone number"
            }
            !phoneNumber.matches(Regex("^[0-9]{10,}$")) -> {
                isError = true
                errorMessage = "Please enter a valid phone number"
            }
            email.isEmpty() -> {
                isError = true
                errorMessage = "Please enter your email"
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                isError = true
                errorMessage = "Please enter a valid email"
            }
            password.isEmpty() -> {
                isError = true
                errorMessage = "Please enter a password"
            }
            password.length < 6 -> {
                isError = true
                errorMessage = "Password must be at least 6 characters"
            }
            confirmPassword.isEmpty() -> {
                isError = true
                errorMessage = "Please confirm your password"
            }
            password != confirmPassword -> {
                isError = true
                errorMessage = "Passwords do not match"
            }
            UserRepository.getUserByEmail(email) != null -> {
                isError = true
                errorMessage = "Email already registered"
            }
            else -> {
                isError = false
                errorMessage = ""
                UserRepository.registerUser(User(fullName, phoneNumber, email, password, UserRole.PATIENT))
                onRegister(fullName, phoneNumber, email, password)
            }
        }
    }

    Scaffold(
        topBar = {
            DentalBlissTopBar(
                title = "Registration",
                showBackButton = true,
                onBackClick = onNavigateToLogin
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Full Name
            OutlinedTextField(
                value = fullName,
                onValueChange = { 
                    fullName = it
                    isError = false 
                },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DentalBlue,
                    unfocusedBorderColor = TextSecondary
                ),
                isError = isError && fullName.isEmpty()
            )

            // Phone Number
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { 
                    phoneNumber = it.take(10).filter { it.isDigit() }
                    isError = false 
                },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DentalBlue,
                    unfocusedBorderColor = TextSecondary
                ),
                isError = isError && !phoneNumber.matches(Regex("^[0-9]{10,}$"))
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    isError = false 
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DentalBlue,
                    unfocusedBorderColor = TextSecondary
                ),
                isError = isError && (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    isError = false 
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DentalBlue,
                    unfocusedBorderColor = TextSecondary
                ),
                isError = isError && password.length < 6
            )

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it
                    isError = false 
                },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DentalBlue,
                    unfocusedBorderColor = TextSecondary
                ),
                isError = isError && password != confirmPassword
            )

            if (isError) {
                Text(
                    text = errorMessage,
                    color = DeclinedRed,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            Button(
                onClick = { validateAndRegister() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DentalBlue
                )
            ) {
                Text("Register")
            }

            // Login Link
            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Already have an account? Login",
                    color = DentalBlue
                )
            }
        }
    }
} 