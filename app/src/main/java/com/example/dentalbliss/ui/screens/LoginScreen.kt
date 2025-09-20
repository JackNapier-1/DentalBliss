package com.example.dentalbliss.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.example.dentalbliss.ui.components.DentalBlissTopBar
import com.example.dentalbliss.ui.components.FaceDetectionCamera
import com.example.dentalbliss.ui.theme.*
import com.example.dentalbliss.data.FaceAuthManager
import com.example.dentalbliss.data.UserRepository

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (UserRole) -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToPatient: () -> Unit,
    onNavigateToRegistration: () -> Unit
) {
    val context = LocalContext.current
    val faceAuthManager = remember { FaceAuthManager.getInstance(context) }
    val demoFaceId = "demo_face_id" // For demo: always use this as the face ID
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showFaceLogin by remember { mutableStateOf(false) }
    var showFaceRegistration by remember { mutableStateOf(false) }
    var isFaceDetected by remember { mutableStateOf(false) }
    var canRegisterFace by remember { mutableStateOf(false) }
    var showCameraPermissionError by remember { mutableStateOf(false) }
    
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // Handle camera permission result
    LaunchedEffect(cameraPermissionState.status.isGranted) {
        if (!cameraPermissionState.status.isGranted) {
            showCameraPermissionError = true
        }
    }

    Scaffold(
        topBar = {
            DentalBlissTopBar(
                title = "Login",
                showBackButton = false
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Role Selection Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { 
                        selectedRole = UserRole.ADMIN
                        showError = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRole == UserRole.ADMIN) DentalBlue else TextSecondary
                    )
                ) {
                    Text(text = "Admin")
                }
                
                Button(
                    onClick = { 
                        selectedRole = UserRole.PATIENT
                        showError = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRole == UserRole.PATIENT) DentalBlue else TextSecondary
                    )
                ) {
                    Text(text = "Patient")
                }
            }

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    showError = false
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DentalBlue,
                    unfocusedBorderColor = TextSecondary
                ),
                isError = showError
            )

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    showError = false
                },
                label = { Text("Password") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DentalBlue,
                    unfocusedBorderColor = TextSecondary
                ),
                isError = showError
            )

            if (showError) {
                Text(
                    text = errorMessage,
                    color = DeclinedRed,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Login Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Regular Login Button
                Button(
                    onClick = { 
                        when {
                            selectedRole == null -> {
                                showError = true
                                errorMessage = "Please select a role"
                            }
                            email.isEmpty() -> {
                                showError = true
                                errorMessage = "Please enter your email"
                            }
                            password.isEmpty() -> {
                                showError = true
                                errorMessage = "Please enter your password"
                            }
                            // Check admin hardcoded
                            selectedRole == UserRole.ADMIN && email == "admin@dentalbliss.com" && password == "admin123" -> {
                                showError = false
                                showFaceRegistration = true
                            }
                            // Check patient hardcoded
                            selectedRole == UserRole.PATIENT && email == "patient@dentalbliss.com" && password == "patient123" -> {
                                showError = false
                                showFaceRegistration = true
                            }
                            // Check registered users for both roles
                            UserRepository.validateCredentials(email, password, selectedRole!!) -> {
                                showError = false
                                showFaceRegistration = true
                            }
                            else -> {
                                showError = true
                                errorMessage = "Invalid credentials"
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DentalBlue
                    )
                ) {
                    Text("Login")
                }

                // Face Login Button
                Button(
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            showFaceLogin = true
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DentalBlue
                    )
                ) {
                    Text("Face Login")
                }
            }

            // Registration Link
            TextButton(
                onClick = onNavigateToRegistration,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Don't have an account? Register",
                    color = DentalBlue
                )
            }
        }
    }

    // Face Registration Dialog
    if (showFaceRegistration) {
        AlertDialog(
            onDismissRequest = { showFaceRegistration = false },
            title = { Text("Face Registration") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        FaceDetectionCamera(
                            onFaceDetected = { detected ->
                                isFaceDetected = detected
                                canRegisterFace = detected
                            }
                        )
                    }
                    if (isFaceDetected) {
                        Text(
                            text = "Face detected! Click 'Register Face' to save.",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    } else {
                        Text(
                            text = "No face detected.",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Row {
                    TextButton(
                        onClick = { showFaceRegistration = false }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            if (isFaceDetected && selectedRole != null) {
                                faceAuthManager.registerFace(demoFaceId, selectedRole!!)
                                showFaceRegistration = false
                                onLoginSuccess(selectedRole!!)
                            }
                        },
                        enabled = canRegisterFace && selectedRole != null
                    ) {
                        Text("Register Face")
                    }
                }
            }
        )
    }

    // Face Login Dialog
    if (showFaceLogin) {
        AlertDialog(
            onDismissRequest = { showFaceLogin = false },
            title = { Text("Face Login") },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    FaceDetectionCamera(
                        onFaceDetected = { detected ->
                            isFaceDetected = detected
                            if (detected) {
                                val role = faceAuthManager.getFaceRole(demoFaceId)
                                if (role != null) {
                                    showFaceLogin = false
                                    onLoginSuccess(role)
                                } else {
                                    showError = true
                                    errorMessage = "Face not registered"
                                    showFaceLogin = false
                                }
                            }
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showFaceLogin = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Camera Permission Error Dialog
    if (showCameraPermissionError) {
        AlertDialog(
            onDismissRequest = { showCameraPermissionError = false },
            title = { Text("Camera Permission Required") },
            text = { 
                Text("Camera permission is required for face recognition. Please grant camera permission in your device settings to use this feature.")
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showCameraPermissionError = false
                        cameraPermissionState.launchPermissionRequest()
                    }
                ) {
                    Text("Grant Permission")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCameraPermissionError = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

