package com.example.dentalbliss.data

import androidx.compose.runtime.mutableStateListOf
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class AppointmentData(
    val date: LocalDate,
    val time: LocalTime,
    val service: String,
    val patientName: String // You can add more fields as needed
)

object AppointmentRepository {
    val appointments = mutableStateListOf<AppointmentData>()
} 