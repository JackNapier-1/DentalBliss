package com.example.dentalbliss.data.model

import java.time.LocalDateTime

data class Patient(
    val patientId: String,
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val address: String
)

data class Dentist(
    val dentistId: String,
    val name: String,
    val specialization: String,
    val schedule: List<Availability>
)

data class Appointment(
    val appointmentId: String,
    val patientId: String,
    val dentistId: String,
    val dateTime: LocalDateTime,
    val status: AppointmentStatus,
    val service: String
)

data class Payment(
    val paymentId: String,
    val appointmentId: String,
    val amount: Double,
    val status: PaymentStatus,
    val timestamp: LocalDateTime
)

data class Availability(
    val dentistId: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String
)

enum class AppointmentStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}

enum class PaymentStatus {
    PENDING,
    COMPLETED,
    REFUNDED
} 