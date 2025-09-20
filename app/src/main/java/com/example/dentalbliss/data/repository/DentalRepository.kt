package com.example.dentalbliss.data.repository

import com.example.dentalbliss.data.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface DentalRepository {
    // Patient operations
    suspend fun getPatient(patientId: String): Patient?
    suspend fun createPatient(patient: Patient): Boolean
    suspend fun updatePatient(patient: Patient): Boolean

    // Dentist operations
    suspend fun getDentist(dentistId: String): Dentist?
    suspend fun getAllDentists(): List<Dentist>
    suspend fun getDentistAvailability(dentistId: String): List<Availability>

    // Appointment operations
    suspend fun createAppointment(appointment: Appointment): Boolean
    suspend fun updateAppointment(appointment: Appointment): Boolean
    suspend fun getAppointment(appointmentId: String): Appointment?
    suspend fun getPatientAppointments(patientId: String): Flow<List<Appointment>>
    suspend fun getDentistAppointments(dentistId: String): Flow<List<Appointment>>
    suspend fun getUpcomingAppointments(): Flow<List<Appointment>>

    // Payment operations
    suspend fun createPayment(payment: Payment): Boolean
    suspend fun updatePayment(payment: Payment): Boolean
    suspend fun getPayment(paymentId: String): Payment?
    suspend fun getAppointmentPayment(appointmentId: String): Payment?
    suspend fun getPendingPayments(): Flow<List<Payment>>

    // Authentication
    suspend fun login(email: String, password: String): String? // Returns userId if successful
    suspend fun validateCredentials(email: String, password: String): Boolean
} 